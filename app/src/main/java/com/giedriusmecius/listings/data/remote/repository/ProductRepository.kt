package com.giedriusmecius.listings.data.remote.repository

import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import javax.inject.Singleton

@Singleton
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getAllProducts(): Result<List<Category>>
}