package com.giedriusmecius.listings.ui.product

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.databinding.DialogProductFragmentBinding
import com.giedriusmecius.listings.ui.common.base.BaseDialogFragment
import com.giedriusmecius.listings.ui.common.groupie.ProductImageItem
import com.giedriusmecius.listings.utils.extensions.toCurrency
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xwray.groupie.GroupieAdapter

class ProductBottomSheetDialog :
    BaseDialogFragment<DialogProductFragmentBinding>(DialogProductFragmentBinding::inflate) {
    private val args by navArgs<ProductBottomSheetDialogArgs>()
    private val groupie = GroupieAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.behavior.skipCollapsed = true
        dialog.behavior.peekHeight = resources.displayMetrics.heightPixels
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.isDraggable = false
        return dialog
    }

    override fun onStart() {
        super.onStart()
        binding.root.layoutParams.height = resources.displayMetrics.heightPixels
        binding.root.requestLayout()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            close.setOnClickListener { dismiss() }

            if (args.product != null) {
                productTitle.text = args.product!!.title ?: ""
                productPrice.text = args.product!!.price.toCurrency() ?: ""

                productRV.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = groupie
                }

                val imageList = listOf(
                    ProductImageItem(args.product!!.image, true),
                    ProductImageItem(args.product!!.image, false),
                    ProductImageItem(args.product!!.image, false)
                )

                groupie.addAll(imageList)

                colorBtn.setColorBtn("#800000", "Maroon")
                colorBtn.isEnabled(false)
                addtoCartBtn.setupCTAButton()
            }
        }
    }
}