package kito.metapolemika.core

interface StatefulSerializable<T : Any> {
    fun   toData(): T
    fun fromData(data: T)
}