package com.example.presentation.main.basket.checkout

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.databinding.FragmentCheckoutBinding
import com.example.presentation.main.base.ActionBarViewModel
import com.example.presentation.main.base.BaseFragment
import com.example.presentation.main.basket.BasketViewModel
import com.example.presentation.main.util.afterFocusLost
import com.example.presentation.main.util.afterTextChanged
import com.example.presentation.main.util.getCurrencySymbol
import com.example.presentation.main.util.isValidEmail
import com.example.presentation.main.util.viewBinding


class CheckoutFragment : BaseFragment(R.layout.fragment_checkout) {

    private val viewBinding by viewBinding { FragmentCheckoutBinding.bind(it) }
    private val viewModel: CheckoutViewModel by viewModels()
    private val basketViewModel: BasketViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
        bindUI()
    }

    override fun configActionBarForFragment() {
        actionBarViewModel.configActionBar(
            "Checkout details",
            ActionBarViewModel.NavBehaviour.BACK, view
        )
    }


    private fun configureUI() {
        with(viewBinding) {
            with(viewModel) {
                tilName.editText?.afterTextChanged {
                    buyer.name = it
                }

                tilName.editText?.afterFocusLost {
                    if ((it as EditText).text.isNullOrBlank()) {
                        it.error = "Name cannot be blank"
                    }
                }

                tilAddress.editText?.afterTextChanged {
                    buyer.address = it
                }

                tilAddress.editText?.afterFocusLost {
                    if ((it as EditText).text.isNullOrBlank()) {
                        it.error = "Address cannot be blank"
                    }
                }

                tilCity.editText?.afterTextChanged {
                    buyer.city = it
                }

                tilCity.editText?.afterFocusLost {
                    if ((it as EditText).text.isNullOrBlank()) {
                        it.error = "City cannot be blank"
                    }
                }

                tilPc.editText?.afterTextChanged {
                    buyer.postalCode = it
                }

                tilPc.editText?.afterFocusLost {
                    if ((it as EditText).text.isNullOrBlank()) {
                        it.error = "Postal Code cannot be blank"
                    }
                }

                tilEmail.editText?.afterTextChanged {
                    buyer.email = it
                }

                tilEmail.editText?.afterFocusLost {
                    if ((it as EditText).text.isNullOrBlank()) {
                        it.error = "Email cannot be blank"
                    }
                    if (!it.text.isValidEmail()) {
                        it.error = "Invalid email format"
                    }
                }

                tilPhoneNumber.editText?.afterTextChanged {
                    buyer.phone = it
                }

                tilPhoneNumber.editText?.afterFocusLost {
                    if ((it as EditText).text.isNullOrBlank()) {
                        it.error = "Phone Number cannot be blank"
                    }
                }

                btnPay.setOnClickListener {
                    findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToPaymentConfirmationFragment())
                }
            }
        }
    }

    private fun bindUI() {
        with(viewBinding) {
            with(viewModel) {
                payAvailabilityLive.observe(viewLifecycleOwner) {
                    btnPay.isEnabled = it
                }

                basketViewModel.basketTotalLive.observe(viewLifecycleOwner) {
                    tvBasketTotal.text =
                        getString(R.string.total_price, it, getCurrencySymbol())
                }
            }
        }
    }
}