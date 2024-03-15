package com.example.presentation.main.products.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.base.DataResult
import com.example.domain.products.entity.Product
import com.example.presentation.R
import com.example.presentation.databinding.FragmentHomeBinding
import com.example.presentation.main.base.ActionBarViewModel
import com.example.presentation.main.base.BaseFragment
import com.example.presentation.main.basket.BasketViewModel
import com.example.presentation.main.products.list.adapter.ProductAdapter
import com.example.presentation.main.products.list.adapter.ProductItem
import com.example.presentation.main.util.on
import com.example.presentation.main.util.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewBinding by viewBinding { FragmentHomeBinding.bind(it) }
    private val viewModel: HomeViewModel by viewModels()
    private val basketViewModel: BasketViewModel by activityViewModels()

    private lateinit var productAdapter: ProductAdapter

    override fun configActionBarForFragment() {
        actionBarViewModel.configActionBar(
            R.string.home_fragment_title,
            ActionBarViewModel.NavBehaviour.NONE,
            view
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            // If we don't have the list from a previous call we call to get it
            if (products.isNullOrEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        on(productList) { result ->
                            when (result) {
                                is DataResult.Success -> {
                                    products = result.data
                                    configureUI()
                                    bindUI(requireNotNull(result.data))
                                    hideLoading()
                                }

                                is DataResult.Error -> {
                                    showSnackBarError("Error retrieving the products")
                                    hideLoading()
                                }

                                is DataResult.Status -> {
                                    when (result.status) {
                                        DataResult.DataStatus.LOADING -> {
                                            showLoading()
                                        }

                                        else -> {
                                            // No-op
                                        }
                                    }
                                }

                                else -> {
                                    // No-op
                                }
                            }
                        }
                    }
                }
                getProductsList()
            } else {
                // If we do have a list from a previous call we skip the network call
                configureUI()
                bindUI(requireNotNull(products))
                hideLoading()
            }
        }
    }

    private fun configureUI() {
        with(viewBinding) {
            productAdapter = ProductAdapter(requireContext(),
                object : ProductAdapter.UpdatedBasketProductsListener {
                    override fun onUpdateProduct(productItem: ProductItem) {
                        basketViewModel.updateBasket(productItem.toProduct())
                    }

                    override fun onDeleteProduct(productItem: ProductItem) {
                        // No-op
                    }
                })
        }
    }

    private fun bindUI(data: List<Product>) {
        with(viewBinding) {
            basketViewModel.basketLive.observe(viewLifecycleOwner) { productDBList ->
                productDBList.forEach { basketProduct ->
                    data.forEach { product ->
                        if (product.id == basketProduct.id) {
                            product.quantity = basketProduct.quantity
                        }
                    }
                }
            }

            productAdapter.setData(data)

            rvProducts.layoutManager = LinearLayoutManager(context)
            rvProducts.adapter = productAdapter
        }
    }

    private fun hideLoading() {
        with(viewBinding) {
            shimmerProducts.stopShimmer()
            shimmerProducts.isVisible = false
        }
    }

    private fun showLoading() {
        with(viewBinding) {
            shimmerProducts.startShimmer()
            shimmerProducts.isVisible = true
        }
    }

    private fun showSnackBarError(text: String) {
        val snackBar = getSnackBar(text, Snackbar.LENGTH_INDEFINITE)
        snackBar?.setAction(R.string.retry) {
            showLoading()
            viewModel.getProductsList()
        }?.show()
    }

    /**
     *Only when the view is destroyed we clean the viewModel data so when coming back to this
     *fragment another network call is made but not if the process dies.
     **/
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetData()
    }

}