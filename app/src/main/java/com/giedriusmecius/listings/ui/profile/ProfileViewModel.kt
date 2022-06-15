package com.giedriusmecius.listings.ui.profile

import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) :
    BaseViewModel<ProfileState, ProfileState.Event>(ProfileState()) {
    override fun handleState(newState: ProfileState) {
        when (val req = newState.request) {
            ProfileState.Request.FetchUser -> {
                viewModelScope.launch {
                    fetchUserData()
                }
            }
            is ProfileState.Request.SavePaymentMethod -> {
                saveItems(req.user, req.user.paymentMethods, req.method, false)
            }

            is ProfileState.Request.SaveUserAddress -> {
                saveItems(req.user, req.user.addresses, req.address, true)
            }

            is ProfileState.Request.SaveUserSize -> {
                val updatedUser = req.user.copy(userSize = req.size)
                userPreferences.saveUser(updatedUser)
                // figure out why this works only with this.
                transition(ProfileState.Event.SavedUserSize)
            }

            is ProfileState.Request.SaveUserDepartment -> {
                val updatedUser = req.user.copy(mainDepartment = req.departmentName)
                userPreferences.saveUser(updatedUser)
            }

            is ProfileState.Request.SaveUserColor -> {
                val updatedUser = req.user.copy(favoriteColor = req.color)
                userPreferences.saveUser(updatedUser)
            }

            is ProfileState.Request.UpdatePaymentMethods -> {
                updateItems(req.user, req.allMethods, req.newMethod, req.oldMethod, false)
            }

            is ProfileState.Request.UpdateUserAddresses -> {
                updateItems(req.user, req.allAddresses, req.newAddress, req.oldAddress, true)
            }

            is ProfileState.Request.DeleteUserAddress -> {
                deleteItem(req.user, req.allAddresses, req.address, true)
            }

            is ProfileState.Request.DeletePaymentMethod -> {
                deleteItem(req.user, req.allMethods, req.method, false)
            }
            else -> {}
        }
    }

    private suspend fun fetchUserData() {
        val user = userPreferences.getUser()
        delay(500)
        transition(ProfileState.Event.ReceivedUserDetails(user))
    }

    private fun saveItems(user: User, all: List<Any>, new: Any, isAddress: Boolean) {
        val newList = all.toMutableList()
        newList.add(new)
        if (isAddress) {
            val updatedList = newList.map { it as UserAddress }
            val updatedUser = user.copy(addresses = updatedList)
            userPreferences.saveUser(updatedUser)
            transition(ProfileState.Event.UpdatedUserAddresses(updatedList))
        } else {
            val updatedList = newList.map { it as PaymentMethod }
            val updatedUser = user.copy(paymentMethods = updatedList)
            userPreferences.saveUser(updatedUser)
            transition(ProfileState.Event.UpdatedUserPaymentMethods(updatedList))
        }
    }

    private fun updateItems(user: User, all: List<Any>, new: Any, old: Any, isAddress: Boolean) {
        val newList = all.toMutableList()
        newList.remove(old)
        newList.add(new)

        saveAddressesOrPaymentMethodsToSharedPrefs(user, isAddress, newList)
    }

    private fun deleteItem(user: User, all: List<Any>, old: Any, isAddress: Boolean) {
        val newList = all.toMutableList()
        newList.remove(old)

        saveAddressesOrPaymentMethodsToSharedPrefs(user, isAddress, newList)
    }

    private fun saveAddressesOrPaymentMethodsToSharedPrefs(
        user: User,
        isAddress: Boolean,
        listToSave: List<Any>
    ) {
        if (isAddress) {
            val updatedUser = user.copy(addresses = listToSave.map { it as UserAddress })
            userPreferences.saveUser(updatedUser)
        } else {
            val updatedUser = user.copy(paymentMethods = listToSave.map { it as PaymentMethod })
            userPreferences.saveUser(updatedUser)
        }

        viewModelScope.launch {
            fetchUserData()
        }
    }
}