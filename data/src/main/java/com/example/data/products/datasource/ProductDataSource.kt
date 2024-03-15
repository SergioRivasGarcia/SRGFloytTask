package com.example.data.products.datasource

import com.example.data.products.entity.ProductResponse
import com.example.data.products.entity.toEntity
import com.example.data.products.remote.ProductsApi
import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product
import retrofit2.Response

class ProductsDataSource(
    private val productsApi: ProductsApi,
) : IProductsRemoteDataSource {

    override suspend fun getProductsList(): DataResult<List<Product>> {
        val response: Response<List<ProductResponse>>

        return try {
            response = productsApi.getProducts()
            if (response.isSuccessful) {
                DataResult.Success(response.body()?.map { it.toEntity() })
            } else {
                // Should the call have any other code outside range [200-300] could be logged here
                DataResult.Error(Exception())
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

}