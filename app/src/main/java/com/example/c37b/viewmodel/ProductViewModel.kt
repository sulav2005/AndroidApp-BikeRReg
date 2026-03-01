package com.example.c37b.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.c37b.model.ProductModel
import com.example.c37b.repository.ProductRepo

class ProductViewModel(val repo : ProductRepo) : ViewModel(){

    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit){
        repo.addProduct(model,callback)
    }

    fun updateProduct(model: ProductModel, callback: (Boolean, String) -> Unit){
        repo.updateProduct(model,callback)
    }

    fun deleteProduct(productID: String, callback: (Boolean, String) -> Unit){
        repo.deleteProduct(productID,callback)
    }

    private val _products = MutableLiveData<ProductModel?>()
    val products : MutableLiveData<ProductModel?> get() = _products

    private val _allProducts = MutableLiveData<List<ProductModel>?>()
    val allProducts : MutableLiveData<List<ProductModel>?> get() = _allProducts

    fun getProductById(productID: String){
        repo.getProductById(productID){
            success,message,data->
            if(success){
                _products.postValue(data)
            }
        }
    }

    fun getAllProduct(){
        repo.getAllProduct {
                success,message,data->
            if(success){
                _allProducts.postValue(data)
            }
        }
    }

//    fun getProductByCategory(categoryId:String){
//
//    }
}