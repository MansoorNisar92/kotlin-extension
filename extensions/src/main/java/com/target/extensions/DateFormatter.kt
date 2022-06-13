package com.target.extensions

import java.text.SimpleDateFormat
import java.util.*

private val simpleDateTimeFormat: SimpleDateFormat =
    SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ROOT)
private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ROOT)
private val remoteSimpleDateFormat: SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSS", Locale.ROOT)
private val traditionalDateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
private val remoteSimpleDateFormatWithTimeZone: SimpleDateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

private val humanReadableSimpleDateFormatWithTimeZone: SimpleDateFormat =
    SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

private val simpleTimeFormat: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ROOT)

private val numericSimpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.ROOT)

val String?.formatToDateTime: String
    get() = simpleDateTimeFormat.format(
        try {
            remoteSimpleDateFormat.parse(emptyIfNull()) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    )

val String?.formatToDate: String
    get() = simpleDateFormat.format(
        try {
            remoteSimpleDateFormat.parse(emptyIfNull()) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    )


val Calendar.getTraditionalDate: String
    get() = traditionalDateFormat.format(time)

val String?.parseToTraditionalDate: Date
    get() = try {
        traditionalDateFormat.parse(default) ?: Date()
    } catch (e: Exception) {
        Date()
    }

val String?.parseToTraditionalDateAndConvertToString: String
    get() = parseToTraditionalDate.time.toString()

val String?.makeTraditionalFormatCalendar: Calendar
    get() = Calendar.getInstance().apply {
        time = parseToTraditionalDate
    }

val String?.formatToNumericDateTimeFormat: String
    get() = numericSimpleDateFormat.format(
        try {
            remoteSimpleDateFormat.parse(emptyIfNull()) ?: Date()
        } catch (e: Exception) {
            Date()
        }
    )

val Long.parseToTraditionalDate: String
    get() = traditionalDateFormat.format(Date(this))

val Date.parseToSimpleDate: String
    get() = simpleDateFormat.format(this)

val Long.parseToSimpleTimeFormat: String
    get() = simpleTimeFormat.format(Date(this))

val getCurrentTimeInMilliseconds: Long
    get() = Calendar.getInstance().timeInMillis

val String.parseToMessageSentTime: String
    get() = simpleTimeFormat.format(this.parseToMessageDate)

val Date.parseToMessageSentTime: String
    get() = remoteSimpleDateFormat.format(this).toString()

val String?.parseToMessageDate: Date
    get() = try {
        remoteSimpleDateFormatWithTimeZone.parse(default) ?: Date()
    } catch (e: Exception) {
        Date()
    }

val String?.parseStringToDate: Date
    get() = try {
        humanReadableSimpleDateFormatWithTimeZone.parse(default) ?: Date()
    } catch (e: Exception) {
        Date()
    }

val String.toMilliseconds: Long
    get() = this.parseToMessageDate.time

val String.parseToMilliseconds: Long
    get() = this.parseStringToDate.time

val Long.parseToSimpleDate : String
    get() = simpleDateFormat.format(Date(this))