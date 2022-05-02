package com.giedriusmecius.listings.data

import com.giedriusmecius.listings.data.remote.api.ProductApi
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val products = api.getAllProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}