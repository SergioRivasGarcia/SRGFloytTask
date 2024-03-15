package com.example.presentation.main.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.products.entity.Product
import com.example.domain.products.repository.IProductDBRepository
import com.example.domain.products.usecase.DeleteAllProductsDatabaseUseCase
import com.example.domain.products.usecase.DeleteProductDatabaseUseCase
import com.example.domain.products.usecase.InsertProductDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    repository: IProductDBRepository,
    private val insertProductDatabaseUseCase: InsertProductDatabaseUseCase,
    private val deleteProductDatabaseUseCase: DeleteProductDatabaseUseCase,
    private val deleteAllProductsDatabaseUseCase: DeleteAllProductsDatabaseUseCase,
) :
    ViewModel() {

    val basketLive: LiveData<List<Product>> = repository.getAllProducts()

    fun updateBasket(product: Product) {
        viewModelScope.launch {
            if (product.quantity > 0)
                insertProductDatabaseUseCase.invoke(product)
            else {
                deleteProductDatabaseUseCase.invoke(product)
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            deleteProductDatabaseUseCase.invoke(product)
        }
    }

    fun emptyBasket() {
        viewModelScope.launch {
            deleteAllProductsDatabaseUseCase.invoke()
        }
    }

    private var _checkoutAvailability = MutableLiveData(false)
    val checkoutAvailabilityLive = _checkoutAvailability as LiveData<Boolean>

    // We check if checkout should be possible and the total value of it at the same time
    fun checkCheckout() {
        _checkoutAvailability.value = basketLive.value?.isNotEmpty()
        _basketTotal.value = basketLive.value?.sumOf { (it.quantity * it.price).toDouble() }
    }

    private var _basketTotal = MutableLiveData(0.0)
    val basketTotalLive = _basketTotal as LiveData<Double>

}