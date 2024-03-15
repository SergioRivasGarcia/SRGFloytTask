package com.example.domain.usecase

import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product
import com.example.domain.products.repository.IProductsListRepository
import com.example.domain.products.usecase.GetProductsListUseCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetProductsListUseCaseTest {

    lateinit var sut: GetProductsListUseCase
    private lateinit var repo: IProductsListRepository

    @Test
    fun `products are returned on successful call`() = runBlocking {
        // given
        val product: Product = mock()

        repo = mock {
            onBlocking { getProductsList() } doReturn runBlocking {
                DataResult.Success(listOf(product, product, product))
            }
        }

        // when
        sut = GetProductsListUseCase(repo)
        val result = sut()

        // then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(DataResult.Success::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Success<List<Product>>).data?.size,
            CoreMatchers.equalTo(3)
        )
        MatcherAssert.assertThat(result.data, CoreMatchers.hasItems(product))
    }

    @Test
    fun `error is returned when GetProductsListUseCase fails`() = runBlocking {
        // given
        repo = mock {
            onBlocking { getProductsList() } doReturn runBlocking {
                DataResult.Error(Exception("Failed to retrieve products."))
            }
        }

        // when
        sut = GetProductsListUseCase(repo)
        val result = sut()

        // then
        MatcherAssert.assertThat(result, CoreMatchers.instanceOf(DataResult.Error::class.java))
        MatcherAssert.assertThat(
            (result as DataResult.Error).exception.message,
            CoreMatchers.equalTo("Failed to retrieve products.")
        )
    }
}