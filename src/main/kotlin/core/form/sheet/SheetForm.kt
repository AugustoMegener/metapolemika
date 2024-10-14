package kito.metapolemika.core.form.sheet

import kito.metapolemika.core.form.FormBase
import kito.metapolemika.database.table.sheet.ISheet
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass

abstract class SheetForm<T>(title: String, val sheetClass: UUIDEntityClass<T>, val sheet: SheetBaseForm) :
    FormBase<ISheet<T>>(title)
        where T : UUIDEntity,
              T : ISheet<T> {

    open val additionalFields = listOf<Field<*>>()
    val totalFields get() = fields + sheet.fields + additionalFields

    open fun T.additionalMount() {}

    override fun mount() =
        sheetClass.new(sheet.mount().id.value) { fields.forEach { it.apply(this); additionalMount() } }
}