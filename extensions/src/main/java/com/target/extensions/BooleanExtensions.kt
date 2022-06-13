package com.target.extensions

typealias Condition = Boolean.() -> Unit

const val DEFAULT_BOOLEAN_VALUE = false

inline fun Boolean.isTrue(crossinline func: Condition) = run { if (this) func(this) else null }

inline fun Boolean.isFalse(crossinline func: Condition) =
    run { if (this.not()) func(this.not()) else null }

val Boolean.inverse
    get() = not()

val Boolean?.toString
    get() = if (this == null || this == false) "0" else "1"

val Boolean?.default
    get() = this ?: false

fun Boolean.toInt() = if (this) 1 else 0

inline fun <R> isTrue(condition: Boolean?, crossinline block: () -> R?) =
    if (condition == true) block() else null

inline fun <R> isFalse(condition: Boolean?, crossinline block: () -> R?) =
    if (condition?.not() == true) block() else null