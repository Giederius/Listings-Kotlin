package com.giedriusmecius.listings.data.checkoutManager

import com.giedriusmecius.listings.data.local.Size

data class CartItem(
    val productID: Int,
    val productTitle: String,
    val productImg: String,
    val size: Size?,
    val quantity: Int,
    val itemPrice: Float,
    val discountedPrice: Float? = null,
) {
    fun getTotalPrice(): Float {
        return quantity.times(itemPrice)
    }
}