package com.simple.games.tradeassist.ui.customers.list

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val repository: C1Repository
) : AppViewModel<CustomersViewState>(CustomersViewState(emptyList())) {
    override val viewStateCopy get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            CustomersUIEvent.OnScreenLoaded -> handleScreenLoaded()
            AppUIEvent.OnBackClicked -> handleBackClicked()
            is CustomersUIEvent.OnCustomerClicked -> handleCustomerClicked(event.customer)
        }
        super.onUIEvent(event)
    }

    private fun handleCustomerClicked(customerData: CustomerData) = launch {
        navigate { toCustomerDetails(customerData) }
    }

    private fun handleBackClicked() = launch {
        navigate { toBack() }
    }

    private fun handleScreenLoaded() = launch {
        repository.getCustomers().onSuccess {
            reduce {
                customers = it.sortedBy { it.description }
            }
        }
    }
}