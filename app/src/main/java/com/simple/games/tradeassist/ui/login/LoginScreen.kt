package com.simple.games.tradeassist.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.R
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import com.simple.games.tradeassist.ui.base.design.ContentLoadingIndicator

@Composable
fun LoginScreen(
    state: LoginViewState,
    onUIEvent: (AppUIEvent) -> Unit
) {
    LoginScreenContent(
        state.loginError
    ) { login, pass ->
        onUIEvent(LoginUIEvent.OnLoginSubmit(login, pass))
    }

    ContentLoadingIndicator(state.requestInProgress)
}

@Composable
private fun LoginScreenContent(
    loginError: Boolean,
    onLoginClick: (String, String) -> Unit
) {
    Scaffold {
        Box(modifier = Modifier.padding(it)) {
            LoginForm(
                loginError,
                onLoginClick = onLoginClick
            )
        }
    }
}

@Composable
private fun LoginForm(
    loginError: Boolean,
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var passsword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(50.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.authorization),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.size(36.dp))
        OutlinedTextField(
            isError = loginError,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            placeholder = {
                Text(text = stringResource(id = R.string.login))
            },
            value = login,
            onValueChange = {
                login = it
            }
        )

        Spacer(modifier = Modifier.size(12.dp))
        OutlinedTextField(
            isError = loginError,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            placeholder = {
                Text(text = stringResource(id = R.string.password))
            },
            value = passsword,
            onValueChange = {
                passsword = it
            },
        )
        Spacer(modifier = Modifier.size(24.dp))
        Button(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            onClick = { onLoginClick(login, passsword) },
        ) {
            Text(text = stringResource(R.string.log_in))
        }
    }
}


@Preview
@Composable
fun PreviewLogin() {
    TradeAssistTheme {
        LoginScreen(state = LoginViewState(requestInProgress = true)) {

        }
    }
}
