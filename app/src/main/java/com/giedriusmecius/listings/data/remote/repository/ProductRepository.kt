package com.giedriusmecius.listings.data.remote.repository

import com.giedriusmecius.listings.data.remote.model.product.Product
import retrofit2.Response
import javax.inject.Singleton

@Singleton
interface ProductRepository {
    suspend fun getAllProducts(): Response<List<Product>>
}