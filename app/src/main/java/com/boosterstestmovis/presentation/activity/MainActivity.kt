package com.boosterstestmovis.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boosterstestmovis.domain.entity.Movie
import com.boosterstestmovis.presentation.auth.Auth
import com.boosterstestmovis.presentation.ui.item.empty.EmptyListAnim
import com.boosterstestmovis.presentation.ui.item.list.FavoriteList
import com.boosterstestmovis.presentation.ui.item.list.ItemList
import com.boosterstestmovis.presentation.ui.switcher.CustomSwitch
import com.boosterstestmovis.presentation.viewmodel.AuthViewModel
import com.boosterstestmovis.presentation.viewmodel.MovieViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        authViewModel.handleSignInResult(task)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Auth(authViewModel, this)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
            authViewModel.handleOneTapResult(data)
        }
    }
}