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
        data class ReceivedUserDetails(
            val paymentMethods: List<PaymentMethod>,
            val userAddresses: List<UserAddress>
        ) : Event() // change to use when fully setup
        data class TappedEditPaymentMethod(val method: PaymentMethod?, val isEdit: Boolean) :
            Event()
        data class AddedPaymentMethod(val method: PaymentMethod) : Event()
        object SavedPaymentMethodToPrefs : Event()
    }

    sealed class Command {
        data class SetupUserDetails(
            val paymentMethods: List<PaymentMethod>,
            val userAddresses: List<UserAddress>
        ) : Command()

        data class OpenPaymentMethodDialog(val method: PaymentMethod?, val isEdit: Boolean) :
            Command()

        data class AddPaymentMethod(val method: PaymentMethod) : Command()
    }

    sealed class Request {
        object FetchUser : Request()
        data class SavePaymentMethod(
            val method: PaymentMethod,
            val methodList: List<PaymentMethod>
        ) : Request()
    }

    override fun reduce(event: Event): ProfileState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchUser)
            is Event.ReceivedUserDetails -> copy(
                command = Command.SetupUserDetails(
                    event.paymentMethods,
                    event.userAddresses,
                )
            )
            is Event.TappedEditPaymentMethod -> copy(
                command = Command.OpenPaymentMethodDialog(
                    event.method,
                    event.isEdit
                )
            )
            is Event.AddedPaymentMethod -> copy(
                request = Request.SavePaymentMethod(
                    event.method,
                    paymentMethods ?: mutableListOf()
                ), command = Command.AddPaymentMethod(event.method)
            )
            Event.SavedPaymentMethodToPrefs -> copy(request = Request.FetchUser)
        }
    }

    override fun clearCommandAndRequest(): ProfileState {
        return copy(request = null, command = null)
    }


}