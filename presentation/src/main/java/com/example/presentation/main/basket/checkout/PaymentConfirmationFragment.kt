package com.example.presentation.main.basket.checkout

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.databinding.FragmentPaymentConfirmationBinding
import com.example.presentation.main.base.ActionBarViewModel
import com.example.presentation.main.base.BaseFragment
import com.example.presentation.main.basket.BasketViewModel
import com.example.presentation.main.util.viewBinding

class PaymentConfirmationFragment : BaseFragment(R.layout.fragment_payment_confirmation) {

    private val viewBinding by viewBinding { FragmentPaymentConfirmationBinding.bind(it) }
    private val viewModel: PaymentConfirmationViewModel by viewModels()
    private val basketViewModel: BasketViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
        bindUI()
    }

    override fun configActionBarForFragment() {
        actionBarViewModel.configActionBar(
            "Order confirmed",
            ActionBarViewModel.NavBehaviour.CLOSE, view
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    emptyBasketAndBackToHome()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun emptyBasketAndBackToHome() {
        basketViewModel.emptyBasket()
        findNavController().popBackStack(R.id.nav_basket, true)
        findNavController().navigate(R.id.nav_home)
    }


    private fun configureUI() {
        with(viewBinding) {
            btnConfirm.setOnClickListener {
                emptyBasketAndBackToHome()
            }
        }
    }

    private fun bindUI() {

    }
}