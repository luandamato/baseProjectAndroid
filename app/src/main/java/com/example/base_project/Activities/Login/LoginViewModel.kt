package com.example.base_project.Activities.Login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.base_project.Activities.Login.Model.SignInRequest
import com.example.base_project.Activities.Login.Model.SignInResponse
import com.example.base_project.Utils.Extensions.singleSubscribe
import com.example.base_project.Utils.Network.Service.RemoteDataSource
import com.example.base_project.Utils.PreferencesHelper
import com.example.base_project.Utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(private val repository: RemoteDataSource = RemoteDataSource()) : ViewModel(){

    private val compositeDisposable = CompositeDisposable()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = SingleLiveEvent<String?>()
    val success = SingleLiveEvent<SignInResponse>()

    fun login(username: String, password: String){
        loading.value = true
        compositeDisposable.add(repository.singin(SignInRequest(username, password))
            .singleSubscribe(onSuccess = {
            loading.value = false
            success.value = it
            PreferencesHelper.session = it
        }, onError = {
            loading.value = false
            errorMessage.value = "${it.statusCode} ${it.message}"
        }))
    }
}