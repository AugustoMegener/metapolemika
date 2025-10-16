package kito.metapolemika.core.form.sheet

import kito.metapolemika.core.form.sheet.SheetForm.Companion.SHEET
import kito.metapolemika.reflect.ClassRegister
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.serializer
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

open class SheetSerializer<T : SheetForm<*>> : KSerializer<T> {
    override val descriptor = buildClassSerialDescriptor("sheet")

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): T =
        with(decoder.beginStructure(descriptor)) {
            var i = 0
            val id = decodeStringElement(descriptor, i++)

            val sheetClass = (ClassRegister.of<SheetForm<*>>(SHEET).find { it.findAnnotation<Sheet>()?.id == id }
                ?: throw IllegalArgumentException("No sheet with $id id!"))

            val sheet = (sheetClass.let { cls -> cls.constructors.find { it.hasAnnotation<SheetBuilder>() }
                ?: cls.primaryConstructor }
                ?: throw IllegalStateException("No valid contrutor found for $sheetClass"))
                .call(decodeSerializableElement(descriptor, i++, BaseSheetForm.serializer()))

            sheet.fields.forEach {
                it.input = decodeNullableSerializableElement(descriptor, i++, String.serializer())
            }

            sheet as T
        }


    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: T) {
        with(encoder.beginStructure(descriptor)) {
            var i = 0

            encodeStringElement(descriptor, i++, value.id)
            encodeSerializableElement(descriptor, i++, serializer(), value.sheet)

            value.fields.forEach { encodeNullableSerializableElement(descriptor, i++, serializer(), it.input) }
        }
    }

}