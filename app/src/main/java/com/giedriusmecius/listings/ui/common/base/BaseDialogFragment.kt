package com.giedriusmecius.listings.ui.common.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.giedriusmecius.listings.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialogFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) :
    BottomSheetDialogFragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onStart() {
        super.onStart()
//        val bottomSheet = dialog?.window?.findViewById<View>(R.id.design_bottom_sheet)
//        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        dialog?.window?.setSoftInputMode(5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun observeState() {}

}