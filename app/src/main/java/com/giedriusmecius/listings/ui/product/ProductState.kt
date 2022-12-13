package com.giedriusmecius.listings.ui.product

import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.state.State

data class ProductState(val request: Request? = null, var command: Command? = null) :
    State<ProductState, ProductState.Event> {
    sealed class Event {
        data class ViewCreated(val productId: Int) : Event()
        data class ReceivedProduct(val product: Product) : Event()
    }

    sealed class Command {
        data class DisplayProduct(val product: Product) : Command()
    }

    sealed class Request {
        data class FetchProduct(val productId: Int) : Request()
        object FetchData : Request()
    }

    override fun reduce(event: Event): ProductState {
        return when (event) {
            is Event.ViewCreated -> copy(request = Request.FetchProduct(event.productId))
            is Event.ReceivedProduct -> copy(command = Command.DisplayProduct(event.product))
        }
    }

    override fun clearCommandAndRequest(): ProductState {
        return copy(command = null, request = null)
    }

}