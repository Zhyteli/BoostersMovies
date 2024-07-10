package com.boosterstestmovis.presentation.auth

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.boosterstestmovis.presentation.ui.Switcher
import com.boosterstestmovis.presentation.viewmodel.AuthViewModel

@Composable
fun Auth(authViewModel: AuthViewModel, activity: ComponentActivity) {
    val signInState by authViewModel.signInState.observeAsState(false)

    MaterialTheme {
        if (signInState) {
            Switcher()
        } else {
            Box(Modifier.fillMaxSize()) {
                Button(modifier = Modifier.align(Alignment.Center),onClick = {
                    authViewModel.startOneTapSignIn(activity)
                }) {
                    Text("Sign In with Google")
                }
            }
        }
    }
}