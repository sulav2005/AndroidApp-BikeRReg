package com.example.c37b.repository

import com.example.c37b.model.ProductModel

class ProductRepoImpl : ProductRepo {

    private val products = mutableMapOf<String, ProductModel>()

    override fun addProduct(model: ProductModel,
                            callback: (Boolean, String) -> Unit) {
        val id = java.util.UUID.randomUUID().toString()
        model.productId = id
        products[id] = model
        callback(true, "product added successfully")
    }

    override fun updateProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        if (products.containsKey(model.productId)) {
            products[model.productId] = model
            callback(true, "product updated successfully")
        } else {
            callback(false, "Product not found")
        }
    }

    override fun deleteProduct(
        productID: String,
        callback: (Boolean, String) -> Unit
    ) {
        if (products.remove(productID) != null) {
            callback(true, "product deleted successfully")
        } else {
            callback(false, "Product not found")
        }
    }

    override fun getProductById(
        productID: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    ) {
        val product = products[productID]
        if (product != null) {
            callback(true, "Product fetched", product)
        } else {
            callback(false, "Product not found", null)
        }
    }

    override fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        callback(true, "product fetched", products.values.toList())
    }

    override fun getProductByCategory(
        categoryId: String,
        callback: (Boolean, String, List<ProductModel>?) -> Unit
    ) {
        val filteredProducts = products.values.filter { it.categoryId == categoryId }
        callback(true, "product fetched", filteredProducts)
    }
}
