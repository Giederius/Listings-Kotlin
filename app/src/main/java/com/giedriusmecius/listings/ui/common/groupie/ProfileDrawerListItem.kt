package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProfileDrawerListBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProfileDrawerListItem(val title: String, val count: Int, val img: String) :
    BindableItem<ItemProfileDrawerListBinding>() {

    override fun getLayout(): Int = R.layout.item_profile_drawer_list

    override fun initializeViewBinding(view: View): ItemProfileDrawerListBinding =
        ItemProfileDrawerListBinding.bind(view)

    override fun bind(viewBinding: ItemProfileDrawerListBinding, position: Int) {
        with(viewBinding) {
            listLayoutTitle.text = title
            listLayoutDescription.text = "$count products"
            listItemImage.load(img)
        }
    }


}