package com.target.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import androidx.core.text.HtmlCompat
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

const val URL_REGEX: String = "(?:http|https)://(?:www\\\\.)?\\\\S+(?:/|\\\\b)"
const val EMPTY_STRING = ""
const val HTTP_SCHEME = "http://"
const val HTTPS_SCHEME = "https://"
const val FACE_BOOK = "com.facebook.katana"
const val LINKEDIN = "com.linkedin"
const val TWITTER = "com.twitter.android"
fun String?.getValidUrl(baseUrl:String): String? {
    var url: String?
    this.apply {
        url = if (this?.trim()?.isEmpty().default) {
            EMPTY_STRING
        } else if (this?.startsWith("http")?.inverse.default) {
            if (this?.startsWith("/download").default) {
                baseUrl + "fm" + this
            } else {
                baseUrl + "fm/download/" + this
            }
        } else {
            EMPTY_STRING
        }
    }
    return url
}

fun String?.addNextLine() = this + "\n"

fun String?.emptyIfNull() = this ?: EMPTY_STRING

val String?.default
    get() = emptyIfNull()

val String?.isUrl
    get() = Patterns.WEB_URL.matcher(this).matches()

val String.appendUrlScheme
    get() = if (startsWith(HTTP_SCHEME) or startsWith(HTTPS_SCHEME)) this else """$HTTP_SCHEME$this"""

fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY

fun String?.toHtml() = HtmlCompat.fromHtml(default, HtmlCompat.FROM_HTML_MODE_LEGACY)

fun String.containsHTTPS(): Boolean = this.contains("https://")

fun String.containsHTTP(): Boolean = this.contains("http://")

val String?.toBoolean
    get() = this != "0"

fun String.urlExtension(): String = MimeTypeMap.getFileExtensionFromUrl(this)

fun String.appendTwoStringsWithSpaceAsSeperator(second: String?) = "$this $second"

fun String.appendTwoStringsWithCustomSeperator(second: String?, seperator: String) = "$this$seperator$second"

fun String.appendTwoStrings(second: String?) = "$this$second"

fun String.toUpperCase(): String = this.capitalize()

fun String.appendAll(vararg strings: String): String {
    var string = this
    strings.forEachIndexed { _, c ->
        string += c
    }
    return string
}


fun String.stringToBoolean(): Boolean {
    var string = this
    var isTrue = false
    isTrue(string.equals("true", ignoreCase = true)) {
        isTrue = true
    } ?: kotlin.run {
        isTrue = false
    }
    return isTrue
}

val String.removeLastChar: String
    get() = this.substring(0, this.length.minus(1))


val String.isValidURl
    get() = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE)
        .matcher(this).matches()

fun String.toUpperCaseAllCharacters(): String {

    val value = this
    var result = ""

    for (element in value) {

        result += if (element in 'a'..'z') {

            val distance = element - 'a'
            'A' + distance
        } else {

            element
        }
    }

    return result
}

fun String.toLowerCaseAllCharacters(): String {

    val value = this
    var result = ""

    for (element in value) {
        result += if (element in 'A'..'Z') {
            val distance = element - 'A'
            'a' + distance
        } else {
            element
        }
    }

    return result
}


fun String?.isValidUrl(): Boolean {
    if (this == null) {
        return false
    }
    if (URLUtil.isValidUrl(this) && Patterns.WEB_URL.matcher(this).matches()) {
        return true
    }
    return false
}

fun String?.replaceWithThisIfNullOrEmpty(nextString: String) =
    if (isNullOrEmpty().not()) this else nextString

fun String.replaceMyTextWithPlaceholder(placeholder: String, replaceWith: String) =
    replace(placeholder, replaceWith)

@SuppressLint("SimpleDateFormat")
fun String?.changeServerDateTimeFormat(requiredFormat: String?): String {
    var time = this
    if (time == null || time == "null" || time.isEmpty()) return ""
    time = time.getYearFixedTime()
    var convertedTime = ""
    val shortFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") //0000-01-01T09:05:00Z
    shortFormat.timeZone = TimeZone.getTimeZone("UTC") //2018-07-30T20:45:38.155645Z
    val longFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") //0000-01-01T09:05:00Z
    longFormat.timeZone = TimeZone.getTimeZone("UTC") //2018-07-30T20:45:38.155645Z
    var date: Date? = null
    try {
        date = shortFormat.parse(time)
    } catch (e: ParseException) {
    }
    if (date == null) {
        try {
            date = longFormat.parse(time)
        } catch (e: ParseException) {
        }
    }
    try {
        val dateFormat: DateFormat = SimpleDateFormat(requiredFormat)
        convertedTime = dateFormat.format(date)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return convertedTime
}

@SuppressLint("SimpleDateFormat")
fun String?.changeServerDateTimeFormatProfile(requiredFormat: String?): String {
    var time = this
    if (time == null || time == "null" || time.isEmpty()) return ""
    time = time.getYearFixedTime()
    var convertedTime = ""
    val shortFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") //0000-01-01T09:05:00ZshortFormat.timeZone = TimeZone.getTimeZone("UTC") //2018-07-30T20:45:38.155645Z
    val longFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") //0000-01-01T09:05:00Z
    longFormat.timeZone = TimeZone.getTimeZone("UTC") //2018-07-30T20:45:38.155645Z
    var date: Date? = null
    try {
        date = shortFormat.parse(time)
    } catch (e: ParseException) {
    }
    if (date == null) {
        try {
            date = longFormat.parse(time)
        } catch (e: ParseException) {
        }
    }
    try {
        val dateFormat: DateFormat = SimpleDateFormat(requiredFormat)
        convertedTime = dateFormat.format(date)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return convertedTime
}



fun String.getYearFixedTime(): String {
    var yearFixedTime = this
    if (yearFixedTime.startsWith("0000")) {
        yearFixedTime = yearFixedTime.replace("0000", "1970")
    }
    return yearFixedTime
}