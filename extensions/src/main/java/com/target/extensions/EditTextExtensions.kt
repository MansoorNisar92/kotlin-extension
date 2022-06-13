package com.target.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.text.InputFilter
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatEditText.enableEditing() {
    this.apply {
        isFocusable = true
        isFocusableInTouchMode = true
    }

}

fun AppCompatEditText.disableEditing() {
    this.apply {
        isFocusable = false
        isFocusableInTouchMode = false
    }

}

fun AppCompatEditText.onKeyboardDone(donActionCallBack: ()-> Unit){
    this.setOnEditorActionListener { _, actionId, _ ->
        isTrue(actionId == (EditorInfo.IME_ACTION_DONE or EditorInfo.IME_ACTION_GO) ){
            hideKeyboard()
            donActionCallBack()
            true
        }?: false
    }
}
fun TextInputEditText.onKeyboardDone(donActionCallBack: ()-> Unit){
    this.setOnEditorActionListener { _, actionId, _ ->
        isTrue(actionId == (EditorInfo.IME_ACTION_DONE or EditorInfo.IME_ACTION_GO) ){
            hideKeyboard()
            donActionCallBack()
            true
        }?: false
    }
}

fun AppCompatEditText.pickDateDialog(
    activity: Activity,
    format: String,
    shouldEnableFutureDates: Boolean = true,
    dateCallBack:(String) -> Unit
) {
    val editText: AppCompatEditText = this
    val calendar = getCalendarInstance()
    val datePickerDialog = DatePickerDialog(
        activity,
        { datePicker, _, _, _ ->
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
            calendar.set(Calendar.MONTH, datePicker.month)
            calendar.set(Calendar.YEAR, datePicker.year)
            var formatted = SimpleDateFormat(format).format(calendar.time)
            editText.setText(formatted)
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
}


private fun getCalendarInstance(): Calendar = Calendar.getInstance()

fun AppCompatEditText.get(): String{
    return this.text.toString()
}

@SuppressLint("ClickableViewAccessibility")
fun AppCompatEditText.scrollInTouchMode(){
    this.apply {
        setOnTouchListener { v, event ->
            if (hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
            false
        }
    }
}

fun EditText.setMaxLength(maxLength: Int){
    filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

}
