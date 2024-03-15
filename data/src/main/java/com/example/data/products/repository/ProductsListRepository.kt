package com.example.data.products.repository

import com.example.data.products.datasource.IProductsRemoteDataSource
import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product
import com.example.domain.products.repository.IProductsListRepository


class ProductsListRepository(private val productsRemoteDataSource: IProductsRemoteDataSource) :
    IProductsListRepository {
    override suspend fun getProductsList(): DataResult<List<Product>> {
        return productsRemoteDataSource.getProductsList()
    }
}