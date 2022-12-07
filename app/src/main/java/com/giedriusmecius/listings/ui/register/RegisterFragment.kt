package com.giedriusmecius.listings.ui.register

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.R
import com.giedriusmecius.listings.databinding.FragmentRegisterBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val vm by viewModels<RegisterViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            signupBtn.setOnClickListener {
                if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
//                                val user = auth.currentUser
                                navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
                            } else {
                                Log.d("MANO", "New user creation failed.")
                                showToast(requireContext(), "Failed to create user.")
                            }
                        }
                } else {
                    signupBtn.startAnimation(
                        AnimationUtils.loadAnimation(
                            context,
                            R.anim.anim_shake
                        )
                    )
                }
            }
        }
    }
}