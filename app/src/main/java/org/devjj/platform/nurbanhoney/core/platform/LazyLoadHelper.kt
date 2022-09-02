package org.devjj.platform.nurbanhoney.core.platform

import javax.inject.Inject

interface LazyLoadHelper<T> {
    var loadedList: MutableMap<Int, T>
    var nextList: Map<Int, T>
    val ketSet: Set<Int>
    fun size(): Int
    fun hasEnough(): Boolean
    fun getNext(): Result<List<T>>
    fun storeNext(keys: Set<Int>, next: List<T>)
}

class LazyLoadHelperImpl<T>
@Inject constructor(private val capacity: Int) : LazyLoadHelper<T> {

    override var loadedList: MutableMap<Int, T> = HashMap()
    override var nextList: Map<Int, T> = HashMap()
    override val ketSet: Set<Int> = setOf()

    override fun size() = loadedList.size

    override fun hasEnough() = nextList.size >= capacity

    override fun getNext() =
        if (hasEnough()) Result.success(
            nextList.filter { !loadedList.contains(it.key) }
                .apply { loadedList.putAll(this) }
                .map { it.value }.toList()
        )
        else Result.failure(NotEnoughItemsException())

    override fun storeNext(keys: Set<Int>, next: List<T>) {
        nextList = keys.zip(next).filter { !loadedList.contains(it.first) }.toMap()
    }


}

class NotEnoughItemsException : Exception()