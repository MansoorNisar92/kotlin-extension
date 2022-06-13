package com.target.extensions

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams

fun View.show() = run {
    visibility = View.VISIBLE
    this
}

fun View.invisible() = run {
    visibility = View.INVISIBLE
    this
}

fun View.hide() = run {
    visibility = View.GONE
    this
}

fun View.visibility(show: Boolean, invisible: Boolean = false) = run {
    if (show) show() else if (invisible) invisible() else hide()
}

inline fun <T : View> viewVisibility(
    view: T?, show: Boolean, invisible: Boolean = false,
    crossinline showedCallBack: T.() -> Unit = {}, crossinline hideCallBack: T.() -> Unit = {}
) =
    view?.run {
        if (show) show() else if (invisible) invisible() else hide()
        isTrue(show) { showedCallBack(this) } ?: hideCallBack(this)
    }

inline fun <T : View> viewEnabled(
    view: T?, enabled: Boolean, disabled: Boolean = false,
    crossinline showedCallBack: T.() -> Unit = {}, crossinline hideCallBack: T.() -> Unit = {}
) =
    view?.run {
        when {
            enabled -> {
                alpha = 1f
                show()
            }
            disabled -> {
                alpha = .5f
            }
            else -> {
                hide()
            }
        }
        isTrue(enabled) { showedCallBack(this) } ?: hideCallBack(this)
    }


fun View.hideKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun View.dimensionInteger(@DimenRes dimenRes: Int) = dimension(dimenRes).toInt()

fun View.string(@StringRes stringRes: Int) = resources.getString(stringRes)

fun View.colorStateList(@ColorRes color: Int): ColorStateList =
    AppCompatResources.getColorStateList(context, color)

fun View.color(@ColorRes color: Int) = ContextCompat.getColor(context,color)

fun View.font(@FontRes font: Int) = ResourcesCompat.getFont(context, font)

fun ViewGroup.inflate(@LayoutRes resourceId: Int): View = View.inflate(context, resourceId, this)

fun View.getResourceValueId(a: TypedArray, styleableId: Int) = TypedValue().also {
    a.getValue(styleableId, it)
}.resourceId

fun View.getResourceValue(a: TypedArray, styleableId: Int) = TypedValue().apply {
    a.getValue(styleableId, this)
}

fun View.checkIfResourceIsColor(resourceType: Int) =
    resourceType.greaterThanEqualsTo(TypedValue.TYPE_FIRST_COLOR_INT) and resourceType.lessThanEqualsTo(
        TypedValue.TYPE_LAST_COLOR_INT
    )

fun View.colorOrDrawable(resourceId: Int, isColor: Int.() -> Unit, isDrawable: Int.() -> Unit) {
    TypedValue().apply {
        resources.getValue(resourceId, this, true)
        isTrue(checkIfResourceIsColor(type)) {
            isColor(data)
        } ?: isDrawable(resourceId)
    }
}

fun View.isValidResourceId(id: Int) =
    isValidDrawableRes(id) or isValidColorRes(id) or
            isValidDimens(id) or isValidString(id) or
            id.greaterThan(0)

fun View.isValidFont(id: Int) = try {
    font(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidString(id: Int) = try {
    string(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidDimens(id: Int) = try {
    dimension(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidDrawableRes(id: Int) = try {
    drawable(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidColorRes(id: Int) = try {
    color(id)
    true
} catch (e: Exception) {
    false
}

val View.isVisible
    get() = visibility == View.VISIBLE

val View.isGone
    get() = visibility == View.GONE

val View.isInvisible
    get() = visibility == View.INVISIBLE

inline fun View.isVisible(crossinline showed: View.() -> Unit) = run {
    if (isVisible) showed(this)
}

inline fun View.isGone(crossinline showed: View.() -> Unit) = run {
    if (isGone) showed(this)
}

inline fun View.isInvisible(crossinline showed: View.() -> Unit) {
    if (isInvisible) showed(this)
}

fun View.drawable(@DrawableRes drawableRes: Int): Drawable? =
    AppCompatResources.getDrawable(context, drawableRes)


private fun <T : AttributeSet?> View.getStyleAttributes(styleableId: IntArray, t: T): TypedArray =
    context.theme.obtainStyledAttributes(t, styleableId, 0, 0)

fun <T : AttributeSet?> View.getStyleAttributes(
    styleableId: IntArray,
    t: T,
    typedArray: TypedArray.() -> TypedArray
) =
    typedArray(context.theme.obtainStyledAttributes(t, styleableId, 0, 0)).apply {
        recycle()
    }

fun <T : AttributeSet?> ViewGroup.inflateAndGetStyleAttributes(
    @LayoutRes resourceId: Int,
    styleableId: IntArray, t: T, typedArray: TypedArray.() -> Unit
): View =
    inflate(resourceId).apply {
        typedArray(getStyleAttributes(styleableId, t))
    }


fun View.clickToAction(action: () -> Unit = {}) {
    setOnClickListener {
        hideKeyboard()
        action()
    }
}

fun View.removeCLickToAction() {
    setOnClickListener(null)
}

fun attachClickListenerToViews(vararg views: View, listener: () -> Unit = {}) {
    views.forEach { view -> view.clickToAction(listener) }
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.getRadioButtonDrawable() = RadioButton(context).buttonDrawable

@RequiresApi(Build.VERSION_CODES.M)
fun View.getCheckBoxDrawable() = CheckBox(context).buttonDrawable

fun View.createTextView(@DimenRes dimenRes: Int, @ColorRes colorRes: Int, @FontRes font: Int) =
    AppCompatTextView(context).apply {
        applyColorSizeAndFont(font, colorRes, dimenRes)
    }

fun <T : View> View.castTo() = this as T

fun View.applyMargin(@Px all: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = all
        topMargin = all
        rightMargin = all
        bottomMargin = all
    }
}

fun View.applyMargin(@Px start: Int, @Px top: Int, @Px end: Int, @Px bottom: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = start
        topMargin = top
        rightMargin = end
        bottomMargin = bottom
    }
}

fun View.disableOrEnable(shouldEnable: Boolean) {
    this.apply {
        isTrue(shouldEnable) {
            alpha = 1f
            isClickable = true
            isEnabled = true
        } ?: run {
            alpha = .5f
            isClickable = false
            isEnabled = false
        }
    }
}

fun View.hideKeyboardOnTouchOutside(){
    performClick()
    setOnTouchListener { _, _ ->
        hideKeyboard()
        performClick()
    }
}

