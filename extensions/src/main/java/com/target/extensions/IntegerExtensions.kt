@file:Suppress("UNCHECKED_CAST")

package com.target.extensions

import android.content.Context
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.roundToLong

const val DEFAULT_INT_VALUE = 0
const val DEFAULT_PAGE_VALUE = 1

const val ONE = 1
const val TWO = 2

fun Int.lessThan(value: Int) = this < value

fun Int.lessThanEqualsTo(value: Int) = this <= value

fun Int.greaterThanEqualsTo(value: Int) = this >= value

fun Int.greaterThan(value: Int) = this > value

fun Number.asFormatted(format: String) = String.format(format, this)

val Number.toDp
    get() = toInt().times(4)

fun Number.toDp(context: Context) =
    toInt().times(context.resources.getDimension(com.intuit.sdp.R.dimen._1sdp)).toInt()

fun IntRange.getMemberFromIndex(index: Int) = toList()[index]

val Number.toSeconds
    get() = TimeUnit.SECONDS.toMillis(toLong())

fun Number.lessThan(value: Number) = this.toDouble() < value.toDouble()

fun Number.lessThanEqualsTo(value: Number) = this.toDouble() <= value.toDouble()

fun Number.greaterThanEqualsTo(value: Number) = this.toDouble() >= value.toDouble()

fun Number.greaterThan(value: Number) = this.toDouble() > value.toDouble()

fun Number.toBoolean() = this == 1

fun Int.toBoolean() = this == 1

val Number?.formatInto2DecimalPlaces: Double
    get() = DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US)).format(this)
        .toDouble()


val Int?.default
    get() = this ?: DEFAULT_INT_VALUE


private fun Long.isMultipleOf(interval: Long, byMax: Int = Int.MAX_VALUE) =
    greaterThan(byMax).inverse and (this % interval == 0L)

fun <T : Number> Number.isEqualTo(other: Number) = if (this == other) this as T else null

fun <T : Number> Number.isNotEqualTo(other: Number) = if (this != other) this as T else null

fun <T : Any> Any.isEqualTo(other: T) = if (this == other) this as T else null

fun <T : Any> Any.isNotEqualTo(other: T) = if (this != other) this as T else null

fun <T : Any, R : Any> Any.isEqualTo(other: T, returnThis: R) =
    if (this == other) returnThis else null

fun Int.toArrayList(context: Context): ArrayList<String>{
    return context.resources.getStringArray(this).toList() as ArrayList<String>
}

fun Int.toArray(context: Context): Array<String>{
    return context.resources.getStringArray(this)
}

val Long?.default
    get() = this ?: 0L
