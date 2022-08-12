package com.giedriusmecius.listings.ui.common.groupie

import android.graphics.Paint
import android.view.View
import androidx.core.view.isGone
import coil.load
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.ItemProductBinding
import com.giedriusmecius.listings.utils.extensions.toCurrency
import com.xwray.groupie.viewbinding.BindableItem

class ProductItem(
    val title: String,
    val image: String,
    val price: Float,
    private val discountedPrice: Float? = null,
    val onClick: () -> Unit
) : BindableItem<ItemProductBinding>() {
    override fun getLayout(): Int = R.layout.item_product

    override fun initializeViewBinding(view: View): ItemProductBinding =
        ItemProductBinding.bind(view)

    override fun bind(viewBinding: ItemProductBinding, position: Int) {
        viewBinding.apply {
            if (discountedPrice != null) {
                productPrice.setTextAppearance(R.style.strikethroughPrice)
                productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                productPriceDiscounted.apply {
                    isGone = false
                    text = discountedPrice.toCurrency()
                }
            }

            productTitle.text = title
            productPrice.text = price.toCurrency()
            productImg.load(image)
            root.setOnClickListener {
                onClick()
            }

        }
    }
}