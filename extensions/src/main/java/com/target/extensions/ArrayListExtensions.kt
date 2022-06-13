package com.target.extensions

const val DEFAULT_ARRAY_SIZE = 0

fun <T> ArrayList<T>.removeFrom(startIndex: Int = 0, finalIndex: Int) = kotlin.run {
    val newList = arrayListOf<T>()
    filterIndexed { index, t ->
        (index in startIndex..finalIndex && finalIndex <= lastIndex).isTrue {
            newList.add(t)
        }
        true
    }
    removeAll(newList)
    this
}

val <T> ArrayList<T>.firstIndex
    get() = indexOf(first())

fun <T> List<T>?.orEmptyArrayList(): ArrayList<T> = emptyIfNull as ArrayList<T>

val <T> List<T>?.hasDataToShow
    get() = isNullOrEmpty().not()

fun <T> List<T>?.hasDataToShow(list: List<T>.() -> Unit) =
    if (isNullOrEmpty().not()) list(this!!) else null

fun <T> List<T>?.hasValidData(list: (List<T>) -> Unit) =
    if (isNullOrEmpty().not()) list(this!!) else null

fun <T> List<T>?.isNotEmptyOrNull(list: (List<T>) -> Unit) =
    if (isNullOrEmpty().not()) list(this!!) else null

fun <T> List<T>?.get() = if (isNullOrEmpty().not()) this ?: emptyList() else emptyList()

fun <T> listWithCapacity(capacity: Int) =
    arrayListOf<T>()
        .apply { ensureCapacity(capacity) }

fun <T> List<T>.hasItem(index: Int): T? = if (has(index)) this[index] else null

fun <T> List<T>.has(index: Int): Boolean = index.lessThan(size)

val <T> ArrayList<T>.validLastIndex
    get() = if (size.greaterThan(0)) lastIndex else size

fun <T> List<T>.validIndex(required: Int) = if (size.lessThan(required)) lastIndex else required

val <T> List<T>.castToArrayList: ArrayList<T>
    get() = this as ArrayList<T>

val <T> List<T>.castToMutableList: MutableList<T>
    get() = this as MutableList<T>

fun <T> List<T>.contains(element: T, contained: (T) -> Unit) =
    isTrue(contains(element)) { contained(element) }

fun <T> List<T>.isLastItem(index: Int) = lastIndex == index

fun <T> List<T>.isLastItem(element: T) = indexOf(element) == lastIndex

val <T> List<T>.hasMoreThanOneItem
    get() = size.greaterThan(1)

val <T> List<T>.hasOnlyOneItem
    get() = size == 1

val <T> List<T>.hasOnlyTwoItems
    get() = size == 2

val <T> List<T>.hasOnlyThreeItems
    get() = size == 3

val <T> List<T>.hasOnlyFourItems
    get() = size == 4

val <T> List<T>.hasMoreThanFourItems
    get() = size.greaterThan(4)

fun <T> makeList(size: Int = 5, item: T) = arrayListOf<T>().apply {
    for (i in 0 until size)
        add(item)
}

inline fun <reified T> makeList(size: Int = 5, itemCallBack: (index: Int, item: T) -> Unit) =
    arrayListOf<T>().apply {
        for (i in 0 until size)
            add(T::class.java.newInstance().apply {
                itemCallBack(i, this)
            })
    }

fun <T> List<T>.hasItemsMoreThan(value: Int) = size - 1 > value

fun <T> List<T>?.indexOfItem(predicate: (T) -> Boolean): Int? =
    this?.find(predicate)?.let { indexOf(it) }

fun <T> ArrayList<T>?.createIfNull(): ArrayList<T> = this ?: arrayListOf()

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}

fun <T> List<T>.replace(onIndex: Int, newValue: T): List<T> {
    return mapIndexed { index, t ->
        if (index == onIndex) newValue else t
    }
}

fun <T> List<T>.replace(newValue: T): List<T> {
    return map {
        newValue
    }
}

val <T> List<T>.firstIndex
    get() = indexOf(first())

val <T> List<T>?.emptyIfNull
    get() = this ?: arrayListOf()

fun <T> List<T>?.ifNullThen(list: List<T>) = this ?: list

fun <T> List<T>.swapPositions(from: Int, to: Int): List<T> =
    toMutableList().apply {
        val temp = get(from)
        set(from, get(to))
        set(to, temp)
    }

fun <T> List<T>.filterAll(filters: List<(T) -> Boolean>) =
    filter { item -> filters.all { filter -> filter(item) } }

fun <T> List<T>.performAddition(): Long {
    var totalSum = 0L
    this.apply {
        forEachIndexed { _, listItem ->
            totalSum = totalSum.plus((listItem as Long))
        }
    }
    return totalSum
}

fun <T> ArrayList<T>.delete(predicate: (T) -> Boolean): List<T> {
    return apply {
        find(predicate)?.let { value ->
            remove(value)
        }
    }
}

fun <T> MutableList<T>.removeSecond(): T =
    if (isEmpty()) throw NoSuchElementException("List is empty.") else removeAt(1)

fun <T> MutableList<T>.removeNth(index: Int = 0): T =
    if (isEmpty()) throw NoSuchElementException("List is empty.") else removeAt(index)

fun <T> List<T>.second(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[1]
}

fun <T> List<T>.third(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[2]
}

fun <T> List<T>.fourth(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[3]
}

fun <T> List<T>.fifth(): T {
    if (isEmpty())
        throw NoSuchElementException("List is empty.")
    return this[4]
}

fun <T> ArrayList<T>.subListFromList(startIndex:Int, endIndex:Int) = arrayListOf<T>().apply {
    val list = this@subListFromList.subList(startIndex,endIndex)
    list.forEach {
        add(it)
    }
}
