package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.blue
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ViewListingButtonBinding

class ListingsButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = ViewListingButtonBinding.inflate(LayoutInflater.from(context), this)
    private var text = ""
    private var icon: Drawable? = null
    private var padding: Int = 0

    init {
        var isText = false

        context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsButton, 0, 0).use {
            isText = it.getBoolean(R.styleable.ListingsButton_isText, false)
            text = it.getString(R.styleable.ListingsButton_android_text).toString()
            icon = it.getDrawable(R.styleable.ListingsButton_android_drawable)
            padding = it.getInt(R.styleable.ListingsButton_android_paddingHorizontal, 0)
        }

        isClickable = true
        with(binding) {
            when (isText) {
                true -> {
                    Log.d("MANO", text)
                    buttonImage.isGone = true
                    buttonTitle.isGone = false
                    setText(text)
                    buttonContainer.setPadding(0, padding, padding, 0)
                }
                else -> {
                    buttonImage.isGone = false
                    buttonTitle.isGone = true
                    if (icon != null) {
                        buttonImage.setImageDrawable(icon)
                        setIconTint(R.color.darkTextColor)
                    }
                }
            }
        }
    }

    fun setText(text: String) {
        binding.buttonTitle.text = text
    }

    fun setIconTint(color: Int) {
        binding.buttonImage.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }

}

fun ListingsButton.setupButton(text: String, iconColor: Int) {
    setText(text)
    setIconTint(iconColor)
}

// susitvarkyti su paddingais
// issiaiskinti dydzius burbuliuko
// perpanaudot ant edit / add card
// marginus susitvarkyti prie fragment profile edit add card
//