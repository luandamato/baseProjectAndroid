package com.example.base_project.Activities.NewProduct

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Activities.NewProduct.Model.ProductRequest
import com.example.base_project.Utils.Extensions.singleSubscribe
import com.example.base_project.Utils.Network.Service.RemoteDataSource
import com.example.base_project.Utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class NewProductViewModel (private val repository: RemoteDataSource = RemoteDataSource()) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    val loading = MutableLiveData<Boolean>()
    val loadingRequest = MutableLiveData<Boolean>()
    val errorMessage = SingleLiveEvent<String?>()
    val product = SingleLiveEvent<ProductResponse>()
    val productDetail = SingleLiveEvent<ProductResponse>()

    fun createProduct(item: ProductRequest){
        loadingRequest.value = true
        compositeDisposable.add(repository.createProduct(item)
            .singleSubscribe(onSuccess = {
                loadingRequest.value = false
                product.value = it
            }, onError = {
                loadingRequest.value = false
                errorMessage.value = "${it.statusCode} ${it.message}"
            }))
    }

    fun updateProduct(id: Int, item: ProductRequest){
        loadingRequest.value = true
        compositeDisposable.add(repository.updateProduct(id, item)
            .singleSubscribe(onSuccess = {
                loadingRequest.value = false
                product.value = it
            }, onError = {
                loadingRequest.value = false
                errorMessage.value = "${it.statusCode} ${it.message}"
            }))
    }

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