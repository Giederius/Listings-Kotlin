package com.giedriusmecius.listings.data

import com.giedriusmecius.listings.data.remote.model.category.Category
import com.giedriusmecius.listings.data.remote.model.product.Product

class ProductMapper : MapperToProductList<List<Category>, List<Product>> {
    override fun mapToProductList(T: List<Category>): List<Product> {
        val productList = mutableListOf<Product>()
        T.map {
            it.products.forEach { product ->
                productList.add(product)
            }
        }
        return productList.toList()
    }

    override fun mapToCategoryList(R: List<Product>): List<Category> {
        TODO("Not yet implemented")
    }
}