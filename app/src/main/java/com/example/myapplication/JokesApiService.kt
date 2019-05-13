package com.example.myapplication

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query


interface JokesApiService {

    @retrofit2.http.GET("jokes/random/10")
    fun getJokes(@Query("firstName") s: String,
                 @Query("lastName") s1: String): Deferred<Response<Json4Kotlin_Base>>

    @retrofit2.http.GET("jokes/random/10")
    fun getJokes2(): Deferred<Response<Json4Kotlin_Base>>

    object RetrofitFactory {
        private const val BASE_URL = "https://api.icndb.com/"

        fun makeRetrofitService(): JokesApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(JokesApiService::class.java)
        }
    }

}