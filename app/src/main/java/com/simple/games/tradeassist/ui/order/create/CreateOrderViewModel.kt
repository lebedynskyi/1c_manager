package com.simple.games.tradeassist.ui.order.create

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val repository: C1Repository
) : AppViewModel<CreateOrderViewState>(
    CreateOrderViewState()
) {
    override val viewStateCopy: CreateOrderViewState get() = viewState.value.copy()

    private val loadedCustomers: MutableList<CustomerData> = mutableListOf()
    private val addedGods: MutableList<GodsData> = mutableListOf()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is CreateOrderUIEvent.OnScreenLoaded -> handleScreenLoaded()
            is CreateOrderUIEvent.OnAddGods -> handleAddGods()
            is CreateOrderUIEvent.OnCustomerNameChange -> handleCustomerNameChange(event.name)
            is CreateOrderUIEvent.OnDismissCustomerDropDown -> handleDismissCustomerDropDown()
            is CreateOrderUIEvent.OnCustomerSelected -> handleCustomerSelected(event.customer)
//            is CreateOrderUIEvent.OnGodSelected -> handleGodAdded(event.resultGodKey)
        }

        super.onUIEvent(event)
    }

    private fun handleOrderAdded(godRefKey: String) = launch { state ->
        val customer = state.selectedCustomer?: return@launch

        navigate {
//            toGodsInfo(customer, godRefKey)
        }
    }

    private fun handleCustomerSelected(customer: CustomerData) {
        reduce {
            selectedCustomer = customer
            addGodsEnabled = true
            customerName = customer.description.orEmpty()
            filteredCustomers = emptyList()
        }
    }

    private fun handleDismissCustomerDropDown(){
        reduce {
            filteredCustomers = emptyList()
        }
    }

    private fun handleCustomerNameChange(name: String) {
        val filtered = if (name.isBlank()) {
             emptyList()
        }else {
            loadedCustomers.filter { it.description?.contains(name, true) == true }
        }
        reduce {
            customerName = name
            filteredCustomers = filtered.take(8)
            selectedCustomer = null
            addGodsEnabled = false
        }
    }

    private fun handleScreenLoaded() = launch {
        if (loadedCustomers.isNotEmpty()) {
            return@launch
        }

        reduce { requestInProgress = true }

        repository.getCustomers().onSuccess {
            loadedCustomers.clear()
            loadedCustomers.addAll(it.sortedBy { it.description?.lowercase() ?: "" })
        }

        reduce { requestInProgress = false }
    }

    private fun handleBackClicked() = launch {
        navigate { toBack() }
    }

    private fun handleAddGods() = launch {state ->
        val customerKey = state.selectedCustomer?.refKey ?: return@launch
        navigate { toGodsSelection(customerKey) }
    }
}