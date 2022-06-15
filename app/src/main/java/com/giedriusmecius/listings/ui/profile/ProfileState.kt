package com.giedriusmecius.listings.ui.profile

import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.Size
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.utils.state.State


data class ProfileState(
    val request: Request? = null,
    val command: Command? = null,
    val user: User? = User(),
    val userSize: Size? = null,
    val departmentName: String? = null,
    val colorName: Pair<String, String>? = null,
    val paymentMethods: List<PaymentMethod>? = null,
    val userAddresses: List<UserAddress>? = null
) :
    State<ProfileState, ProfileState.Event> {

    sealed class Event {
        object ViewCreated : Event()
        data class ReceivedUserDetails(
            val user: User
        ) : Event()

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
        data class ReceivedUserSize(val size: Size) : Event()
        object TappedColorPicker : Event()
        data class ReceivedUserColor(val color: Pair<String, String>) : Event()
        object SavedUserSize : Event()
        data class UpdatedUserAddresses(val addresses: List<UserAddress>) : Event()
        data class UpdatedUserPaymentMethods(val methods: List<PaymentMethod>) : Event()
    }

    sealed class Command {
        data class SetupUserDetails(val user: User) : Command()
        data class OpenPaymentMethodDialog(val method: PaymentMethod?, val isEdit: Boolean) :
            Command()

        data class AddPaymentMethod(val method: PaymentMethod) : Command()
        data class AddUserAddress(val address: UserAddress) : Command()
        data class OpenUserAddressDialog(val address: UserAddress?, val isEdit: Boolean) : Command()
        data class OpenSizePicker(val userSize: Size?) : Command()
        data class OpenDepartmentPicker(val departmentName: String?) : Command()
        data class UpdateDepartment(val departmentName: String) : Command()
        data class UpdateSize(val size: Size) : Command()
        data class OpenColorPicker(val colorName: String?) : Command()
        data class UpdateColor(val color: Pair<String, String>) : Command()
    }

    sealed class Request {
        object FetchUser : Request()

        data class SavePaymentMethod(
            val user: User,
            val method: PaymentMethod,
        ) : Request()

        data class UpdatePaymentMethods(
            val user: User,
            val newMethod: PaymentMethod,
            val oldMethod: PaymentMethod,
            val allMethods: List<PaymentMethod>
        ) : Request()

        data class SaveUserAddress(
            val user: User,
            val address: UserAddress,
        ) : Request()

        data class UpdateUserAddresses(
            val user: User,
            val newAddress: UserAddress,
            val oldAddress: UserAddress,
            val allAddresses: List<UserAddress>
        ) : Request()

        data class DeleteUserAddress(
            val user: User,
            val address: UserAddress,
            val allAddresses: List<UserAddress>
        ) : Request()

        data class DeletePaymentMethod(
            val user: User,
            val method: PaymentMethod,
            val allMethods: List<PaymentMethod>
        ) : Request()

        data class SaveUserSize(val user: User, val size: Size) : Request()
        data class SaveUserDepartment(val user: User, val departmentName: String) : Request()
        data class SaveUserColor(val user: User, val color: Pair<String, String>) : Request()
    }

    override fun reduce(event: Event): ProfileState {
        return when (event) {
            Event.ViewCreated -> copy(request = Request.FetchUser)
            is Event.ReceivedUserDetails -> copy(
                command = Command.SetupUserDetails(
                    event.user
                ),
                user = event.user,
                paymentMethods = event.user.paymentMethods,
                userAddresses = event.user.addresses,
                userSize = event.user.userSize,
                departmentName = event.user.mainDepartment,
                colorName = event.user.favoriteColor
            )
            is Event.TappedPaymentMethod -> copy(
                command = Command.OpenPaymentMethodDialog(
                    event.method,
                    event.isEdit
                )
            )
            is Event.AddedPaymentMethod -> copy(
                request = Request.SavePaymentMethod(
                    user ?: User(),
                    event.method,
                ), command = Command.AddPaymentMethod(event.method)
            )
            Event.SavedPaymentMethodToPrefs -> copy(request = Request.FetchUser)
            is Event.EditedPaymentMethod -> copy(
                request = Request.UpdatePaymentMethods(
                    user ?: User(),
                    event.newMethod,
                    event.oldMethod,
                    paymentMethods ?: emptyList()
                )
            )
            is Event.AddedUserAddress -> copy(
                request = Request.SaveUserAddress(
                    user ?: User(),
                    event.address,
                ), command = Command.AddUserAddress(event.address)
            )
            is Event.EditedUserAddress -> copy(
                request = Request.UpdateUserAddresses(
                    user ?: User(),
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
                    user ?: User(),
                    event.address,
                    userAddresses ?: emptyList()
                )
            )
            is Event.TappedDeletePaymentMethod -> copy(
                request = Request.DeletePaymentMethod(
                    user ?: User(),
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
                request = Request.SaveUserDepartment(user ?: User(), event.department),
                command = Command.UpdateDepartment(event.department)
            )
            is Event.ReceivedUserSize -> copy(
                request = Request.SaveUserSize(user ?: User(), event.size),
                userSize = event.size
            )
            Event.TappedColorPicker -> copy(command = Command.OpenColorPicker(colorName?.second))
            is Event.ReceivedUserColor -> copy(
                command = Command.UpdateColor(event.color),
                request = Request.SaveUserColor(user ?: User(), event.color),
                colorName = event.color
            )
            Event.SavedUserSize -> copy(command = Command.UpdateSize(userSize!!))
            is Event.UpdatedUserAddresses -> copy(userAddresses = event.addresses)
            is Event.UpdatedUserPaymentMethods -> copy(paymentMethods = event.methods)
        }
    }

    override fun clearCommandAndRequest(): ProfileState {
        return copy(request = null, command = null)
    }


}