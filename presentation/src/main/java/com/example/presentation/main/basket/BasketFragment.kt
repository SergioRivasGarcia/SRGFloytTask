package com.example.presentation.main.basket

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.R
import com.example.presentation.databinding.FragmentBasketBinding
import com.example.presentation.main.base.ActionBarViewModel
import com.example.presentation.main.base.BaseFragment
import com.example.presentation.main.products.list.adapter.ProductAdapter
import com.example.presentation.main.products.list.adapter.ProductItem
import com.example.presentation.main.util.getCurrencySymbol
import com.example.presentation.main.util.round
import com.example.presentation.main.util.viewBinding

class BasketFragment : BaseFragment(R.layout.fragment_basket) {

    private val viewBinding by viewBinding { FragmentBasketBinding.bind(it) }
    private val viewModel: BasketViewModel by activityViewModels()

    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
        bindUI()
    }

    override fun configActionBarForFragment() {
        actionBarViewModel.configActionBar(
            R.string.basket_fragment_title,
            ActionBarViewModel.NavBehaviour.NONE,
            view
        )
    }

    private fun configureUI() {
        with(viewBinding) {
            with(viewModel) {
                productAdapter = ProductAdapter(requireContext(),
                    object : ProductAdapter.UpdatedBasketProductsListener {
                        override fun onUpdateProduct(productItem: ProductItem) {
                            viewModel.updateBasket(productItem.toProduct())
                        }

                        override fun onDeleteProduct(productItem: ProductItem) {
                            viewModel.deleteProduct(productItem.toProduct())
                        }
                    })
                productAdapter.setDeleteMode(true)

                btnCheckout.setOnClickListener {
                    findNavController().navigate(BasketFragmentDirections.actionBasketFragmentToCheckoutFragment())
                }
            }
        }
    }

    private fun bindUI() {
        with(viewBinding) {
            with(viewModel) {
                viewModel.basketLive.observe(viewLifecycleOwner) { productDBList ->
                    productAdapter.setData(productDBList)
                    checkCheckout()
                }

                viewModel.basketTotalLive.observe(viewLifecycleOwner) {
                    val totalPrice = it.round(2)
                    if (totalPrice > 0) {
                        btnCheckout.text = getString(
                            R.string.checkout_price, totalPrice,
                            getCurrencySymbol()
                        )
                    } else {
                        btnCheckout.text = getString(R.string.checkout)
                    }
                }

                checkoutAvailabilityLive.observe(viewLifecycleOwner) {
                    btnCheckout.isEnabled = it
                    rvBasket.isVisible = it
                    viewEmptyBasket.root.isVisible = !it
                }

                checkCheckout()

                rvBasket.layoutManager = LinearLayoutManager(context)
                rvBasket.adapter = productAdapter
            }
        }
    }

}