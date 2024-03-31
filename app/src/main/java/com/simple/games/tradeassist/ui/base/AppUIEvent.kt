package com.simple.games.tradeassist.ui.base

abstract class AppUIEvent {
    override fun toString(): String {
        return this::class.java.name
    }

    companion object OnBackClicked : AppUIEvent()
}
