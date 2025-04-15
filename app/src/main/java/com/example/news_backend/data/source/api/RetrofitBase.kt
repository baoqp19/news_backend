package com.example.news_backend.data.source.api

import com.example.news_backend.data.sharedpreferences.DataLocalManager
import com.example.news_backend.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBase {
    private const val BASE_URL = Constants.BASE_URL_LOGIN

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer ${DataLocalManager.getInstance().getInfoTokenKey()}")
                    .build()
                chain.proceed(request)
            })
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(httpClient)
            .build()
    }

    fun <T> apiService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}