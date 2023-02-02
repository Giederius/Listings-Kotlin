package com.giedriusmecius.listings.data.checkoutManager

class CheckoutManager {
    var orderId: Int? = null
    var cartItems: MutableList<CartItem> = mutableListOf()
    var cartSize = cartItems.size

    fun resetCart() {
        orderId = null
        cartItems = mutableListOf()
    }

    fun addToCart(item: CartItem) {
        if (!lookForDuplicates(item)) {
            cartItems.add(item)
        }
    }

    fun removeFromCart(item: CartItem) {
        cartItems.remove(item)
    }

    fun getTotalCartCost(): Float {
        var cost = 0.0F
        cartItems.forEach { cost.plus(it.getTotalPrice()) }
        return cost
    }

    fun lookForDuplicates(item: CartItem): Boolean {
        val wasFound = cartItems.find { it == item }
        if (wasFound != null) {
            cartItems.find { it == wasFound }?.let { foundItem ->
                foundItem.copy(quantity = foundItem.quantity.plus(item.quantity))
                    .also { duplicatedItem ->
                        cartItems.remove(foundItem)
                        cartItems.add(duplicatedItem)
                        return true
                    }
            }
        }
        return false
    }
}