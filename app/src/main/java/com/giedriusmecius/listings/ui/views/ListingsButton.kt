package com.giedriusmecius.listings.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ViewListingButtonBinding

class ListingsButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = ViewListingButtonBinding.inflate(

        LayoutInflater.from(context), this, false
    )

    init {

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ListingsButton,0,0)

        with(binding) {
            buttonImage.isGone = true
            buttonTitle.isGone = false
            buttonTitle.text = a.getText(0)
        }
    }
}

// susitvarkyti su paddingais
// issiaiskinti dydzius burbuliuko
// perpanaudot ant edit / add card
// marginus susitvarkyti prie fragment profile edit add card
//