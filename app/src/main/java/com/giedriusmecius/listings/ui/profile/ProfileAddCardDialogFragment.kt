package com.giedriusmecius.listings.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.data.remote.model.CC
import com.giedriusmecius.listings.databinding.DialogProfileAddCardBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileAddCardDialogFragment : BottomSheetDialogFragment() {
    private var _binding: DialogProfileAddCardBinding? = null
    private val binding get() = _binding!!
    private val cardTypeCheck = ArrayList<String>()
    private val newCard: CC? = null

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogProfileAddCardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardTypeCheck.add("^4[0-9]{6,}$")
        cardTypeCheck.add("^5[1-5][0-9]{5,}$")

        with(binding) {
            addCardNumberTextEdit.addTextChangedListener {
                Log.d("MANO", it.toString())

            }
            cardNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    Log.d("MANO", p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                    Log.d("MANO", p0.toString())
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val CARD_NUMBER_CHUNKS = 4
    }
}