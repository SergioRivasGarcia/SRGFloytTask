package com.example.data.products.remote

import com.example.data.network.BaseApiService
import com.example.data.products.entity.ProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApi : BaseApiService {

    @GET("bmdevel/MobileCodeChallengeResources/main/groceryProducts.json")
    suspend fun getProducts(): Response<List<ProductResponse>>

}