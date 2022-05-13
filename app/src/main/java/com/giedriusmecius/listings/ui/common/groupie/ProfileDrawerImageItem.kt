package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import android.widget.Toast
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProfileDrawerImageBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProfileDrawerImageItem(private val img: String) : BindableItem<ItemProfileDrawerImageBinding>() {
    override fun getLayout(): Int = R.layout.item_profile_drawer_image

    override fun initializeViewBinding(view: View): ItemProfileDrawerImageBinding =
        ItemProfileDrawerImageBinding.bind(view)

    override fun bind(viewBinding: ItemProfileDrawerImageBinding, position: Int) {
        with(viewBinding) {
            profileDrawersImg.load(img)
            profileDrawersImg.setOnClickListener {
                // navigate to product page
                Toast.makeText(this.root.context, "Not Implemented", Toast.LENGTH_SHORT).show()
            }
        }

    }


}