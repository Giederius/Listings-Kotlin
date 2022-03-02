package com.giedriusmecius.listings.ui.home.items

import android.media.Image
import android.view.View
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemHomeCategoryBinding
import com.xwray.groupie.viewbinding.BindableItem

class CategoryItem(val icon: Image, val title: String, val onClick: () -> Unit) :
    BindableItem<ItemHomeCategoryBinding>() {
    override fun bind(viewBinding: ItemHomeCategoryBinding, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getLayout(): Int = R.layout.item_home_category

    override fun initializeViewBinding(view: View): ItemHomeCategoryBinding {
        TODO("Not yet implemented")
    }
}