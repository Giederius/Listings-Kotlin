package com.giedriusmecius.listings.utils.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController

fun <T> Fragment.setNavigationResult(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(
        key,
        value
    )
}

fun <T> Fragment.getNavigationResult(@IdRes id: Int, key: String, onResult: (result: T) -> Unit) {
    try {
        val navBackStackEntry = findNavController().getBackStackEntry(id)

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains(
                    key
                )
            ) {
                navBackStackEntry.savedStateHandle.get<T>(key)?.let(onResult)
                navBackStackEntry.savedStateHandle.remove<T>(key)
            }
        }.also {
            navBackStackEntry.lifecycle.addObserver(it)
        }

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    } catch (e: Exception) {
    }
}