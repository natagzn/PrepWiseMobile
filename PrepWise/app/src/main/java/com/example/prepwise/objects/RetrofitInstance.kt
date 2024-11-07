package com.example.prepwise.objects

import android.content.Context
import android.util.Log
import com.example.prepwise.ApiService
import com.example.prepwise.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://prepwiseback-371541286df6.herokuapp.com"

    private lateinit var retrofit: Retrofit

    fun initRetrofit(context: Context) {
        val sharedPref = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val authToken = sharedPref.getString("auth_token", null) ?: ""

        Log.d("TOKEN", authToken)

        val auth = "Bearer $authToken"
        val cont = "application/json"

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authToken))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    fun api(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
