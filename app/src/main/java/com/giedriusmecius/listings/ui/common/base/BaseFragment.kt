package com.giedriusmecius.listings.ui.common.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.R

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    lateinit var binding: VB

    val previousBackStackEntry: Int?
        get() = findNavController().previousBackStackEntry?.destination?.id

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        binding = _binding!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    open fun observeState() {}

    fun navigate(directions: NavDirections) {
        try {
            findNavController().navigate(directions)
        } catch (e: IllegalArgumentException) {
            Log.d("errorArgument", e.localizedMessage)
        } catch (e: IllegalStateException) {
            Log.d("errorState", e.localizedMessage)
        }
    }

    fun navigateUp() {
        findNavController().navigateUp()
    }
}