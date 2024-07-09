package com.boosterstestmovis.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boosterstestmovis.domain.entity.FavouriteMovie
import com.boosterstestmovis.domain.entity.Movie
import com.boosterstestmovis.presentation.ui.item.FavoriteList
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
    val movies = model.movieList.collectAsState().value
    if (empty) {
        EmptyListAnim()
    } else {
        MainScreen(movies, model)
    }

}

@Composable
fun MainScreen(
    movies: List<Movie>,
    model: MovieViewModel
) {
    var isSwitchChecked by remember { mutableStateOf(false) }
    val favoriteMovies = model.allFavouriteMovies.collectAsState().value

    Box(Modifier.fillMaxSize()) {
        Column {
            CustomSwitch(
                isChecked = isSwitchChecked,
                onCheckedChange = { isSwitchChecked = it }
            )

            if (isSwitchChecked) {
                FavoriteList(favoriteMovies)
            } else {
                ItemList(movies, onLoadMore = { model.loadingMovies() })
            }
        }
    }
}


@Composable
fun CustomSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .background(if (!isChecked) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable { onCheckedChange(false) }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Movies",
                color = if (!isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable { onCheckedChange(true) }
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Favourites",
                color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun HelloWorldScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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

