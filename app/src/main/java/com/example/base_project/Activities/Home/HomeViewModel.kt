package com.example.base_project.Activities.Home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Utils.Extensions.singleSubscribe
import com.example.base_project.Utils.Network.Service.RemoteDataSource
import com.example.base_project.Utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(private val repository: RemoteDataSource = RemoteDataSource()) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = SingleLiveEvent<String?>()
    val products = SingleLiveEvent<List<ProductResponse>>()
    fun get(){
        loading.value = true
        compositeDisposable.add(repository.getProducts()
            .singleSubscribe(onSuccess = {
                loading.value = false
                products.value = it
            }, onError = {
                loading.value = false
                errorMessage.value = "${it.statusCode} ${it.message}"
            }))
    }

    fun delete(id: Int){
        loading.value = true
        compositeDisposable.add(repository.deleteProduct(id)
            .singleSubscribe(onSuccess = {
                loading.value = false
                get()
            }, onError = {
                loading.value = false
                errorMessage.value = "${it.statusCode} ${it.message}"
            }))
    }
}