package com.giedriusmecius.listings.data

import com.giedriusmecius.listings.data.remote.model.product.Product
import com.google.android.datatransport.runtime.dagger.Module
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

private val BASE_URL = "https://fakestoreapi.com/"

val client = OkHttpClient.Builder()
    .connectTimeout(2, TimeUnit.MINUTES)
    .writeTimeout(2, TimeUnit.MINUTES) // write timeout
    .readTimeout(2, TimeUnit.MINUTES) // read timeout
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(client)
        .build()

interface ProductApiService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
}

object ProductApi {
    val retrofitService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }
}