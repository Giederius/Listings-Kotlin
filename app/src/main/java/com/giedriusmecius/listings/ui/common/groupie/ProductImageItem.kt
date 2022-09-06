package com.giedriusmecius.listings.ui.common.groupie

import android.view.View
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProductImageBinding
import com.xwray.groupie.viewbinding.BindableItem

class ProductImageItem(val img: String, val isFirst: Boolean) :
    BindableItem<ItemProductImageBinding>() {
    override fun getLayout(): Int = R.layout.item_product_image

    override fun initializeViewBinding(view: View): ItemProductImageBinding =
        ItemProductImageBinding.bind(view)

    override fun bind(viewBinding: ItemProductImageBinding, position: Int) {
        with(viewBinding) {
            image.load(img)
            if (!isFirst) {
                val model = image.shapeAppearanceModel.toBuilder().setAllCornerSizes(0F).build()
                image.shapeAppearanceModel = model
            }
        }
    }
}