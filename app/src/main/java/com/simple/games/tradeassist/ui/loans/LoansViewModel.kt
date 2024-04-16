package com.simple.games.tradeassist.ui.loans

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoansViewModel @Inject constructor(

) : AppViewModel<LoansViewState>(LoansViewState(33)) {
    override val viewStateCopy get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            LoansUIEvent.OnScreenLoaded -> handleScreenLoaded()
            AppUIEvent.OnBackClicked -> handleBackClicked()
        }
        super.onUIEvent(event)
    }

    private fun handleBackClicked() = launch {
        navigate { toBack() }
    }

    private fun handleScreenLoaded() {

    }
}