package com.giedriusmecius.listings.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.giedriusmecius.listings.data.local.PaymentMethodResponse
import com.giedriusmecius.listings.data.local.UserAddressResponse
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
                val allMethods = req.methodList.toMutableList()
                allMethods.add(req.method)

                userPreferences.saveUserPaymentMethods(PaymentMethodResponse(methods = allMethods))
            }
            is ProfileState.Request.UpdatePaymentMethods -> {
                val allMethods = req.allMethods.toMutableList()
                allMethods.remove(req.oldMethod)
                allMethods.add(req.newMethod)
                userPreferences.saveUserPaymentMethods(PaymentMethodResponse(methods = allMethods))

                viewModelScope.launch {
                    fetchUserData()
                }
            }
            is ProfileState.Request.SaveUserAddress -> {
                val allAddresses = req.addressList.toMutableList()
                allAddresses.add(req.address)
                userPreferences.saveUserAddresses(UserAddressResponse(addresses = allAddresses))
            }
            is ProfileState.Request.UpdateUserAddresses -> {
                val allAddresses = req.allAddresses.toMutableList()
                allAddresses.remove(req.oldAddress)
                allAddresses.add(req.newAddress)
                userPreferences.saveUserAddresses(UserAddressResponse(addresses = allAddresses))

                viewModelScope.launch {
                    fetchUserData()
                }
            }
            is ProfileState.Request.DeleteUserAddress -> {
                val allAddresses = req.allAddresses.toMutableList()
                allAddresses.remove(req.address)
                userPreferences.saveUserAddresses(UserAddressResponse(addresses = allAddresses))

                viewModelScope.launch {
                    fetchUserData()
                }
            }
            is ProfileState.Request.DeletePaymentMethod -> {
                val allMethods = req.allMethods.toMutableList()
                allMethods.remove(req.method)

                userPreferences.saveUserPaymentMethods(PaymentMethodResponse(methods = allMethods))

                viewModelScope.launch {
                    fetchUserData()
                }
            }
            else -> {}
        }
    }

    private suspend fun fetchUserData() {
        val paymentMethods = userPreferences.getUserPaymentMethods().methods
        val userAddresses = userPreferences.getUserAddresses().addresses
        delay(300)
        transition(ProfileState.Event.ReceivedUserDetails(paymentMethods, userAddresses))
    }
}