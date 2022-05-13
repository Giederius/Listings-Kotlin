package com.giedriusmecius.listings.utils.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isGone

fun EditText.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText.addCardAnimationWithSetText(
    addTextTo: TextView,
    animateIn: View,
    label: TextView,
    labelTitle: Int
) {
    label.text = context.getText(labelTitle)
    this.setOnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
            addTextTo.text = this.text
            animateIn.apply {
                isGone = false
                alpha = 0F
                translationY = 50F
                requestFocus()
                animate()
                    .translationY(0F)
                    .alpha(1F)
                    .setDuration(700)
                    .setListener(null)
            }

            this.animate()
                .translationY(-50F)
                .alpha(0F)
                .setDuration(400)
                .setListener(null)
                .withEndAction { v.isGone = true }
            true
        } else {
            false
        }
    }
}

fun showAlertDialog(context: Context, onPositiveClick: () -> Unit, onNegativeClick: () -> Unit) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Test")
    builder.setMessage("i\'m a test")
    builder.setCancelable(true)
    builder.setPositiveButton(
        "OK",
        DialogInterface.OnClickListener { _, _ -> onPositiveClick() })

    builder.setNegativeButton("Edit") { _, _ ->
        onNegativeClick()
    }

    builder.show()
}