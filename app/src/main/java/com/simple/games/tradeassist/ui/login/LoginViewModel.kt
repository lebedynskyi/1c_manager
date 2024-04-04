package com.simple.games.tradeassist.ui.login

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val c1Repository: C1Repository
) : AppViewModel<LoginViewState>(LoginViewState()) {
    override val viewStateCopy get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is LoginUIEvent.OnScreenLoaded -> handleScreenLoaded()
            is LoginUIEvent.OnLoginSubmit -> handleLoginSubmitted(event.login, event.pass)
        }

        super.onUIEvent(event)
    }

    private fun handleScreenLoaded() = launch {
        handleLoginSubmitted("стас", "1989")
    }

    private fun handleLoginSubmitted(login: String, pass: String) = launch {
        System.err.println("Login -> $login, pass -> $pass")
        reduce {
            requestInProgress = true
            loginError = false
        }

        c1Repository.login(login, pass).onSuccess {
            navigate {
                toMain()
            }
        }.onFailure {
            reduce {
                loginError = true
            }
        }

        reduce { requestInProgress = false }
    }
}