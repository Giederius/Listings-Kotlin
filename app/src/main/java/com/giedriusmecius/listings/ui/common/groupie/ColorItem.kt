package com.giedriusmecius.listings.ui.common.groupie

import android.graphics.Color
import android.view.View
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemColorBinding
import com.xwray.groupie.viewbinding.BindableItem

class ColorItem(
    val color: String,
    val colorName: String,
    var isSelected: Boolean,
    val onClick: (String) -> Unit
) :
    BindableItem<ItemColorBinding>() {


    override fun getLayout(): Int = R.layout.item_color

    override fun initializeViewBinding(view: View): ItemColorBinding = ItemColorBinding.bind(view)

    override fun bind(viewBinding: ItemColorBinding, position: Int) {
        with(viewBinding) {

            colorContainer.setCardBackgroundColor(Color.parseColor(color))
            colorNameView.text = colorName

            if (isSelected) {
                root.setBackgroundColor((Color.parseColor("#D2CEF6")))
            } else {
                root.setBackgroundColor(Color.TRANSPARENT)
            }

            selectedIcon.isGone = !isSelected

            root.setOnClickListener {
                onClick(colorName)
            }
        }
    }
}