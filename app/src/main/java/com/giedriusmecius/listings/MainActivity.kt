package com.giedriusmecius.listings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.giedriusmecius.listings.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullscreenActivity()
        ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
    }

    private fun setFullscreenActivity() {
        @Suppress("DEPRECATION")
        window.decorView.rootView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }
}