package com.target.extensions

import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File


inline fun <T : Any> T?.nonNull(action: T.() -> Unit) = this?.apply { action(this) }
val Context.isInternetAvailable: Boolean
    get() = kotlin.run {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

val Context.getScreenWidth: Int
    get() = getScreenWidthHeight[0]

val Context.getScreenHeight: Int
    get() = getScreenWidthHeight[1]

fun Context.getScreenHeight(percent: Int): Int =
    getScreenWidthHeight[1].times(percent.div(100.0)).toInt()

fun <T> T.createSingleItemListForItem() = makeList(size = 1, item = this)

val Context.getScreenWidthHeight: IntArray
    get() = DisplayMetrics().run {
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getRealMetrics(this)
        IntArray(2).apply {
            set(0, widthPixels)
            set(1, heightPixels)
        }
    }

fun Context.navigateToAppSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.fromParts("package", packageName, null)
    })
}

fun Context.navigate(intent: Intent) {
    startActivity(intent)
}

fun Context.navigateViaUrl(url : String) {
    navigate(createViewIntent(Uri.parse(url.appendUrlScheme)))
}

val BottomNavigationView.currentItem
    get() = selectedItemId

fun BottomNavigationView.reselectCurrentItem() {
    selectedItemId = currentItem
}

fun getFile(path: String) = File(path)

fun File.isImageFile() : Boolean = BitmapFactory.decodeFile(absolutePath) != null



fun <T> convertObjectToJsonString(model: T): String = Gson().toJson(model)

inline fun <reified T> convertJsonToModel(string: String): T? = Gson().fromJson(string, T::class.java)

fun createViewIntent(uri: Uri) = Intent(Intent.ACTION_VIEW, uri)



fun createImageFile(prefix : String, suffix : String = ".jpg") : File {
    return File.createTempFile("""$prefix${getCurrentCalendar().timeInMillis}""",
        suffix, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))

}

fun Context.showSingleChoiceDialog(
    list: Array<String>,
    selectedOptionListener: (Int) -> Unit
) {
    val builder = AlertDialog.Builder(this)
    builder.setSingleChoiceItems(
        list, // array
        -1 // initial selection (-1 none)
    ) { dialog, i ->
        dialog.dismiss()
        selectedOptionListener(i)
    }

    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

fun isContainNonAlphanumericCharacter(text: String): Boolean{
    return text.contains("[^A-Za-z0-9]".toRegex())
}
inline fun <reified T> createActivityIntent(context: Context) = Intent(context, T::class.java)

fun createPendingIntent(context: Context, intent: Intent, requestCode:Int): PendingIntent =
    PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

fun Context.copyToClipboard(text : String) =
    (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)
        ?.setPrimaryClip(ClipData.newPlainText("Target Tech", text))

fun JSONObject.parseError(): String {
    var error = ""
    if (this.has("errors")){
        val errors = this.getJSONArray("errors")
        val errorObj = errors.get(0) as JSONObject
        error = errorObj.get("message").toString()
    }
    return error
}

fun Context.isPhoneCallActive(): Boolean {
    val manager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    return manager.mode == AudioManager.MODE_IN_CALL
}