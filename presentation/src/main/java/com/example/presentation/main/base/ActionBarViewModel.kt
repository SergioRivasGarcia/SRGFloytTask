package com.example.presentation.main.base

import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ActionBarViewModel @Inject constructor() : ViewModel() {

    enum class NavBehaviour {
        NONE, BACK, CLOSE
    }

    var update = MutableStateFlow<View?>(null)

    private var _title = MutableStateFlow<Any?>(null)
    var title: StateFlow<Any?> = _title

    private var _navBehaviour: NavBehaviour = NavBehaviour.BACK
    var navBehaviour: NavBehaviour
        get() {
            return _navBehaviour
        }
        private set(value) {
            _navBehaviour = value
        }

    fun configActionBar(title: Any?, behaviour: NavBehaviour, view: View?) {
        _title.value = title
        _navBehaviour = behaviour
        update.value = view
    }
}
