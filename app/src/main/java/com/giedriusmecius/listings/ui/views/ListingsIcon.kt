package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ViewListingsIconBinding

class ListingsIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = ViewListingsIconBinding.inflate(LayoutInflater.from(context), this)
    private var isActive = false
    private var hasBorder = false

    @DrawableRes
    private var icon: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsIcon, 0, 0).use {
            icon = it.getResourceId(R.styleable.ListingsIcon_android_src, -1)
            hasBorder = it.getBoolean(R.styleable.ListingsIcon_hasBorder, false)
        }

        toggleState(isActive)
        toggleBorder(hasBorder)

        binding.icon.setImageResource(icon)
    }

    private fun toggleBorder(hasBorder: Boolean) {
        if (hasBorder) {
            binding.iconContainer.background =
                ContextCompat.getDrawable(context, R.drawable.bg_small_selector_48x48_bordered)
        }
    }

    fun toggleState(isActive: Boolean) {
        binding.indicator.isGone = !isActive
    }
}
