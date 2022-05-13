package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.ItemProfileDrawerGridBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem

class ProfileDrawerGridItem(val title: String, val productList: List<Product>) :
    BindableItem<ItemProfileDrawerGridBinding>() {
    private val groupie = GroupieAdapter()

    override fun bind(viewBinding: ItemProfileDrawerGridBinding, position: Int) {
        with(viewBinding) {
            gridLayoutRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = groupie
            }

            gridLayoutTitle.text = title
            gridLayoutDescription.text = "${productList.size} products"
            val listOfImageItems = productList.map { ProfileDrawerImageItem(it.image) }
            groupie.addAll(listOfImageItems)
        }
    }

    override fun getLayout(): Int = R.layout.item_profile_drawer_grid

    override fun initializeViewBinding(view: View): ItemProfileDrawerGridBinding =
        ItemProfileDrawerGridBinding.bind(view)
}