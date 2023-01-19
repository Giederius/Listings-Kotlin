package com.giedriusmecius.listings.ui.cart

import com.giedriusmecius.listings.data.remote.model.product.InCartProduct
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State


data class CartState(
    val command: Command? = null,
    val request: Request? = null,
    val cartItems: List<Product> = emptyList()
) :
    State<CartState, CartState.Event> {
    sealed class Event {
        object ViewCreated : Event()

        //        data class ReceivedProducts(val list: List<Product>) : Event()
        data class DeletedProduct(val item: Product) : Event()
        data class TappedCheckout(val price: Float) : Event()
    }

    sealed class Command {
        data class StartCheckout(val cartItems: List<Product>, val price: Float) : Command()
    }

    sealed class Request {
        object FetchData : Request()
        data class DeleteProductFromCart(val item: Product) : Request()
    }

    override fun reduce(event: Event): CartState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchData)
            is Event.DeletedProduct -> copy(request = Request.DeleteProductFromCart(event.item))
            is Event.TappedCheckout -> copy(command = Command.StartCheckout(cartItems, event.price))
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): CartState {
        return copy(command = null, request = null)
    }
}