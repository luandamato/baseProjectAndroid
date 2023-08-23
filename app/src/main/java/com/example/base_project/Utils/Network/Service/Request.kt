package com.example.base_project.Utils.Network.Service

import com.example.base_project.Activities.Home.Models.ProductResponse
import com.example.base_project.Activities.Login.Model.SignInRequest
import com.example.base_project.Activities.Login.Model.SignInResponse
import com.example.base_project.Activities.NewProduct.Model.ProductRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Request {
    @POST("login")
    fun singin(@Body request: SignInRequest): Single<SignInResponse>
    @GET("products")
    fun getProducts(): Single<List<ProductResponse>>

    @GET("products/{id}")
    fun getProduct(@Path ("id") id: Int): Single<ProductResponse>
    @DELETE("products/{id}")
    fun deleteProduct(@Path ("id") id: Int): Single<ProductResponse>
    @POST("products")
    fun createProduct(@Body request: ProductRequest): Single<ProductResponse>

    @PUT("products/{id}")
    fun updateProduct(@Path ("id") id: Int, @Body request: ProductRequest): Single<ProductResponse>
}