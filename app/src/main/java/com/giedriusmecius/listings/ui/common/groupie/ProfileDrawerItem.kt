package com.giedriusmecius.listings.ui.common.groupie

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.databinding.ItemProfileDrawerBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem

class ProfileDrawerItem(val title: String, val imgList: List<Product>) :
    BindableItem<ItemProfileDrawerBinding>() {
    override fun getLayout(): Int = R.layout.item_profile_drawer

    private val groupie = GroupieAdapter()

    override fun initializeViewBinding(view: View): ItemProfileDrawerBinding =
        ItemProfileDrawerBinding.bind(view)

    override fun bind(viewBinding: ItemProfileDrawerBinding, position: Int) {
        with(viewBinding) {
            drawerRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                //layout params del to gravity title
                adapter = groupie
            }
            val listOfImageItems = imgList.map { ProfileDrawerImageItem(it.image) }
            groupie.add(ProfileDrawerTitleItem(title, imgList.size))
            groupie.addAll(listOfImageItems)
        }
    }
}

// drawer api filtrus
// su states filtravima ir saugojima drawer screene.