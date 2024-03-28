package com.simple.games.dexter.ui.base

abstract class AppUIEvent {
    override fun toString(): String {
        return this::class.java.name
    }

    companion object OnBackClicked : AppUIEvent()
}
