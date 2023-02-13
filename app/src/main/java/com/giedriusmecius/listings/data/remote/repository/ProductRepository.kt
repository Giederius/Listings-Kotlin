package com.giedriusmecius.listings.data.remote.repository

import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product
import com.giedriusmecius.listings.utils.ResponseResult
import okhttp3.ResponseBody
import javax.inject.Singleton

@Singleton
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getAllProducts(): Result<List<Category>>
    suspend fun getProduct(id: Int): Result<Product>
}