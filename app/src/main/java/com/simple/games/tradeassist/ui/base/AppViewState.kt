package com.simple.games.tradeassist.ui.base

abstract class AppViewState {
    open var contentInProgress: Boolean = false
    open var refreshInProgress: Boolean = false
    open var requestInProgress: Boolean = false
}
