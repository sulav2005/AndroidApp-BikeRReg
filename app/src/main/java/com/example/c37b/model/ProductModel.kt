package com.example.c37b.model

data class ProductModel(
    var productId : String = "",
    var productName : String = "",
    var price : Double = 0.0,
    var description : String = "",
    var categoryId : String = "",
    var imageUrl : String = ""
){
    fun toMap() : Map<String,Any?>{
        return mapOf(
            "productName" to productName,
            "price" to price,
            "description" to description,
            "categoryId" to categoryId,
        )
    }
}



