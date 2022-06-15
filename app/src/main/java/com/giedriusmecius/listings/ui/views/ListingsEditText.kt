package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ViewListingsEditTextBinding

class ListingsEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = ViewListingsEditTextBinding.inflate(LayoutInflater.from(context), this)
    var labelText = ""
    var errorText = ""
    var nextFocusDown = 0

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsEditText, 0, 0).use {
            labelText = it.getString(R.styleable.ListingsEditText_labelText).toString()
            errorText = it.getString(R.styleable.ListingsEditText_errorText).toString()

        }

        with(binding) {
            editTextLabel.text = labelText
            if (editText.isFocused) {
                changeState(EditTextState.FOCUS)
            } else {
                changeState(EditTextState.ACTIVE)
            }
        }
    }

    fun getEditText(): EditText = binding.editText

    fun setText(value: String) {
        binding.editText.setText(value)
    }

    fun focusItem() {
        binding.editText.requestFocus()
    }

    fun setOnEnterKeyListener(callback: () -> Unit) {
        binding.editText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_DOWN) {
                Log.d("MANO", "inside")
                callback()
                true
            } else {
                false
            }
        }
    }

    fun changeState(state: EditTextState) {
        with(binding) {
            editTextLabel.setTextColor(state.accentColor)
            editText.apply {
                backgroundTintList = ContextCompat.getColorStateList(context, state.accentColor)
                setTextColor(state.textColor)
                isEnabled = state != EditTextState.DISABLED
            }

            if (state == EditTextState.ERROR) {
                editTextErrorIcon.isGone = false
                errorMessage.apply {
                    isGone = false
                    setTextColor(state.accentColor)
                }
            }
        }
    }

    enum class EditTextState(val accentColor: Int, val textColor: Int, val displayError: Boolean) {
        DEFAULT(R.color.neutralLight, R.color.neutralLight, false),
        FOCUS(R.color.accentPurple, R.color.neutralLight, false),
        ACTIVE(R.color.darkTextColor, R.color.darkTextColor, false),
        DISABLED(R.color.disabledGrey, R.color.disabledGrey, false),
        ERROR(R.color.highlightRed, R.color.darkTextColor, true)
    }
}