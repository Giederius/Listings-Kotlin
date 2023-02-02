package com.giedriusmecius.listings.data.checkoutManager

interface CartItemMapper<R, T> {
    fun mapProductToCartItem(R: R): T
    fun mapCartItemToProduct(T: T): R
}