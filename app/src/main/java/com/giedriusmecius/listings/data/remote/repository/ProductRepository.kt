package com.giedriusmecius.listings.data.remote.repository

import com.giedriusmecius.listings.data.remote.model.product.Product
import javax.inject.Singleton

@Singleton
interface ProductRepository {
    suspend fun getAllProducts(): Result<List<Product>>
}