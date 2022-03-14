//package com.giedriusmecius.listings.data.remote
//
//import com.giedriusmecius.listings.data.remote.api.ProductApi
//import com.giedriusmecius.listings.data.remote.model.product.Product
//import com.giedriusmecius.listings.data.remote.repository.ProductRepository
//import retrofit2.Response
//
//class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository() {
//
//    override suspend fun getAllProducts(): Response<List<Product>> {
//        return api.getAllProducts()
//    }
//}