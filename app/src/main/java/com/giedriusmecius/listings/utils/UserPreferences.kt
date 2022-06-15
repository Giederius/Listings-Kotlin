package com.giedriusmecius.listings.utils

import android.content.SharedPreferences
import com.giedriusmecius.listings.data.local.PaymentMethodResponse
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.local.UserAddressResponse
import com.google.gson.Gson
import javax.inject.Singleton

@Singleton
class UserPreferences(private val sharedPreferences: SharedPreferences) {

    fun saveUser(user: User) {
        val json = Gson().toJson(user)
        sharedPreferences.edit().putString(USER, json).apply()
    }

    fun getUser() : User {
        val user = sharedPreferences.getString(USER, "")
        return Gson().fromJson(user, User::class.java) ?: User()
    }

    companion object {
        const val USER = "user"
    }
}