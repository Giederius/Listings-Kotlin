package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemStoreFollowBinding
import com.xwray.groupie.viewbinding.BindableItem

data class StoreFollowItem(
    val storeTitle: String,
    val storeTag: String,
    var isFollowing: Boolean
) :
    BindableItem<ItemStoreFollowBinding>() {
    override fun getLayout(): Int = R.layout.item_store_follow

    override fun initializeViewBinding(view: View): ItemStoreFollowBinding =
        ItemStoreFollowBinding.bind(view)

    override fun bind(viewBinding: ItemStoreFollowBinding, position: Int) {
        with(viewBinding) {
            storeName.text = storeTitle
            storeDescription.text = storeTag
            followButton.setOnClickListener {
                isFollowing = !isFollowing
            }
            followButton.apply {
                text = if (isFollowing) "Unfollow" else "Follow"
            }
        }
    }
}