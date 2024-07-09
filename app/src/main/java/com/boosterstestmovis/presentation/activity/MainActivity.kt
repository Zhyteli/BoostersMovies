package com.boosterstestmovis.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boosterstestmovis.presentation.ui.item.ItemList
import com.boosterstestmovis.presentation.ui.item.empty.EmptyListAnim
import com.boosterstestmovis.presentation.viewmodel.MovieViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.play.integrity.internal.f
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataFon()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Handle error
                Log.d("TEST_API", e.toString())
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d("TEST_", task.result.user.toString())
                } else {
                    Log.d("TEST_0", task.exception.toString())
                    // Sign in fails
                }
            }
    }
}


@Composable
fun DataFon(model: MovieViewModel = viewModel()) {
    model.startState()

    val empty = model.emptyList.collectAsState().value
    val movies  = model.movieList.collectAsState().value
    if (empty) {
        EmptyListAnim()
    }else{
        ItemList(movies, onLoadMore = { model.loadingMovies() })
        Log.d("TEST_MOVIE", movies.toString())
    }

}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("616608320922-0ej0lbbfl0fdl51q47n2k5bp7c5vhhqq.apps.googleusercontent.com")
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}

fun signInWithGoogle(activity: Activity) {
    val googleSignInClient = getGoogleSignInClient(activity)
    val signInIntent = googleSignInClient.signInIntent
    activity.startActivityForResult(signInIntent, 0)
}

// Handle the result in onActivityResult

