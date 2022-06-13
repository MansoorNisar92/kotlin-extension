package com.target.extensions

import java.util.*

fun getCurrentCalendar() : Calendar = Calendar.getInstance()

val Calendar.getCurrentYear : Int
    get() = get(Calendar.YEAR)

val Calendar.getCurrentMonth : Int
    get() = get(Calendar.MONTH)

val Calendar.getCurrentDay : Int
    get() = get(Calendar.DAY_OF_MONTH)

fun Calendar.setCurrentYear(year : Int) {
    set(Calendar.YEAR, year)
}

fun Calendar.setCurrentMonth(month : Int) {
    set(Calendar.MONTH, month)
}

fun Calendar.setCurrentDay(day : Int) {
    set(Calendar.DAY_OF_MONTH, day)
}
