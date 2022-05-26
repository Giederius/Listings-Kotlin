package com.giedriusmecius.listings.data.remote.api

import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>
}