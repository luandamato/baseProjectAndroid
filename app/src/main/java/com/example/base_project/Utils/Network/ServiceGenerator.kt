package com.example.base_project.Utils.Network

import com.kniebuhr.baseandroid.data.NiInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    fun <S> createService(serviceClass: Class<S>, url: String): S {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//        httpClient.addInterceptor(NiInterceptor())
        httpClient.connectTimeout(60L, TimeUnit.SECONDS)
        httpClient.readTimeout(60L, TimeUnit.SECONDS)
        httpClient.writeTimeout(60L, TimeUnit.SECONDS)

        retrofit.client(httpClient.build())
        return retrofit.build().create(serviceClass)
    }

}