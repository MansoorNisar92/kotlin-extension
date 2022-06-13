package com.target.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.io.Serializable

fun AppCompatActivity.enableFullScreenMode(hasFocus: Boolean) = kotlin.run {
    if (hasFocus)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
}

val AppCompatActivity.getWindowWidth
    get() = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }.widthPixels

val AppCompatActivity.getWindowHeight
    get() = DisplayMetrics().apply {
        windowManager.defaultDisplay.getMetrics(this)
    }.heightPixels


fun AppCompatActivity.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()


fun AppCompatActivity.hideKeyboard() =
    (getSystemService(android.app.Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }


fun AppCompatActivity.connectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


inline fun <reified T> AppCompatActivity.navigate(
    vararg pair: Pair<String, Any> = arrayOf(),
    optionalFlags:ArrayList<Int> = arrayListOf(),
    requestCode: Int = -2298,
    finish: Boolean = false
) {
    val intent = Intent(this, T::class.java).apply {
        if (pair.size.greaterThan(0)){
            pair.forEach {
                val key = it.first

                when(val value = it.second){
                    is String -> {
                        putExtra(key, value)
                    }

                    is Int -> {
                        putExtra(key, value)
                    }

                    is Long -> {
                        putExtra(key, value)
                    }

                    is Float -> {
                        putExtra(key, value)
                    }

                    is Boolean -> {
                        putExtra(key, value)
                    }

                    is IntArray -> {
                        putExtra(key, value)
                    }

                    is ArrayList<*> -> {
                        putExtra(key, value)
                    }

                    is Serializable -> {
                        putExtra(key, value)
                    }

                    is Parcelable -> {
                        putExtra(key, value)
                    }
                }
            }
        }

        if (optionalFlags.size.greaterThan(0)){
            optionalFlags.forEach {
                flags = it
            }
        }
    }

    isTrue(requestCode!= -2298){
        startActivityForResult(intent, requestCode)
    }?: kotlin.run {
        startActivity(intent)
    }

    isTrue(finish){
        finish()
    }
}

fun AppCompatActivity.showCustomToast(
    message: String,
    isForError:Boolean = false
) {
    val inflater = layoutInflater
    val view = inflater.inflate(R.layout.custom_toast_view, findViewById(R.id.custom_toast_container))
    val updatedDrawable = if (isForError){
        R.drawable.ic_error
    }else {
        R.drawable.ic_green_done
    }
    view.apply {
        findViewById<ImageView>(R.id.toastIv).apply {
            setBackgroundResource(updatedDrawable)
        }
        findViewById<TextView>(R.id.toastMessageTv).text = message
    }

    Toast(this).apply {
        duration = Toast.LENGTH_SHORT
        setView(view)
        show()
    }
}

fun AppCompatActivity.openVideoPlayer(playableUrl: String) {
    val intent =  Intent(Intent.ACTION_VIEW)
    val data = Uri.parse(playableUrl)
    intent.setDataAndType(data, "video/mp4")
    startActivity(intent)
}

fun AppCompatActivity.addChip(
    @LayoutRes layoutRes: Int,
    viewGroup: ViewGroup,
    chip: (Chip) -> Unit
) {
    val chipView = inflateLayout(layoutRes, viewGroup) as Chip
    (viewGroup as ChipGroup).addView(chipView)
    chip(chipView)
}

fun AppCompatActivity.inflateLayout(@LayoutRes layoutRes: Int, container: ViewGroup?): View? =
    layoutInflater.inflate(layoutRes, container, false)

inline fun <reified T> AppCompatActivity.navigateToRoot(){
    this.apply {
        val intent = Intent(this, T::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        this.startActivity(intent)
        finish()

    }
}