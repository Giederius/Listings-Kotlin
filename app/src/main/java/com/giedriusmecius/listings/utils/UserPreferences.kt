package com.giedriusmecius.listings.utils

import android.content.SharedPreferences
import com.giedriusmecius.listings.data.local.PaymentMethodResponse
import com.giedriusmecius.listings.data.local.UserAddressResponse
import com.google.gson.Gson
import javax.inject.Singleton

@Singleton
class UserPreferences(private val sharedPreferences: SharedPreferences) {

    fun saveUserPaymentMethods(paymentMethods: PaymentMethodResponse) {
        val json = Gson().toJson(paymentMethods)
        sharedPreferences.edit().putString(USER_PAYMENT_METHODS, json).apply()
    }

    fun getUserPaymentMethods(): PaymentMethodResponse {
        val paymentMethods = sharedPreferences.getString(USER_PAYMENT_METHODS, "")
        return Gson().fromJson(paymentMethods, PaymentMethodResponse::class.java)
            ?: PaymentMethodResponse()
    }

    fun saveUserAddresses(userAddress: UserAddressResponse) {
        val json = Gson().toJson(userAddress)
        sharedPreferences.edit().putString(USER_ADDRESS, json).apply()
    }

    fun getUserAddresses(): UserAddressResponse {
        val userAddresses = sharedPreferences.getString(USER_ADDRESS, "")
        return Gson().fromJson(userAddresses, UserAddressResponse::class.java)
            ?: UserAddressResponse()
    }

    companion object {
        const val USER_PAYMENT_METHODS = "userPaymentMethods"
        const val USER_ADDRESS = "userAddress"
    }
}