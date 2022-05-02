package com.giedriusmecius.listings.data.remote.api

import com.giedriusmecius.listings.data.remote.model.product.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun getAllProducts(): List<Product>
}