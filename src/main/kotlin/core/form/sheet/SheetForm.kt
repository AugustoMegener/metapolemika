package kito.metapolemika.core.form.sheet

import dev.kordex.core.i18n.types.Key
import kito.metapolemika.core.form.FormBase
import kito.metapolemika.core.form.sheet.SheetForm.Companion.SHEET
import kito.metapolemika.database.table.sheet.ISheet
import kito.metapolemika.reflect.ClassRegister
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import kotlin.reflect.full.findAnnotations

@ClassRegister.Registry(SHEET)
abstract class SheetForm<T>(val title: Key, val sheetClass: UUIDEntityClass<T>, val sheet: BaseSheetForm) :
    FormBase<ISheet<T>>() where T : UUIDEntity,
                                T : ISheet<T>
{
    override val id get() = this::class.findAnnotations(Sheet::class).first().id

    open val additionalFields = listOf<Field<*>>()
    val totalFields get() = sheet.fields + fields + additionalFields

    override val isValid get() = totalFields.all { it.isValid }

    open fun T.additionalMount() {}

    override fun mount() =
        sheetClass.new(sheet.mount().id.value) { fields.forEach { it.apply(this) }; additionalMount() }

    companion object {
        const val SHEET = "sheet"
    }

    /*object Serializer : KSerializer<SheetForm<*>> {
        override val descriptor = buildClassSerialDescriptor("sheet")

        @OptIn(ExperimentalSerializationApi::class)
        override fun deserialize(decoder: Decoder): SheetForm<*> =
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

                sheet
            }


        @OptIn(ExperimentalSerializationApi::class)
        override fun serialize(encoder: Encoder, value: SheetForm<*>) {
            with(encoder.beginStructure(descriptor)) {
                var i = 0

                encodeStringElement(descriptor, i++, value.id)
                encodeSerializableElement(descriptor, i++, serializer(), value.sheet)

                value.fields.forEach { encodeNullableSerializableElement(descriptor, i++, serializer(), it.input) }
            }
        }

    }*/
}