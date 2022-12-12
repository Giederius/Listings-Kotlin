package com.giedriusmecius.listings.ui.checkout

import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.utils.state.State

data class CheckoutState(val command: Command? = null, val request: Request? = null) :
    State<CheckoutState, CheckoutState.Event> {
    sealed class Event {
        object ViewCreated : Event()
        data class TappedAddressEdit(val address: UserAddress, val isEdit: Boolean = true) : Event()
        data class ReceivedAddressEdit(val newAddress: UserAddress, val oldAddress: UserAddress) :
            Event()
        data class TappedAddNewAddress(val address: UserAddress, val isEdit: Boolean = false) :
            Event()
        data class ReceivedNewUserAddress(val newAddress: UserAddress) : Event()
        data class TappedChangeAddress(val selectedAddress: UserAddress) : Event()
        object TappedAddPaymentMethod : Event()
        data class AddedPaymentMethod(val paymentMethod: PaymentMethod) : Event()
    }

    sealed class Command {
        data class OpenAddressDialog(val address: UserAddress, val isEdit: Boolean) : Command()
        data class HandleAddressChange(val selectedAddress: UserAddress) : Command()
        object OpenAddPaymentMethod : Command()
    }

    sealed class Request {
        object FetchData : Request()
        data class HandleAddressEdit(val newAddress: UserAddress, val oldAddress: UserAddress) :
            Request()

        data class HandleNewAddress(val newAddress: UserAddress) : Request()
        data class HandleNewPaymentMethod(val paymentMethod: PaymentMethod) : Request()
    }

    override fun reduce(event: Event): CheckoutState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchData)
            is Event.TappedAddressEdit -> copy(
                command = Command.OpenAddressDialog(event.address, event.isEdit)
            )
            is Event.TappedAddNewAddress -> copy(
                command = Command.OpenAddressDialog(
                    event.address,
                    event.isEdit
                )
            )
            is Event.ReceivedAddressEdit -> copy(
                request = Request.HandleAddressEdit(
                    event.newAddress,
                    event.oldAddress
                )
            )
            is Event.ReceivedNewUserAddress -> copy(request = Request.HandleNewAddress(event.newAddress))
            is Event.TappedChangeAddress -> copy(command = Command.HandleAddressChange(event.selectedAddress))
            Event.TappedAddPaymentMethod -> copy(command = Command.OpenAddPaymentMethod)
            is Event.AddedPaymentMethod -> copy(request = Request.HandleNewPaymentMethod(event.paymentMethod))
            else -> copy()
        }
    }

    override fun clearCommandAndRequest(): CheckoutState {
        return copy(command = null, request = null)
    }

}