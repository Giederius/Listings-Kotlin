package com.giedriusmecius.listings.utils.extensions

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.giedriusmecius.listings.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Context.showDialog(
    title: String,
    message: String = "",
    positiveBtnTitle: String,
    negativeBtnTitle: String,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    MaterialAlertDialogBuilder(this).apply {
        AppCompatResources.getDrawable(this@showDialog, R.drawable.bg_rounded_rect_sm_borderless)
            ?.let {
                setBackground(it)
            }
        if (message != "") {
            setTitle(title)
            setMessage(message)
        } else {
            setMessage(title)
        }
        setPositiveButton(positiveBtnTitle) { dialog, which ->
            onPositiveClick()
        }
        setNegativeButton(negativeBtnTitle) { dialog, which ->
            onNegativeClick()
        }
    }.show()
}
