package com.giedriusmecius.listings.ui.login

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.giedriusmecius.listings.MainActivity
import com.giedriusmecius.listings.const.Constants
import com.giedriusmecius.listings.databinding.FragmentLoginBinding
import com.giedriusmecius.listings.ui.common.base.BaseFragment
import com.giedriusmecius.listings.utils.UserPreferences
import com.giedriusmecius.listings.utils.state.subscribeWithAutoDispose
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val vm by viewModels<LoginViewModel>()
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2

    @Inject
    lateinit var userPrefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        oneTapClient = Identity.getSignInClient(requireContext())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(Constants.GOOGLE_SERVER_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            Log.d("MANO", "${user.email}")
            navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavBar()
        binding.apply {

            loginBtn.setOnClickListener {
                if (loginUserName.text.isNotEmpty() && loginPassword.text.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(
                        loginUserName.text.toString(),
                        loginPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("MANO", "SignInWithCredential: Success")
                                navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                            } else {
                                Log.d("MANO", "SignInWithCredential: FAILED")
                            }
                        }
                }
            }

            signInButton.setOnClickListener {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        try {
                            startIntentSenderForResult(
                                result.pendingIntent.intentSender,
                                REQ_ONE_TAP,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            Log.e("Google Auth", e.localizedMessage)
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Google Auth", it.localizedMessage)
                    }
            }

            signOutBtn.setOnClickListener {
                Firebase.auth.signOut()
            }

            register.setOnClickListener {
                navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val credential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = credential.googleIdToken
        val username = credential.id
        val password = credential.password
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("MANO", "SignInWithCredential: Success")
                                        val user = auth.currentUser
//                                        userPrefs.saveToken(idToken)
                                        navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                                    } else {
                                        Log.d("MANO", "SignInWithCredential: FAILED")
                                    }
                                }

                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d("Google Auth", "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d("Google Auth", "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    // ...
                }
            }
        }
    }

    override fun observeState() {
        vm.subscribeWithAutoDispose(viewLifecycleOwner) { oldState, newState ->
            when (val cmd = newState.command) {

            }
        }
    }
}