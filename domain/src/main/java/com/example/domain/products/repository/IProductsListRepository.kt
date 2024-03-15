package com.example.domain.products.repository

import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product

interface IProductsListRepository {
    suspend fun getProductsList(): DataResult<List<Product>>
}