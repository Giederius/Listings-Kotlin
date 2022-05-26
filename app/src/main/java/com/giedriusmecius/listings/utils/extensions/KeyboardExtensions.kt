package com.giedriusmecius.listings.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

fun EditText.showKeyboard() {
    if (!hasFocus()) {
        setSelection(text.length)
        requestFocus()
    }
    val inputManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.showKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

//fun Fragment.hideKeyboard() {
//    view?.let { activity?.hideKeyboard(it) }
//}
//
//fun Activity.hideKeyboard() {
//    hideKeyboard(currentFocus ?: View(this))
//}

