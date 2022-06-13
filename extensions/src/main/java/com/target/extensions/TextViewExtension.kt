package com.target.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.text.SimpleDateFormat
import java.util.*

private fun getCalendarInstance(): Calendar = Calendar.getInstance()

@SuppressLint("SimpleDateFormat")
fun AppCompatTextView.pickDateDialog(
    activity: Activity,
    format: String = "dd/MM/yyyy",
    shouldEnableFutureDates: Boolean = true,
    shouldSetMinData:Boolean = false,
    minDate: Date? = null,
    dateCallBack:(String) -> Unit
) {
    val textView: AppCompatTextView = this
    val calendar = getCalendarInstance()
    val datePickerDialog = DatePickerDialog(
        activity,
        { datePicker, _, _, _ ->
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
            calendar.set(Calendar.MONTH, datePicker.month)
            calendar.set(Calendar.YEAR, datePicker.year)
            val formatted = SimpleDateFormat(format).format(calendar.time)
            textView.text = formatted
            dateCallBack(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
    isTrue(shouldEnableFutureDates.inverse) {
        datePickerDialog.datePicker.maxDate = Date().time
    }

    isTrue(shouldSetMinData){
        minDate?.nonNull {
            datePickerDialog.datePicker.minDate = time
        }
    }
}

fun AppCompatTextView.novaBold(context: Context){
    setTypeface(ResourcesCompat.getFont(context, R.font.proxima_nova_bold), Typeface.BOLD)
}

fun AppCompatTextView.novaRegular(context: Context, ){
    setTypeface(ResourcesCompat.getFont(context, R.font.proxima_nova_regular), Typeface.NORMAL)
}

fun AppCompatTextView.setDrawables(left: Drawable? = null, top: Drawable? = null, right: Drawable? = null, bottom: Drawable? = null){
    this.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
}

fun TextView?.setTextOrHideIfEmpty(text: String? = "") {
    if (text.isNullOrEmpty().not()) {
        this?.text = text
        this?.show()
    } else this?.hide()
}

fun TextView?.setTextIfEmptyOrNull(text: String?) {
    if (text.isNullOrEmpty().not()) {
        this?.text = text
    }
}

fun TextView.replaceMyTextWithPlaceholder(placeholder: String, replaceWith: String) = kotlin.run {
    text = text.toString().replaceMyTextWithPlaceholder(placeholder, replaceWith)
}

fun TextView.applyColorSizeAndFont(
    @FontRes fontPath: Int,
    @ColorRes color: Int,
    @DimenRes size: Int
) {
    applyFont(fontPath)
    setTextColor(ContextCompat.getColor(context, color))
    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(size))
}

fun TextView.applyTextSize(@DimenRes dimenRes: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension(dimenRes))
}

fun TextView.applyFont(@FontRes fontPath: Int) {
    typeface = ResourcesCompat.getFont(context, fontPath)
}

fun TextView?.replaceWithThisIfNullOrEmpty(nextString: String) = kotlin.run {
    this?.setText(text.toString().replaceWithThisIfNullOrEmpty(nextString))
}

fun AppCompatTextView.updateDrawableStateAsSelected(@ColorRes color:Int) {
    this.apply {
        novaBold(this.context)
        setTextColor(
            ContextCompat.getColor(
                this.context,
                color
            )
        )
    }
}

fun AppCompatTextView.updateDrawableStateAsNormal(@ColorRes color: Int) {
    this.apply {
        novaRegular(this.context)
        setTextColor(
            ContextCompat.getColor(
                this.context,
                color
            )
        )
    }
}

