package com.example.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product
import com.example.domain.products.usecase.GetProductsListUseCase
import com.example.presentation.main.products.list.HomeViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getProductsListUseCase: GetProductsListUseCase

    private lateinit var viewModel: HomeViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(
            getProductsListUseCase, savedStateHandle
        )

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `getProductsListUseCase happy path is success`() = runTest {
        // given
        val product: Product = mock()
        whenever(getProductsListUseCase()).thenReturn(
            DataResult.Success(
                listOf(product)
            )
        )

        viewModel.productList.flow.test {
            // when
            viewModel.getProductsList()

            // then
            val item = awaitItem()
            MatcherAssert.assertThat(item, CoreMatchers.instanceOf(DataResult.Success::class.java))
            MatcherAssert.assertThat(
                (item as DataResult.Success).data?.first(),
                CoreMatchers.equalTo(product)
            )
        }
    }

    @Test
    fun `getProductsListUseCase is Error`() = runTest {
        // given
        whenever(getProductsListUseCase()).thenReturn(
            DataResult.Error(
                Exception("Test")
            )
        )

        viewModel.productList.flow.test {
            // when
            viewModel.getProductsList()

            // then
            val item = awaitItem()
            MatcherAssert.assertThat(item, CoreMatchers.instanceOf(DataResult.Error::class.java))
            MatcherAssert.assertThat(
                (item as DataResult.Error).exception.message,
                CoreMatchers.equalTo("Test")
            )
        }
    }
}