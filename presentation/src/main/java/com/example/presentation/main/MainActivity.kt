package com.example.presentation.main

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.presentation.R
import com.example.presentation.main.base.ActionBarViewModel
import com.example.presentation.main.basket.BasketViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: View
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val actionBarViewModel: ActionBarViewModel by viewModels()
    private val basketViewModel: BasketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.Theme_SRGFloytTask)

        toolbar = findViewById(R.id.toolbar)
        toolbarTitle = toolbar.findViewById(R.id.tv_title)

        // We setup the toolbar with custom settings for more control
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.elevation = 0f

        setupActionBarObservation()
        setupTitleObservation()

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            bottomNavigationView.isVisible =
                !(destination.id == R.id.checkoutFragment || destination.id == R.id.paymentConfirmationFragment)
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_basket,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // This forces dark mode to be always disabled
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        basketViewModel.basketLive.observe(this) {
            val basketItems = it?.sumOf { product -> product.quantity }
            if (basketItems != null && basketItems > 0) {
                bottomNavigationView.getOrCreateBadge(R.id.nav_basket).backgroundColor =
                    ContextCompat.getColor(this, android.R.color.holo_green_dark)
                bottomNavigationView.getOrCreateBadge(R.id.nav_basket).number = basketItems
            } else {
                bottomNavigationView.removeBadge(R.id.nav_basket)
            }
        }

        onBackPressedDispatcher.addCallback(showExitDialogBackPressedCallback)
    }

    private val showExitDialogBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (navController.previousBackStackEntry == null) {
                val builder = MaterialAlertDialogBuilder(this@MainActivity)

                builder.setTitle(getString(R.string.confirm_before_exit_title))
                builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                    this@MainActivity.moveTaskToBack(true)
                    this@MainActivity.finish()
                }
                builder.setNegativeButton(
                    getString(R.string.cancel),
                    null
                )

                val dialog = builder.create()
                dialog.show()
            } else {
                if (!navController.navigateUp()) {
                    if (isEnabled) {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // This provides consistent behaviour between hardware back button and menu up button.
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBarObservation() {
        // React to child fragments that want to specify action bar attributes (via ActionBarViewModel)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                actionBarViewModel.update.collect { _ ->
                    setUpToolbar()
                }
            }
        }
    }

    private fun setUpToolbar() {
        var options = 0
        if (actionBarViewModel.title.value == null) {
            toolbarTitle.isVisible = true
            supportActionBar?.setTitle(0)
        } else {
            options = options or ActionBar.DISPLAY_SHOW_TITLE
            toolbarTitle.isVisible = false
            actionBarViewModel.title.value?.let {
                if (it is Int) {
                    supportActionBar?.setTitle(it)
                } else { // To set string titles, such as string value that requires a variable
                    supportActionBar?.setTitle(it as String)
                }
            }
        }
        if (actionBarViewModel.navBehaviour != ActionBarViewModel.NavBehaviour.NONE) {
            options = options or ActionBar.DISPLAY_HOME_AS_UP
        }
        supportActionBar?.displayOptions = options

        when (actionBarViewModel.navBehaviour) {
            ActionBarViewModel.NavBehaviour.BACK ->
                supportActionBar?.setHomeAsUpIndicator(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_back
                    )
                )

            ActionBarViewModel.NavBehaviour.CLOSE ->
                supportActionBar?.setHomeAsUpIndicator(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_close
                    )
                )

            else -> {
                // No-Op
            }
        }

        toolbar.navigationIcon?.mutate()?.setTint(Color.BLACK)
    }

    private fun setupTitleObservation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                actionBarViewModel.title.collect {
                    setUpToolbar()
                }
            }
        }
    }
}