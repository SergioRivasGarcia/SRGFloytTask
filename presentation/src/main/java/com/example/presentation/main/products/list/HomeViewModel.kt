package com.example.presentation.main.products.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product
import com.example.domain.products.usecase.GetProductsListUseCase
import com.example.presentation.main.util.receiveAsEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _productsList = Channel<DataResult<List<Product>>>()
    val productList = _productsList.receiveAsEventFlow()

    fun getProductsList() {
        viewModelScope.launch {
            delay(2000) // Just to showcase shimmer while loading
            val result = getProductsListUseCase()
            _productsList.trySend(result)
        }
    }

    /**
     * We save the product list in memory so in case of the app process is killed no network call is
     * needed.
     **/
    var products: List<Product>?
        set(value) {
            savedStateHandle["products"] = value
        }
        get() = savedStateHandle["products"]

    fun resetData() {
        products = null
    }
}
