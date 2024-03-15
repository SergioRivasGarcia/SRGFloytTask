package com.example.domain.products.usecase

import com.example.domain.products.repository.IProductDBRepository
import javax.inject.Inject

class DeleteAllProductsDatabaseUseCase @Inject constructor(private val productDBRepository: IProductDBRepository) {
    suspend operator fun invoke() = productDBRepository.deleteAllProducts()
}