package com.giedriusmecius.listings.data.remote.api

import com.giedriusmecius.listings.data.remote.model.product.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
}