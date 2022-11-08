package com.giedriusmecius.listings.ui.checkout

import androidx.lifecycle.MutableLiveData
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) :
    BaseViewModel<CheckoutState, CheckoutState.Event>(CheckoutState()) {

    private val fetchedAddresses = MutableLiveData<List<UserAddress>>()
    val userAddresses: MutableLiveData<List<UserAddress>>
        get() = fetchedAddresses

    override fun handleState(newState: CheckoutState) {
        return when (val req = newState.request) {
            is CheckoutState.Request.FetchData -> {
                fetchedAddresses.value = userPreferences.getUser().addresses.toList()
            }
            else -> {}
        }
    }
}