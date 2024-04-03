package com.simple.games.tradeassist.ui.main

import com.simple.games.tradeassist.ui.base.AppViewState

data class MainViewState(
    override var requestInProgress: Boolean = false
) : AppViewState()