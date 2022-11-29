package com.giedriusmecius.listings.ui.checkout

import androidx.lifecycle.MutableLiveData
import com.giedriusmecius.listings.data.local.PaymentMethod
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.local.UserAddress
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
) :
    BaseViewModel<CheckoutState, CheckoutState.Event>(CheckoutState()) {

    private val fetchedAddresses = MutableLiveData<List<UserAddress>>()
    val userAddresses: MutableLiveData<List<UserAddress>>
        get() = fetchedAddresses

    private val fetchedUser = MutableLiveData<User>()
    val currentUser: MutableLiveData<User>
        get() = fetchedUser

    private val fetchedPaymentMethods = MutableLiveData<List<PaymentMethod>>()
    val paymentMethods: MutableLiveData<List<PaymentMethod>>
        get() = fetchedPaymentMethods

    private val fetchedCartItems = MutableLiveData<List<Product>>()
    val cartItems: MutableLiveData<List<Product>>
        get() = fetchedCartItems

    override fun handleState(newState: CheckoutState) {
        return when (val req = newState.request) {
            is CheckoutState.Request.FetchData -> {
                fetchedUser.value = userPreferences.getUser()
                fetchedAddresses.value = fetchedUser.value!!.addresses
                fetchedPaymentMethods.value =
                    userPreferences.getUser().paymentMethods.toList() + userPreferences.getUser().paymentMethods.toList() + userPreferences.getUser().paymentMethods.toList()
                fetchedCartItems.value = userPreferences.getAllProducts()[0].products
            }
            else -> {}
        }
    }
}