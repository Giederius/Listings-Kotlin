package com.giedriusmecius.listings.ui.profileDrawers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI.navigateUp
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.DialogProfileDrawersAdjustBinding
import com.giedriusmecius.listings.utils.extensions.setNavigationResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class ProfileDrawersAdjustDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogProfileDrawersAdjustBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogProfileDrawersAdjustBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MANOENTRY1", findNavController().previousBackStackEntry?.destination?.id.toString())
        with(binding) {
            toggleButtonGroup.check(0)
            horizontalLayout.setOnClickListener {
                setResult("AS NESU HORIZONTAL")
//                listener.onLayoutSelected(AdjustDialogActions.HORIZONTAL)
            }
//            gridLayout.setOnClickListener {
//                setResult(AdjustDialogActions.GRID)
////                listener.onLayoutSelected(AdjustDialogActions.GRID)
//            }
//            listLayout.setOnClickListener {
//                setResult(AdjustDialogActions.LIST)
////                listener.onLayoutSelected(AdjustDialogActions.LIST)
//            }
        }
    }

    @Keep
    enum class AdjustDialogActions {
        LAST_SAVED, ALPHABETICALLY, HORIZONTAL, GRID, LIST
    }

    companion object {
        const val RESULT_KEY = "profileDrawersAdjustResultKey"
    }

    private fun setResult(value: String) {
        setNavigationResult(RESULT_KEY, value)
        dismiss()
    }
}