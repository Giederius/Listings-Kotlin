package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ViewListingsSizeRadioButtonBinding

class ListingsSizeRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatRadioButton(context, attrs) {
//    val binding = ViewListingsSizeRadioButtonBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsSizeRadioButton, 0, 0).use {
            it.getBoolean(R.styleable.ListingsSizeRadioButton_android_checked, false)
        }

    }
}