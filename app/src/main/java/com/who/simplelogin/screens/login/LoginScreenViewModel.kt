package com.who.simplelogin.screens.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.who.simplelogin.models.MUser


class LoginScreenViewModel: ViewModel() {
//    val loadingState = mutableStateOf(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(email: String, password: String, navigateToHome: () -> Unit){
        try{
            if(_loading.value == false){
                _loading.value = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(
                                "LoginScreenViewModel",
                                "signInWithEmailAndPassword: success ${task.result}"
                            )
                            navigateToHome()
                        } else {
                            Log.d(
                                "LoginScreenViewModel",
                                "signInWithEmailAndPassword: failed ${task.result}"
                            )
                        }
                    }
            }
        }catch (e: Exception){
            Log.d("LoginScreenViewModel", "signInWithEmailAndPassword: ${e.message}")
        }
    }

    fun createAccount(email: String, password: String, navigateToHome: () -> Unit){
        try{
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.substringBefore("@")
                        createUser(displayName)
                        navigateToHome()
                    } else {
                        Log.d("LoginScreenViewModel", "createAccount: failed ${task.result}")
                    }
                }
        }catch (e: Exception){
            Log.d("LoginScreenViewModel", "createAccount: ${e.message}")
        }
    }

    private fun createUser(displayName: String?) {
        val id = auth.currentUser?.uid
        val user = MUser(id.toString(), displayName.toString(), "Life is Good", "Android Developer").toMap()


        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}
