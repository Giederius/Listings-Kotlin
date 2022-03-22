package com.giedriusmecius.listings.utils.extensions

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController

fun <T> Fragment.setNavigationResult(key: String, value: T) {
    Log.d("MANOSET", "$value")
    Log.d("MANOSET", findNavController().currentBackStackEntry.toString())
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T?) -> Unit) {
    try {
        Log.d("MANO1", "get1")
        val navBackStackEntry = findNavController().getBackStackEntry(id)
        Log.d("MANOBACKSTACK", navBackStackEntry.toString())
        val observer = LifecycleEventObserver { _, event ->

            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(
                    key
                )
            ) {
                Log.d("MANO5", "get2")
                navBackStackEntry.savedStateHandle.get<T>(key).let { onResult(it) }
                navBackStackEntry.savedStateHandle.remove<T>(key)
            }
        }.also {
            navBackStackEntry.lifecycle.addObserver(it)
        }

        Log.d("MANO6", "get3")
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    } catch (e: IllegalArgumentException) {
        Log.d("MANOERROR", e.toString())
        onResult
    }
}