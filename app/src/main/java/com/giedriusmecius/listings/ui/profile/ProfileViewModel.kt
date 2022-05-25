package com.giedriusmecius.listings.ui.profile

import android.util.Log
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) :
    BaseViewModel<ProfileState, ProfileState.Event>(ProfileState()) {
    override fun handleState(newState: ProfileState) {
        when (newState.request) {
            ProfileState.Request.FetchUser -> {
                val paymentMethods = userPreferences.getUserPaymentMethods().methods
                val userAddresses = userPreferences.getUserAddresses().addresses
                Log.d("MANO", paymentMethods.toString())
                ProfileState.Event.ReceivedPaymentMethods(paymentMethods, userAddresses)
            }
            else -> {}
        }
    }
}