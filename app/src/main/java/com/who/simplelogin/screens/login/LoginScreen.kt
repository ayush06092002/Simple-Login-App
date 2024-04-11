package com.who.simplelogin.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.who.simplelogin.R
import com.who.simplelogin.navigation.AppScreens


@Composable
fun LoginScreen(navController: NavController,
                loginViewModel: LoginScreenViewModel = viewModel()) {
    if(FirebaseAuth.getInstance().currentUser?.email != null){
        navController.navigate(AppScreens.Home.name)
    }
    var showLoginForm by rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(showLoginForm){
            UserForm(
                loading = false,
                isCreateAccount = false
            ) { email, password ->
                loginViewModel.signInWithEmailAndPassword(email, password){
                    navController.navigate(AppScreens.Home.name)
                }
            }
        }else{
            UserForm(
                loading = false,
                isCreateAccount = true
            ) { email, password ->
                loginViewModel.createAccount(email, password){
                    navController.navigate(AppScreens.Home.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
        ) {
            val text = if(showLoginForm) "Create Account" else "Login"
            Text(text = text, modifier = Modifier
                .padding(5.dp)
                .clickable {
                    showLoginForm = !showLoginForm
                },
                color = Color.Red
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (email: String, password: String) -> Unit
) {
    Column(
        modifier = Modifier
            .height(300.dp)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isCreateAccount){
            Text(text = stringResource(id = R.string.create_account),
                modifier = Modifier.padding(3.dp),
                color = Color.Red)
        }
        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

        val passwordVisibility = rememberSaveable { mutableStateOf(false) }
        val passwordFocusRequest = FocusRequester.Default
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(email.value, password.value) {
            email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
        }

        EmailInput(
            valueState = email,
            labelId = "Email",
            enabled = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    passwordFocusRequest.requestFocus()
                }
            )
        )

        PasswordInput(
            valueState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            keyboardActions = KeyboardActions {
                if(!valid){
                    keyboardController?.hide()
                }
                onDone(email.value.trim(), password.value.trim())
            },
            passwordFocusRequest = passwordFocusRequest
        )

        SubmitButton(
            text = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(text: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick : () -> Unit) {
    Button(onClick = onClick,
        enabled = !loading && validInputs,
        modifier = Modifier
            .padding(top = 10.dp)
            .width(250.dp)) {
        if(loading){
            CircularProgressIndicator(modifier = Modifier.size(20.dp))
        }else{
            Text(text = text, modifier = Modifier.padding(5.dp),
                color = Color.White)
        }
    }
}

@Composable
fun PasswordInput(
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Default,
    passwordFocusRequest: FocusRequester
) {
    val visualTransformation = if (passwordVisibility.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
    OutlinedTextField(value = valueState.value,
        onValueChange = {
            valueState.value = it
        },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .width(250.dp)
            .focusRequester(passwordFocusRequest),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(
                passwordVisibility = passwordVisibility
            )
        },
        keyboardActions = keyboardActions
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value

    IconButton(onClick = {
        passwordVisibility.value = !visible
    }) {
        Icons.Default.Lock
    }
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next,
) {
    InputField(
        modifier = modifier,
        valueState = valueState,
        labelId = labelId,
        enabled = enabled,
        isSingleLine = true,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onActions = keyboardActions
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(value = valueState.value, onValueChange = {
        valueState.value = it},
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .width(250.dp),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onActions
    )
}
