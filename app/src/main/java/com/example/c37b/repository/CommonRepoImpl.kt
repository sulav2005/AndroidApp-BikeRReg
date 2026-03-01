package com.example.c37b.repository

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.example.c37b.model.ProductModel
import java.io.InputStream
import java.util.concurrent.Executors

class CommonRepoImpl : CommonRepo {

    override fun deleteProduct(productID: String, callback: (Boolean, String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getProductById(productID: String, callback: (Boolean, String, ProductModel?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun uploadImage(context: Context, imageUri: String, callback: (Boolean, String, String) -> Unit) {
        // TODO: Initialize your Cloudinary instance here
        // val c = Cloudinary(your_config)

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(Uri.parse(imageUri))
                var fileName = getFileNameFromUri(context, imageUri)

                fileName = fileName?.substringBeforeLast(".") ?: "uploaded_image"

                // val response = c.uploader().upload(
                //     inputStream, Object.asMap(
                //         "public_id", fileName,
                //         "resource_type", "image"
                //     )
                // )

                // var imageUrl = response["url"] as String?
                // imageUrl = imageUrl?.replace("http://", "https://")

                handler.post {
                    // callback(true, "Image uploaded successfully", imageUrl.orEmpty())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    callback(false, "Image upload failed: ${e.message}", "")
                }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, uri: String): String? {
        TODO("Not yet implemented")
    }
}
