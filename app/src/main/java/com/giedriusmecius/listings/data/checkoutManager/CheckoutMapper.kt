package com.giedriusmecius.listings.data.checkoutManager

import com.giedriusmecius.listings.data.remote.model.product.Product

class CheckoutMapper : CartItemMapper<Product, CartItem> {
    override fun mapProductToCartItem(R: Product): CartItem {
        return CartItem(
            productID = R.id, productTitle = R.title, productImg = R.image, size = null,
            quantity = 3,
            itemPrice = R.price,
            discountedPrice = null,
        )
    }

    override fun mapCartItemToProduct(T: CartItem): Product {
        TODO("Not yet implemented")
    }

}