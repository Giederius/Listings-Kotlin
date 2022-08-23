package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemStoreBinding
import com.xwray.groupie.viewbinding.BindableItem

class StoreItem : BindableItem<ItemStoreBinding>() {

    override fun getLayout(): Int = R.layout.item_store

    override fun initializeViewBinding(view: View): ItemStoreBinding = ItemStoreBinding.bind(view)
    override fun bind(viewBinding: ItemStoreBinding, position: Int) {
        with(viewBinding) {

        }
    }
}