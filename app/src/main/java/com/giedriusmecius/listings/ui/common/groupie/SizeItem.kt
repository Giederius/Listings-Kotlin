package com.giedriusmecius.listings.ui.common.groupie

import android.graphics.Color
import android.view.View
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemSizeBinding
import com.xwray.groupie.viewbinding.BindableItem

class SizeItem(
    val us: Int,
    val eu: String,
    var isSelected: Boolean,
    val onClick: (Int) -> Unit
) :
    BindableItem<ItemSizeBinding>() {
    override fun getLayout(): Int = R.layout.item_size

    override fun initializeViewBinding(view: View): ItemSizeBinding = ItemSizeBinding.bind(view)

    override fun bind(viewBinding: ItemSizeBinding, position: Int) {
        with(viewBinding) {

            usSize.text = us.toString()
            euSize.text = eu

            if (isSelected) {
                root.setBackgroundColor(Color.parseColor("#D2CEF6"))
            } else {
                root.setBackgroundColor(Color.TRANSPARENT)
            }
            selectedIcon.isGone = !isSelected

            root.setOnClickListener {
                onClick(us)
            }
        }
    }
}