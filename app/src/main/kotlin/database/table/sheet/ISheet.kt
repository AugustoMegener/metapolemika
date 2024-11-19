package database.table.sheet

import kito.metapolemika.database.entity.sheet.SheetData
import org.jetbrains.exposed.dao.UUIDEntity

interface ISheet<T> where T : UUIDEntity,
                          T : ISheet<T>
{ val self: T
  val sheet: SheetData
}