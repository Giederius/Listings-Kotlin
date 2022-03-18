package com.giedriusmecius.listings.utils.extensions

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController

fun <T> Fragment.setNavigationResult(key: String, value: T) {
    Log.d("MANOSET", "$value")
    findNavController().currentBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T) -> Unit) {
    try {
        Log.d("MANO1", "get1")
        val navBackStackEntry = findNavController().getBackStackEntry(id)

        val observer = LifecycleEventObserver { _, event ->

//            Log.d("MANOEVENT2",event.toString())
//            Log.d("MANOEVENT3",navBackStackEntry.savedStateHandle.get<T>(key).toString())
//            Log.d("MANOEVENT4",navBackStackEntry.savedStateHandle.contains(key).toString())
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(
                    key
                )
            ) {
                Log.d("MANO5", "get2")
                navBackStackEntry.savedStateHandle.get<T>(key)?.let(onResult)
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
        onResult
    }
}