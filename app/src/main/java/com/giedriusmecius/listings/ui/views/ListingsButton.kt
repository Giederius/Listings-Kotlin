package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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
    private var isText: Boolean
    private var isColorBtn: Boolean

    init {

        context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsButton, 0, 0).use {
            isText = it.getBoolean(R.styleable.ListingsButton_isText, false)
            isColorBtn = it.getBoolean(R.styleable.ListingsButton_isText, false)
            text = it.getString(R.styleable.ListingsButton_android_text).toString()
            icon = it.getDrawable(R.styleable.ListingsButton_android_drawable)
            padding = it.getInt(R.styleable.ListingsButton_android_paddingHorizontal, 0)
        }

        isClickable = true
        with(binding) {
            when {
                isText -> {
                    buttonImage.isGone = isText
                    buttonTitle.isGone = !isText
                    setText(text)
                    buttonContainer.setPadding(0, padding, padding, 0)
                }
                !isText -> {
                    buttonImage.isGone = isText
                    buttonTitle.isGone = !isText
                    if (icon != null) {
                        buttonImage.setImageDrawable(icon)
                        setIconTint(R.color.darkTextColor)
                    }
                }
                isColorBtn -> {
                    buttonImage.isGone = false
                    buttonTitle.isGone = false
                }
            }
        }
    }

    fun setText(text: String) {
        binding.buttonTitle.text = text
    }

    fun setIconTint(color: Int) {
        binding.buttonImage.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }

    fun setIconTintWithString(color: String) {
        ColorStateList.valueOf(Color.parseColor(color)).let {
            binding.buttonImage.imageTintList = it
        }
    }

    fun setColorBtn(color: String, name: String) {
        binding.apply {
            ColorStateList.valueOf(Color.parseColor(color)).let {
                buttonImage.imageTintList = it
            }
            buttonTitle.apply {
                isGone = false
                text = name
            }
        }
    }

    fun isEnabled(isEnabled: Boolean) {
        binding.apply {
            buttonContainer.isEnabled = isEnabled
            buttonTitle.setTextColor(R.color.disabledButtonText)
            isClickable = false
        }
    }

    fun setupCTAButton() {
        binding.apply {
            buttonContainer.background =
                ContextCompat.getDrawable(context, R.drawable.listings_button_cta_selector)
            buttonTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}

fun ListingsButton.setupButton(text: String, iconColor: Int) {
    setText(text)
    setIconTint(iconColor)
}