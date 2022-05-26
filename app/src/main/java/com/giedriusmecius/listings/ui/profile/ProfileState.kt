package com.giedriusmecius.listings.ui.profile

import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.utils.state.State


data class ProfileState(
    val request: Request? = null,
    val command: Command? = null,
    val paymentMethods: List<PaymentMethod>? = null,
    val userAddresses: List<UserAddress>? = null
) :
    State<ProfileState, ProfileState.Event> {

    sealed class Event {
        object ViewCreated : Event()
        data class ReceivedPaymentMethods(
            val paymentMethods: List<PaymentMethod>,
            val userAddresses: List<UserAddress>
        ) :
            Event() // change to use when fully setup
    }

    sealed class Command {
        data class SetupUserDetails(
            val paymentMethods: List<PaymentMethod>,
            val userAddresses: List<UserAddress>
        ) : Command()
    }

    sealed class Request {
        object FetchUser : Request()
    }

    override fun reduce(event: Event): ProfileState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchUser)
            is Event.ReceivedPaymentMethods -> copy(
                command = Command.SetupUserDetails(
                    event.paymentMethods,
                    event.userAddresses,
                )
            )
        }
    }

    override fun clearCommandAndRequest(): ProfileState {
        return copy(request = null, command = null)
    }


}