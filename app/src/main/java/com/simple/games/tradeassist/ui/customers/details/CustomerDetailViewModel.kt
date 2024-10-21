package com.simple.games.tradeassist.ui.customers.details

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerDetailViewModel @Inject constructor(
    private val c1Repository: C1Repository
) : AppViewModel<CustomerDetailViewState>(
    CustomerDetailViewState()
) {
    override val viewStateCopy: CustomerDetailViewState get() = viewState.value.copy()


    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            AppUIEvent.OnBackClicked -> handleBackClicked()
            is CustomerDetailUIEvent.ScreenLoaded -> handleScreenLoaded(event.customerData)
        }
        super.onUIEvent(event)
    }

    private fun handleScreenLoaded(data: CustomerData) = launch {

        reduce {
            contentInProgress = true
            this.customerData = data
        }

        c1Repository.getCustomerDebt(data.refKey).onSuccess {
            reduce {
                contentInProgress = false
                this.debtEntity = it
            }
        }
    }

    private fun handleBackClicked() = launch {
        navigate { toBack() }
    }
}