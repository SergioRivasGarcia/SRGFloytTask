package com.example.domain.products.usecase

import com.example.domain.products.repository.IProductsListRepository
import javax.inject.Inject

class GetProductsListUseCase @Inject constructor(private val productsListRepository: IProductsListRepository) {
    suspend operator fun invoke() = productsListRepository.getProductsList()
}