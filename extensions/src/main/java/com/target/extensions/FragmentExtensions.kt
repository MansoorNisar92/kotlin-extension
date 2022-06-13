package com.target.extensions

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

fun Fragment.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) =
    message?.let {
        Toast.makeText((requireActivity() as AppCompatActivity), message, length).show()
    }

fun Fragment.showSnackBar(
    message: String?,
    duration: Int = Snackbar.LENGTH_LONG,
    isForError: Boolean = false,
    isForAlert: Boolean = false
) =
    message?.let {
        view?.let {
            var color = colorStateList(R.color.sweetGreen)
            var textColor = colorStateList(R.color.colorWhite)
            isTrue(isForError){
                color = colorStateList(R.color.colorColdRed)
            }
            isTrue(isForAlert){
                color = colorStateList(R.color.colorGrey)
                textColor = colorStateList(R.color.black)
            }
            Snackbar.make(it, message, duration).apply {
                setBackgroundTintList(color)
                setTextColor(textColor)
            }.show()
        }
    }

fun Fragment.showCustomToast(
    message: String,
    isForError:Boolean = false
) {
    val inflater = layoutInflater
    val view = inflater.inflate(R.layout.custom_toast_view, requireView().findViewById(R.id.custom_toast_container))
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

    Toast(requireContext()).apply {
        duration = Toast.LENGTH_SHORT
        setView(view)
        show()
    }
}

fun Fragment.colorStateList(@ColorRes color: Int): ColorStateList =
    AppCompatResources.getColorStateList(requireContext(), color)

inline fun <reified T : Serializable> Fragment.getSerializableArguments(key: String) =
    requireArguments().getSerializable(key) as? T

inline fun <reified T : Parcelable> Fragment.getParcelableArguments(key: String) =
    requireArguments().getParcelable(key) as? T

inline fun <reified T : Parcelable> Fragment.getParcelableArrayListArguments(key: String) =
    requireArguments().getParcelableArrayList<Parcelable>(key) as? T

fun Fragment.getIntArgument(key: String) = requireArguments().getInt(key)
fun Fragment.getStringArgument(key: String) = requireArguments().getString(key)
fun Fragment.getBooleanArgument(key: String) = requireArguments().getBoolean(key)

fun Fragment.getIntegerArgument(key: String) = requireArguments().getInt(key)

fun Fragment.string(@StringRes stringRes: Int) = resources.getString(stringRes)

fun Fragment.drawable(@DrawableRes drawableRes: Int): Drawable? =
    AppCompatResources.getDrawable(requireContext(), drawableRes)

fun Fragment.startNewActivity(packageName: String) {
    var intent: Intent? = requireActivity().packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        // we found the activity
        // now start the activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } else {
        // bring user to the market
        // or let them choose an app?
        intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("market://details?id=$packageName")
        startActivity(intent)
    }
}

fun Fragment.showDeleteDialog(message:String,callback: () -> Unit) {
    val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
    builder1.setMessage(message)
    builder1.setCancelable(true)

    builder1.setPositiveButton(
        "Yes",
        DialogInterface.OnClickListener { dialog, id ->

            callback()
            dialog.cancel()
        })

    builder1.setNegativeButton(
        "No",
        DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

    val alert11: AlertDialog = builder1.create()
    alert11.show()
}


fun Fragment.openVideoPlayer(playableUrl: String) {
    requireActivity().apply {
        val intent =  Intent(Intent.ACTION_VIEW)
        val data = Uri.parse(playableUrl)
        intent.setDataAndType(data, "video/mp4")
        startActivity(intent)
    }
}

inline fun <reified T> Fragment.navigateToNextScreen(
    vararg pair: Pair<String, Any> = arrayOf(),
    optionalFlags:ArrayList<Int> = arrayListOf(),
    requestCode: Int = -2298
) {
    val intent = Intent(requireContext(), T::class.java).apply {
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
}
