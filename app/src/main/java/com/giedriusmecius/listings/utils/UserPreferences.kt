package com.giedriusmecius.listings.utils

import android.content.SharedPreferences
import com.giedriusmecius.listings.data.local.User
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.InCartProduct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Singleton

@Singleton
class UserPreferences(private val sharedPreferences: SharedPreferences) {

    fun saveUser(user: User) {
        val json = Gson().toJson(user)
        sharedPreferences.edit().putString(USER, json).apply()
    }

    fun getUser(): User {
        val user = sharedPreferences.getString(USER, "")
        return Gson().fromJson(user, User::class.java) ?: User()
    }

    fun saveRecentSearches(queries: List<String>) {
        val json = Gson().toJson(queries)
        sharedPreferences.edit().putString(QUERIES, json).apply()
    }

    fun getRecentSearches(): List<String> {
        val queries = sharedPreferences.getString(QUERIES, "")
        return Gson().fromJson(queries, object : TypeToken<List<String>>() {}.type) ?: emptyList()
    }

    fun saveAllProducts(products: List<Category>) {
        val json = Gson().toJson(products)
        sharedPreferences.edit().putString(PRODUCTS, json).apply()
    }

    fun getAllProducts(): List<Category> {
        val products = sharedPreferences.getString(PRODUCTS, "")
        return Gson().fromJson(products, object : TypeToken<List<Category>>() {}.type)
            ?: emptyList()
    }

    fun saveCartProducts(products: List<InCartProduct>) {
        val json = Gson().toJson(products)
        sharedPreferences.edit().putString(IN_CART_PRODUCTS, json).apply()
    }

    fun getCartProducts(): List<InCartProduct> {
        val products = sharedPreferences.getString(IN_CART_PRODUCTS, "")
        return Gson().fromJson(products, object : TypeToken<List<InCartProduct>>() {}.type)
            ?: emptyList()
    }

    companion object {
        const val USER = "user"
        const val QUERIES = "queries"
        const val PRODUCTS = "products"
        const val IN_CART_PRODUCTS = "inCartProducts"
    }
}