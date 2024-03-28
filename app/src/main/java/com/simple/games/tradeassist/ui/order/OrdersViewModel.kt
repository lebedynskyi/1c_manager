package com.simple.games.tradeassist.ui.order

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor() : AppViewModel<OrdersViewState>(OrdersViewState()) {
    override val viewStateCopy: OrdersViewState get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClick()

            is OrdersUIEvent.CreateOrder -> handleCreateOrder()
            is OrdersUIEvent.OnDraftsClicked -> handleOnDraftClick()
            is OrdersUIEvent.OnHistoryClicked -> handleOnHistoryClick()
        }
        super.onUIEvent(event)
    }

    private fun handleBackClick() = launch {
        navigate {
            toBack()
        }
    }

    private fun handleCreateOrder() = launch {
        navigate {
            toCreateOrder()
        }
    }

    private fun handleOnDraftClick() = launch {
        reduce {
            selectedTab = 0
            isDrafts = true
            contentInProgress = true
        }

        delay(5000)

        reduce {
            contentInProgress = false
        }
    }

    private fun handleOnHistoryClick() = launch {
        reduce {
            selectedTab = 1
            isDrafts = false
            contentInProgress = true
        }

        delay(5000)

        reduce {
            contentInProgress = false
        }
    }
}