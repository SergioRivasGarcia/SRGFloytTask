package com.example.app.di

import com.example.data.products.datasource.ProductsDataSource
import com.example.data.products.remote.ProductsApi
import com.example.data.products.repository.ProductsListRepository
import com.example.domain.products.repository.IProductsListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Volatile
    var productsListRepository: ProductsListRepository? = null

    @Provides
    fun provideProductsListRepository(productsApi: ProductsApi): IProductsListRepository {
        synchronized(this) {
            return productsListRepository ?: createProductsListRepository(productsApi)
        }
    }

    private fun createProductsListRepository(productsApi: ProductsApi): IProductsListRepository {
        val newRepo =
            ProductsListRepository(
                ProductsDataSource(productsApi)
            )
        productsListRepository = newRepo
        return newRepo
    }

}