package com.giedriusmecius.listings.data

import com.giedriusmecius.listings.data.remote.api.ProductApi
import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.data.remote.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val products = api.getAllProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllProducts(): Result<List<Category>> {
        return try {
            val categories = api.getAllCategories()
            val products = api.getAllProducts()
            val mappedCategories: List<Category> = categories.map { cat ->
                val filteredProducts = products.filter { it.category == cat }
                Category(cat, filteredProducts)
            }

            Result.success(mappedCategories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}