package core.form.sheet

import dev.kordex.core.i18n.types.Key
import kito.metapolemika.core.form.FormBase
import kito.metapolemika.database.table.sheet.ISheet
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass

abstract class SheetForm<T>(id: String, val title: Key, val sheetClass: UUIDEntityClass<T>, val sheet: BaseSheetForm) :
    FormBase<ISheet<T>>(id) where T : UUIDEntity,
                                  T : ISheet<T>
{
    open val additionalFields = listOf<Field<*>>()
    val totalFields get() = sheet.fields + fields + additionalFields

    override val isValid get() = totalFields.all { it.isValid }

    open fun T.additionalMount() {}

    override fun mount() =
        sheetClass.new(sheet.mount().id.value) { fields.forEach { it.apply(this); additionalMount() } }
}