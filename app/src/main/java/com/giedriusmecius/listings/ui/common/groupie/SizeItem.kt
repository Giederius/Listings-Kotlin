package com.giedriusmecius.listings.ui.common.groupie

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.view.isGone
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemSizeBinding
import com.xwray.groupie.viewbinding.BindableItem

class SizeItem(
    private val us: Int,
    private val eu: String,
    private var isSelected: Boolean,
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
            }
            selectedIcon.isGone = !isSelected

            root.setOnClickListener {
                Log.d("MANO", "cliek")
//                isSelected = !isSelected
            }
        }
    }
}