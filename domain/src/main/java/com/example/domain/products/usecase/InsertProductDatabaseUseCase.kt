package com.example.domain.products.usecase

import com.example.domain.products.entity.Product
import com.example.domain.products.repository.IProductDBRepository
import javax.inject.Inject

class InsertProductDatabaseUseCase @Inject constructor(private val productDBRepository: IProductDBRepository) {
    suspend operator fun invoke(product: Product) = productDBRepository.insert(product)
}