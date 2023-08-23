package com.example.base_project.Activities.ProductDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Activities.NewProduct.Model.ProductRequest
import com.example.base_project.Utils.Extensions.singleSubscribe
import com.example.base_project.Utils.Network.Service.RemoteDataSource
import com.example.base_project.Utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class ProductDetailViewModel (private val repository: RemoteDataSource = RemoteDataSource()) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = SingleLiveEvent<String?>()
    val productDetail = SingleLiveEvent<ProductResponse>()


    fun getProduct(id: Int){
        loading.value = true
        compositeDisposable.add(repository.getProduct(id)
            .singleSubscribe(onSuccess = {
                loading.value = false
                productDetail.value = it
            }, onError = {
                loading.value = false
                errorMessage.value = "${it.statusCode} ${it.message}"
            }))
    }
}