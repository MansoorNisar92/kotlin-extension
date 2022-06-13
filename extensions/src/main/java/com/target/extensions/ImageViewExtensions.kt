package com.target.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/*fun AppCompatImageView.loadImageIntoView(url: String?, fullName: String?) {
    *//*val profileUrl = url.getValidUrl()
    if (profileUrl?.isNotEmpty().default){
        loadImage(profileUrl)
    }else{
        setImageDrawable(fullName.nameDrawable(this.context))
    }*//*
}*/

fun AppCompatImageView.loadCoverImageIntoView(url: String?, baseUrl:String, @DrawableRes resourceId:Int?) {
    val profileUrl = url.getValidUrl(baseUrl)
    if (profileUrl?.isNotEmpty().default){
        loadImage(profileUrl)
    }else if (resourceId!=null && resourceId!= 0){
        this.setImageResource(resourceId)
    }else {
        this.setImageResource(R.drawable.default_cover)
    }
}

fun AppCompatImageView.loadImage(url: String?, placeHolder: Int = R.drawable.default_cover) {
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .apply(RequestOptions()).
        placeholder(placeHolder).into(this)
}


fun ImageView.loadImageFromAssets(resourceId: Int?, placeHolder: Int = R.drawable.default_cover) {
    Glide.with(this).load(resourceId).apply(RequestOptions()).placeholder(placeHolder).into(this)
}

fun ImageView.loadImageFromUri(uri: Uri, placeHolder: Int = R.drawable.default_cover ) {
    Glide.with(this).load(uri).apply(RequestOptions()).placeholder(placeHolder).into(this)
}
inline fun String?.downloadFromUrl(context: Context, crossinline downloadComplete: (Bitmap) -> Unit) =
    Glide.with(context)
        .asBitmap()
        .load(this)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                downloadComplete(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                //
            }
        })
