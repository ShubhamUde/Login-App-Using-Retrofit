package com.shubham.retrofitexample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    private val BASE_URL = "http://192.168.103.147/UserApi/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        @Volatile
        private var retrofitClient: RetrofitClient? = null

        @Synchronized
        fun getInstance(): RetrofitClient {
            if (retrofitClient == null) {
                retrofitClient = RetrofitClient()
            }
            return retrofitClient!!
        }
    }

    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }
}