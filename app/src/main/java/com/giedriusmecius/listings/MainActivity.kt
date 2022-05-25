package com.giedriusmecius.listings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.giedriusmecius.listings.databinding.ActivityMainBinding
import com.giedriusmecius.listings.ui.home.HomeFragment
import com.giedriusmecius.listings.ui.market.MarketFragment
import com.giedriusmecius.listings.ui.profile.ProfileFragment
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
        setupBottomNav()
    }

    private fun setupBottomNav() {
        var bottomNav = binding.mainBottomNav

        // todo issiaiskinti kaip pasiimt visible fragmenta kad nuselectint mygtuka

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainBottomNavFeed -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.mainBottomNavMarket -> {
                    openFragment(MarketFragment())
                    true
                }
                R.id.mainBottomNavProfile -> {
                    openFragment(ProfileFragment())
                    true
                }
                else -> {
                    false
                }
            }

        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
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
}