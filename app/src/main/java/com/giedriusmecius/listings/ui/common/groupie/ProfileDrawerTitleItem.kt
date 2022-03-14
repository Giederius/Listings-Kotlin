package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProfileDrawerTitleBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProfileDrawerTitleItem(val title: String, val count: Int) :
    BindableItem<ItemProfileDrawerTitleBinding>() {
    override fun getLayout(): Int = R.layout.item_profile_drawer_title

    override fun initializeViewBinding(view: View): ItemProfileDrawerTitleBinding =
        ItemProfileDrawerTitleBinding.bind(view)

    override fun bind(viewBinding: ItemProfileDrawerTitleBinding, position: Int) {
        with(viewBinding) {
            profileDrawersTitle.text = title
            profileDrawersDescription.text = "$count products"
        }
    }


}