package com.boosterstestmovis.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boosterstestmovis.R
import com.boosterstestmovis.presentation.activity.MainActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {

    private val _signInState = MutableLiveData<Boolean>()
    val signInState: LiveData<Boolean> get() = _signInState

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val oneTapClient: SignInClient = Identity.getSignInClient(application)
    private val signInRequest: BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(application.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .build()

    init {
        _signInState.value = isUserLoggedIn()
    }

    fun startOneTapSignIn(activity: ComponentActivity) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    activity.startIntentSenderForResult(
                        result.pendingIntent.intentSender, 2, null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Sign-in failed: ${e.localizedMessage}")
                secondWay(activity)
            }
    }

    fun handleOneTapResult(data: Intent?) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                firebaseAuthWithGoogle(idToken)
            } else {
                Log.d(TAG, "No ID token!")
            }
        } catch (e: ApiException) {
            Log.e(TAG, "Sign-in failed: ${e.localizedMessage}")
        }
    }

    private fun secondWay(activity: ComponentActivity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        (activity as MainActivity).signInLauncher.launch(signInIntent)
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.e(TAG, "Sign-in failed: ${e.localizedMessage}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signInState.value = true
                    saveLoginState(true)
                    Log.d(TAG, "Sign-in successful: ${task.result.user?.email}")
                } else {
                    Log.e(TAG, "Sign-in failed: ${task.exception?.localizedMessage}")
                }
            }
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = application.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}

