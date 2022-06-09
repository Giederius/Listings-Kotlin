package com.giedriusmecius.listings.ui.profile

import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.Size
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.utils.state.State


data class ProfileState(
    val request: Request? = null,
    val command: Command? = null,
    val user: User? = null,
    val userSize: Size? = null,
    val departmentName: String? = null,
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

        data class TappedPaymentMethod(val method: PaymentMethod?, val isEdit: Boolean) :
            Event()

        data class AddedPaymentMethod(val method: PaymentMethod) : Event()
        data class EditedPaymentMethod(
            val newMethod: PaymentMethod,
            val oldMethod: PaymentMethod
        ) : Event()

        data class AddedUserAddress(val address: UserAddress) : Event()
        data class EditedUserAddress(
            val newAddress: UserAddress,
            val oldAddress: UserAddress
        ) : Event()

        object SavedPaymentMethodToPrefs : Event()
        data class TappedUserAddress(val address: UserAddress?, val isEdit: Boolean) : Event()
        data class TappedDeleteAddress(val address: UserAddress) : Event()
        data class TappedDeletePaymentMethod(val method: PaymentMethod) : Event()
        object TappedSizePicker : Event()
        object TappedDepartmentPicker : Event()
        data class ReceivedDepartment(val department: String) : Event()
    }

    sealed class Command {
        data class SetupUserDetails(
            val paymentMethods: List<PaymentMethod>,
            val userAddresses: List<UserAddress>
        ) : Command()

        data class OpenPaymentMethodDialog(val method: PaymentMethod?, val isEdit: Boolean) :
            Command()

        data class AddPaymentMethod(val method: PaymentMethod) : Command()
        data class AddUserAddress(val address: UserAddress) : Command()
        data class OpenUserAddressDialog(val address: UserAddress?, val isEdit: Boolean) : Command()
        data class OpenSizePicker(val userSize: Size?) : Command()
        data class OpenDepartmentPicker(val departmentName: String?) : Command()
        data class UpdateDepartment(val departmentName: String) : Command()
    }

    sealed class Request {
        object FetchUser : Request()

        data class SavePaymentMethod(
            val method: PaymentMethod,
            val methodList: List<PaymentMethod>
        ) : Request()

        data class UpdatePaymentMethods(
            val newMethod: PaymentMethod,
            val oldMethod: PaymentMethod,
            val allMethods: List<PaymentMethod>
        ) : Request()

        data class SaveUserAddress(val address: UserAddress, val addressList: List<UserAddress>) :
            Request()

        data class UpdateUserAddresses(
            val newAddress: UserAddress,
            val oldAddress: UserAddress,
            val allAddresses: List<UserAddress>
        ) : Request()

        data class DeleteUserAddress(
            val address: UserAddress,
            val allAddresses: List<UserAddress>
        ) : Request()

        data class DeletePaymentMethod(
            val method: PaymentMethod,
            val allMethods: List<PaymentMethod>
        ) : Request()
    }

    override fun reduce(event: Event): ProfileState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchUser)
            is Event.ReceivedUserDetails -> copy(
                command = Command.SetupUserDetails(
                    event.paymentMethods,
                    event.userAddresses,
                ),
                paymentMethods = event.paymentMethods,
                userAddresses = event.userAddresses,
                userSize = Size(26, "s", true),
                departmentName = "Girl"
            )
            is Event.TappedPaymentMethod -> copy(
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
            is Event.EditedPaymentMethod -> copy(
                request = Request.UpdatePaymentMethods(
                    event.newMethod,
                    event.oldMethod,
                    paymentMethods ?: emptyList()
                )
            )
            is Event.AddedUserAddress -> copy(
                request = Request.SaveUserAddress(
                    event.address,
                    userAddresses ?: mutableListOf()
                ), command = Command.AddUserAddress(event.address)
            )
            is Event.EditedUserAddress -> copy(
                request = Request.UpdateUserAddresses(
                    event.newAddress,
                    event.oldAddress,
                    userAddresses ?: emptyList()
                )
            )
            is Event.TappedUserAddress -> copy(
                command = Command.OpenUserAddressDialog(event.address, event.isEdit)
            )
            is Event.TappedDeleteAddress -> copy(
                request = Request.DeleteUserAddress(
                    event.address,
                    userAddresses ?: emptyList()
                )
            )
            is Event.TappedDeletePaymentMethod -> copy(
                request = Request.DeletePaymentMethod(
                    event.method,
                    paymentMethods ?: emptyList()
                )
            )
            Event.TappedSizePicker -> copy(command = Command.OpenSizePicker(userSize))
            Event.TappedDepartmentPicker -> copy(
                command = Command.OpenDepartmentPicker(
                    departmentName = departmentName
                )
            )
            is Event.ReceivedDepartment -> copy(
                departmentName = event.department,
                command = Command.UpdateDepartment(event.department)
            )
        }
    }

    override fun clearCommandAndRequest(): ProfileState {
        return copy(request = null, command = null)
    }


}