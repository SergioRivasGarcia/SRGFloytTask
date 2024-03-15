package com.example.presentation.main.basket.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.presentation.main.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor() : ViewModel() {

    val buyer: BuyerDetails
        get() {
            return _buyer
        }

    class BuyerDetails {
        private var _name: String? = null
        private var _address: String? = null
        private var _city: String? = null
        private var _postalCode: String? = null
        private var _email: CharSequence? = null
        private var _phone: String? = null

        var name: String?
            get() {
                return _name
            }
            set(value) {
                _name = value
                checkPayAvailable()
            }

        var address: String?
            get() {
                return _address
            }
            set(value) {
                _address = value
                checkPayAvailable()
            }

        var city: String?
            get() {
                return _city
            }
            set(value) {
                _city = value
                checkPayAvailable()
            }

        var postalCode: String?
            get() {
                return _postalCode
            }
            set(value) {
                _postalCode = value
                checkPayAvailable()
            }

        var email: CharSequence?
            get() {
                return _email
            }
            set(value) {
                _email = value
                checkPayAvailable()
            }

        var phone: String?
            get() {
                return _phone
            }
            set(value) {
                _phone = value
                checkPayAvailable()
            }

        fun isValid(): Boolean {
            return !_name.isNullOrEmpty() &&
                    !_address.isNullOrEmpty() &&
                    !_city.isNullOrEmpty() &&
                    !_postalCode.isNullOrEmpty() &&
                    !_email.isNullOrEmpty() &&
                    _email!!.isValidEmail() &&
                    !_phone.isNullOrEmpty()
        }
    }

    val payAvailabilityLive = _payAvailability as LiveData<Boolean>

    companion object {
        private var _buyer: BuyerDetails = BuyerDetails()
        private var _payAvailability = MutableLiveData(false)

        private fun checkPayAvailable() {
            _payAvailability.value = _buyer.isValid()
        }
    }
}