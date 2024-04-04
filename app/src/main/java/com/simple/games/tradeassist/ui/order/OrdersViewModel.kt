package com.simple.games.tradeassist.ui.order

import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.OrderEntity
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: C1Repository
) : AppViewModel<OrdersViewState>(OrdersViewState()) {
    override val viewStateCopy: OrdersViewState get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClick()

            is OrdersUIEvent.OnScreenLoaded -> handleScreenLoaded()
            is OrdersUIEvent.CreateOrder -> handleCreateOrder()
            is OrdersUIEvent.OnDraftsClicked -> handleOnDraftClick()
            is OrdersUIEvent.OnHistoryClicked -> handleOnHistoryClick()
            is OrdersUIEvent.OnPublishClick -> handlePublishClick(event.order)
            is OrdersUIEvent.OnDeleteClick -> handleOnDeleteClick(event.order)
            is OrdersUIEvent.OnEditClick -> {}
        }
        super.onUIEvent(event)
    }

    private fun handlePublishClick(order: OrderEntity) = launch { state ->
        reduce { requestInProgress = true }

        repository.publishOrder(order)

        repository.getOrders(state.isDrafts).onSuccess {
            reduce {
                orders = it
                requestInProgress = false
            }
        }
    }

    private fun handleOnDeleteClick(order: OrderEntity) = launch { state ->
        repository.deleteOrder(order)
        repository.getOrders(state.isDrafts).onSuccess {
            reduce { orders = it }
        }
    }

    private fun handleScreenLoaded() = launch { state ->
        repository.getOrders(state.isDrafts).onSuccess {
            reduce { orders = it }
        }
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

        repository.getOrders(true).onSuccess {
            reduce { orders = it }
        }
    }

    private fun handleOnHistoryClick() = launch {
        reduce {
            selectedTab = 1
            isDrafts = false
            contentInProgress = true
        }

        repository.getOrders(false).onSuccess {
            reduce { orders = it }
        }
    }
}