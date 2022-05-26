package com.giedriusmecius.listings.utils.extensions

import android.app.AlertDialog
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import com.giedriusmecius.listings.R

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

fun showAlertDialog(
    context: Context,
    title: String = "",
    message: String = "",
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    AlertDialog.Builder(context, R.style.MyDialogTheme)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(true)
        .setPositiveButton("OK") { _, _ -> onPositiveClick() }
        .setNegativeButton("Edit") { _, _ -> onNegativeClick() }
        .show()
}

fun View.animateLeave() {
    this.apply {
        alpha = 1F
        animate()
            .translationY(-50F)
            .alpha(0F)
            .setDuration(400)
            .setListener(null)
            .withEndAction { isGone = true }
    }
}

fun View.animateShowUp() {
    this.apply {
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
}

fun showToast(context: Context, title: String, ) {
    Toast.makeText(
        context,
        title,
        Toast.LENGTH_SHORT
    ).show()
}