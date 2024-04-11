package com.who.simplelogin.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.who.simplelogin.navigation.AppScreens

@Composable
fun HomeScreen(navController: NavController){
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen")
        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(AppScreens.Login.name)
        }) {
            Text(text = "Logout")
        }
    }
}