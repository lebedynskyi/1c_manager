package com.simple.games.tradeassist.ui.main

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : AppViewModel<MainViewState>(
    MainViewState(1)
) {
    override val viewStateCopy: MainViewState get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when(event){
            MainUIEvent.OnOrderClick -> handleOrdersClick()
            MainUIEvent.OnGodsClick -> handleOnGodsClick()
        }
        super.onUIEvent(event)
    }

    private fun handleOrdersClick() = launch {
        navigate {
            toOrders()
        }
    }

    private fun handleOnGodsClick() = launch {
        navigate {
            toGods()
        }
    }
}