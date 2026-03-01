package com.example.c37b.repository

import android.content.Context
import android.net.Uri
import com.example.c37b.model.ProductModel

interface CommonRepo {
    fun deleteProduct(productID: String,
                      callback: (Boolean, String) -> Unit)

    fun getProductById(productID: String,
                       callback: (Boolean, String, ProductModel?) -> Unit)

    fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit)

    fun uploadImage(context: Context,
                    imageUri: String,
                    callback: (Boolean, String, String) -> Unit)

    fun getFileNameFromUri(context: Context, uri: String): String?
}
