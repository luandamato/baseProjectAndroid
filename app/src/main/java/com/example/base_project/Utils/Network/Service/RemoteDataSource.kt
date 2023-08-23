package com.example.base_project.Utils.Network.Service

import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Activities.Login.Model.SignInRequest
import com.example.base_project.Activities.NewProduct.Model.ProductRequest
import com.example.base_project.Utils.Network.BaseModel
import com.example.base_project.Utils.Network.NetworkConstants
import com.example.base_project.Utils.Network.ServiceGenerator

class RemoteDataSource {

    private val serviceLogin = ServiceGenerator.createService(Request::class.java, url = NetworkConstants.BASE_URL_LOGIN)
    private val serviceTest = ServiceGenerator.createService(Request::class.java, url = NetworkConstants.BASE_URL)

    fun singin(request: SignInRequest) = serviceLogin.singin(request)
    fun getProducts() = serviceTest.getProducts()
    fun getProduct(id: Int) = serviceTest.getProduct(id)
    fun deleteProduct(id: Int) = serviceTest.deleteProduct(id)
    fun createProduct(request: ProductRequest) = serviceTest.createProduct(request)
    fun updateProduct(id: Int, request: ProductRequest) = serviceTest.updateProduct(id, request)
}