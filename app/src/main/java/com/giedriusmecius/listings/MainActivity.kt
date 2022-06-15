package com.giedriusmecius.listings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.giedriusmecius.listings.databinding.ActivityMainBinding
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setFullscreenActivity()
        observeState()
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBottomNav.setupWithNavController(navController)
    }

    private fun setFullscreenActivity() {
//        @Suppress("DEPRECATION")
//        window.decorView.rootView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    private fun observeState() {
        vm.subscribeWithAutoDispose(this) { _, newState ->
            when (val cmd = newState.command) {

                else -> {}
            }
        }
    }

    fun hideBottomNavBar() {
        binding.mainBottomNav.isGone = true
    }

    fun showBottomNavBar() {
        binding.mainBottomNav.isGone = false
    }
}