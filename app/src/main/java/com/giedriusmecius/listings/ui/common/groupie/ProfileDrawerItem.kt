package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProfileDrawerBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem

class ProfileDrawerItem(val title: String, val count: Int, val img: Int) :
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
            groupie.add(ProfileDrawerTitleItem(title, count))
            groupie.addAll(
                arrayListOf(
                    ProfileDrawerImageItem(img),
                    ProfileDrawerImageItem(img),
                    ProfileDrawerImageItem(img),
                    ProfileDrawerImageItem(img)
                )
            )
        }
    }
}

// drawer api filtrus
// su states filtravima ir saugojima drawer screene.