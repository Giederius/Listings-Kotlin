package com.giedriusmecius.listings.data.checkoutManager

import android.util.Log

class CheckoutManager {
    var orderId: Int? = null
    var cartItems: MutableList<CartItem> = mutableListOf()

    init {
        Log.d("MANOCHECKINIT", "FUCKING STTART")
    }

    fun resetCart() {
        orderId = null
        cartItems = mutableListOf()
    }

    fun addToCart(item: CartItem): Boolean {
//        return if (!lookForDuplicates(item)) {
        try {
            cartItems.add(item)
            Log.d("MANOCHECKOUTMNG2", "${getCartSize()} $cartItems")
            return true
        } catch (e: Exception) {
            Log.d("MANOCHECKOUTMNG2", "${e.message}")
            return false
        }

//        } else {
//            true
//        }
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
        Log.d("MANOCHECKOUTMNG", "${wasFound?.productTitle}")
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

    fun getCartSize(): Int {
        return cartItems.size
    }
}