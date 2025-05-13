package com.example.task_01

import retrofit2.Call
import retrofit2.http.GET


interface ProductService {
    @get:GET("products")
    val products: Call<ProductResponse?>?
}