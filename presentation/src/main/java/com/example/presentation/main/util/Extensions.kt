package com.example.presentation.main.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.onEach
import java.text.NumberFormat
import java.util.Locale

/**
 * Subscribes to [EventFlow] streams, the action gets invoked each time a new value gets emitted to
 * the stream. An [EventFlow] can be acquired by using the extension function [receiveAsEventFlow]
 */
inline fun <reified T : Any, EF : EventFlow<T>> LifecycleOwner.on(
    eventFlow: EF,
    useViewLifeCycle: Boolean = true,
    crossinline body: (T) -> Unit
) {
    eventFlow.flow
        .onEach { body.invoke(it) }
        .observeInLifecycle(if (this is Fragment && useViewLifeCycle) viewLifecycleOwner else this)
}

/**
 * Limits the number of decimals of a Double to the value specified in [decimals]
 */
fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun getCurrencySymbol(): String {
    val locale = Locale.getDefault()
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.currency?.symbol ?: "€"
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.afterFocusLost(afterFocusLost: (View?) -> Unit) {
    this.onFocusChangeListener = OnFocusChangeListener { p0, p1 ->
        if (!p1) {
            afterFocusLost.invoke(p0)
        }
    }
}

fun CharSequence.isValidEmail(): Boolean {
    return Regex(Patterns.EMAIL_ADDRESS.pattern()).containsMatchIn(this)
}

