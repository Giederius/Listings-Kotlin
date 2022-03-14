package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.DialogProfileFollowingBottomSheetBinding
import com.giedriusmecius.listings.ui.common.groupie.StoreFollowItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupieAdapter

class ProfileFollowingDialogFragment : BottomSheetDialogFragment() {
    private val groupieAdapter = GroupieAdapter()
    private var _binding: DialogProfileFollowingBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogProfileFollowingBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        groupieAdapter.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemList = arrayListOf(
            StoreFollowItem("Zara", "Tag", true),
            StoreFollowItem("HM", "Tag", true),
            StoreFollowItem("Apple", "Tag", true),
            StoreFollowItem("Orange", "Tag", true),
            StoreFollowItem("Trumpet", "Tag", true),
            StoreFollowItem("Nezinau", "Tag", true),
            StoreFollowItem("Ka parasyti", "Tag", true),
            StoreFollowItem("Vitaminas", "Tag", true),
            StoreFollowItem("Vanduo", "Tag", true),
            StoreFollowItem("Toli", "Tag", true),
        )

        binding.profileFollowingDialogRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupieAdapter
        }
        groupieAdapter.addAll(itemList)

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}