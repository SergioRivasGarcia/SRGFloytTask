package com.example.data.products.datasource

import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product

interface IProductsRemoteDataSource {

    suspend fun getProductsList(): DataResult<List<Product>>

}