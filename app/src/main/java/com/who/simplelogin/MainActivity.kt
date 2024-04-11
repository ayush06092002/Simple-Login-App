package com.who.simplelogin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.FirebaseFirestore
import com.who.simplelogin.navigation.AppNavigation
import com.who.simplelogin.ui.theme.SimpleLoginTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLoginTheme {
//                val db = FirebaseFirestore.getInstance()
//                val user: MutableMap<String, Any> = HashMap()
//                user["firstName"] = "Ayush"
//                user["lastName"] = "Sharma"
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    db.collection("users")
//                        .add(user)
//                        .addOnSuccessListener {
//                            Log.d("Firebase", "onCreate: User added successfully ${it.id}")
//                        }.addOnFailureListener{
//                            Log.d("Firebase", "onCreate: User addition failed ${it.message}")
//                        }
                    AppNavigation()
                }
            }
        }
    }
}
