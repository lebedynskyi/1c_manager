package com.simple.games.tradeassist.ui.login

import com.simple.games.tradeassist.ui.base.AppViewState

data class LoginViewState(
    override var requestInProgress: Boolean = false,
    var loginError: Boolean = false
) : AppViewState()