package com.simple.games.tradeassist.ui.login

import com.simple.games.dexter.ui.base.AppUIEvent

sealed class LoginUIEvent : AppUIEvent() {
    object OnScreenLoaded : LoginUIEvent()
    data class OnLoginSubmit(val login: String, val pass: String) : LoginUIEvent()
}