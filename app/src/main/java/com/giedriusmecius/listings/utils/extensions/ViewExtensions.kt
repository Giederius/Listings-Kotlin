package com.giedriusmecius.listings.utils.extensions

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone

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

fun showToast(context: Context, title: String) {
    Toast.makeText(
        context,
        title,
        Toast.LENGTH_SHORT
    ).show()
}

fun View.updateMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val params = this.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(left, top, right, bottom)
    this.layoutParams = params
}