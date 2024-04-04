package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.OrderEntity
import com.simple.games.tradeassist.ui.base.AppViewModel
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
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
    private val loadedResponsible: MutableList<ResponsibleData> = mutableListOf()
    private val orderGods: MutableList<GodOrderTemplate> = mutableListOf()

    private var selectedCustomer: CustomerData? = null
    private var selectedResponsible: ResponsibleData? = null


    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is CreateOrderUIEvent.OnScreenLoaded -> handleScreenLoaded()
            is CreateOrderUIEvent.OnAddGods -> handleAddGods()
            is CreateOrderUIEvent.OnCustomerNameChange -> handleCustomerNameChange(event.name)
            is CreateOrderUIEvent.OnDismissCustomerDropDown -> handleDismissCustomerDropDown()
            is CreateOrderUIEvent.OnCustomerSelected -> handleCustomerSelected(event.customer)
            is CreateOrderUIEvent.OnResponsibleSelected -> handleResponsibleSelected(event.responsible)
            is CreateOrderUIEvent.OnGodsAdded -> handleGodsAdded(event.gods)
            is CreateOrderUIEvent.OnGodAdded -> handleGodAdded(event.god)
            is CreateOrderUIEvent.OnGodRemoveClicked -> handleGodRemoved(event.god)
            is CreateOrderUIEvent.OnGodEditClick -> handleGodEdit(event.god)
            is CreateOrderUIEvent.SaveOrder -> handleSaveOrder()
        }

        super.onUIEvent(event)
    }

    private fun handleGodAdded(god: GodOrderTemplate) {
        reduce {
            orderTemplates = mutableListOf<GodOrderTemplate>().apply {
                orderGods.forEach {
                    if (it.godEntity.data.refKey == god.godEntity.data.refKey) {
                        add(god)
                    } else {
                        add(it)
                    }
                }
            }
        }
    }

    private fun handleSaveOrder() = launch { state ->
        val customer = selectedCustomer ?: return@launch
        val gods = state.orderTemplates ?: return@launch

        repository.saveOrder(OrderEntity().apply {
            customerKey = customer.refKey
            customerName = customer.description.orEmpty()
            responsibleKey = selectedResponsible?.refKey.orEmpty()
            responsibleName = selectedResponsible?.name.orEmpty()
            this.gods = gods
        }).onSuccess {
            navigate { toBack() }
        }
    }

    private fun handleGodsAdded(gods: List<GodOrderTemplate>) = launch { state ->
        orderGods.addAll(gods)

        reduce {
            orderTemplates = mutableListOf<GodOrderTemplate>().apply {
                orderGods.forEach {
                    add(it)
                }
            }
        }
    }

    private fun handleGodRemoved(god: GodOrderTemplate) = launch {
        orderGods.remove(god)
        reduce {
            orderTemplates = mutableListOf<GodOrderTemplate>().apply {
                orderGods.forEach {
                    add(it)
                }
            }
        }
    }

    private fun handleGodEdit(order: GodOrderTemplate) = launch { state ->
        val customer = selectedCustomer ?: return@launch
        navigate {
            toGodsInfo(customer, order.godEntity, order.amount, order.price)
        }
    }
    private fun handleResponsibleSelected(responsible: ResponsibleData) {
        selectedResponsible = responsible

        reduce {
            responsibleName = responsible.name
        }
    }

    private fun handleCustomerSelected(customer: CustomerData) {
        selectedCustomer = customer

        reduce {
            addGodsEnabled = true
            customerName = customer.description.orEmpty()
            filteredCustomers = emptyList()
        }
    }

    private fun handleDismissCustomerDropDown() {
        reduce {
            filteredCustomers = emptyList()
        }
    }

    private fun handleCustomerNameChange(name: String) {
        val filtered = if (name.isBlank()) {
            emptyList()
        } else {
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
        reduce { requestInProgress = true }

        if (loadedCustomers.isEmpty()) {
            repository.getCustomers().onSuccess {
                loadedCustomers.clear()
                loadedCustomers.addAll(it.sortedBy { it.description?.lowercase() ?: "" })
            }

        }
        if (loadedResponsible.isEmpty()) {
            repository.getResponsible().onSuccess {
                loadedResponsible.clear()
                loadedResponsible.addAll(it)

                selectedResponsible = it.firstOrNull()

                reduce {
                    responsible = it
                }
            }
        }

        reduce { requestInProgress = false }
    }

    private fun handleBackClicked() = launch {
        navigate { toBack() }
    }

    private fun handleAddGods() = launch { state ->
        val customer = selectedCustomer ?: return@launch
        navigate { toGodsSelection(customer) }
    }
}