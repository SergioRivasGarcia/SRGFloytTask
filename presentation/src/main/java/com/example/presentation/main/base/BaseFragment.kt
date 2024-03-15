package com.example.presentation.main.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.presentation.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Only to use in fragments that may use all the features. Use [ServiceDelegates] in other cases.
 */
@AndroidEntryPoint
abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes layout: Int) : super(layout)

    protected val actionBarViewModel: ActionBarViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        configActionBarForFragment()
    }

    open fun configActionBarForFragment() {
        // Default condition for the host activities action bar - override in each
        // fragment if back nav or custom title needed
        actionBarViewModel.configActionBar(
            null,
            ActionBarViewModel.NavBehaviour.NONE,
            view
        )
    }

    fun showSnackBar(text: String) {
        view?.let {
            val snackBar = createSnackBar(it, text, Snackbar.LENGTH_SHORT)
            snackBar.show()
        }
    }

    fun getSnackBar(text: String, length: Int): Snackbar? {
        view?.let {
            return createSnackBar(it, text, length)
        } ?: return null
    }

    private fun createSnackBar(
        it: View,
        text: String,
        length: Int
    ): Snackbar {
        val snackBar = Snackbar.make(it, text, length)
        val bottomNavigationView =
            it.rootView.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        snackBar.setAnchorView(bottomNavigationView)
        return snackBar
    }
}