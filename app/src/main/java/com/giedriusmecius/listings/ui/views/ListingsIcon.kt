package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
    @DrawableRes private var icon : Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsIcon, 0, 0).use {
            icon = it.getResourceId(R.styleable.ListingsIcon_android_src, -1)
        }

        toggleState(isActive)

        binding.icon.setImageResource(icon)
    }

    fun toggleState(isActive: Boolean) {
        binding.indicator.isGone = !isActive
    }
}
