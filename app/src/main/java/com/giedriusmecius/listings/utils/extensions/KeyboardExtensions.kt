package com.giedriusmecius.listings.utils.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText.showKeyboard() {
    if (!hasFocus()) {
        setSelection(text.length)
        requestFocus()
    }
    val inputManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard() {
    val inputManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

