package com.giedriusmecius.listings.ui.product

import com.giedriusmecius.listings.utils.state.State

data class ProductState(val request: Request? = null, var command: Command? = null) :
    State<ProductState, ProductState.Event> {
    sealed class Event {

    }

    sealed class Command {

    }

    sealed class Request {

    }

    override fun reduce(event: Event): ProductState {
        TODO("Not yet implemented")
    }

    override fun clearCommandAndRequest(): ProductState {
        return copy(command = null, request = null)
    }

}