package com.giedriusmecius.listings.ui.profileDrawers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.DialogProfileDrawersAdjustBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileDrawersAdjustDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogProfileDrawersAdjustBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: OnLayoutSelectedListener

    fun setOnLayoutSelectedListener(listener: OnLayoutSelectedListener) {
        this.listener = listener
    }

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
        with(binding) {
            horizontal.setOnClickListener {
                listener.onLayoutSelected(AdjustDialogActions.HORIZONTAL)
            }
            grid.setOnClickListener {
                listener.onLayoutSelected(AdjustDialogActions.GRID)
            }
            list.setOnClickListener {
                listener.onLayoutSelected(AdjustDialogActions.LIST)
            }
        }
    }

    enum class AdjustDialogActions {
        LAST_SAVED, ALPHABETICALLY, HORIZONTAL, GRID, LIST
    }

    interface OnLayoutSelectedListener {
        fun onLayoutSelected(action: AdjustDialogActions)
    }
}