package com.simple.games.tradeassist.ui.main

import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: C1Repository
) : AppViewModel<MainViewState>(
    MainViewState()
) {
    override val viewStateCopy: MainViewState get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            MainUIEvent.OnScreenLoaded -> handleScreenLoaded()
            MainUIEvent.OnOrderClick -> handleOrdersClick()
            MainUIEvent.OnGodsClick -> handleOnGodsClick()
            MainUIEvent.OnSyncClicked -> handleSyncClicked()
            MainUIEvent.OnLoansClicked -> handleLoansClicked()
        }
        super.onUIEvent(event)
    }
    
    private fun handleLoansClicked() = launch {
        navigate { toLoans() }
    }

    private fun handleScreenLoaded() = launch {
        if (!repository.hasData()) {
            reduce { requestInProgress = true }
            repository.syncData()
            reduce {
                requestInProgress = false
            }
        } else {
            repository.syncStorage()
        }
    }

    private fun handleSyncClicked() = launch {
        reduce { requestInProgress = true }
        repository.syncData()
        reduce { requestInProgress = false }
    }

    private fun handleOrdersClick() = launch {
        navigate {
            toOrders()
        }
    }

    private fun handleOnGodsClick() = launch {
        navigate {
            toGodsSelection()
        }
    }
}