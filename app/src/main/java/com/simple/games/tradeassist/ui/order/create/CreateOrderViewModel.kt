package com.simple.games.tradeassist.ui.order.create

import androidx.compose.material3.rememberBottomAppBarState
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

    private lateinit var currentDraft: OrderEntity

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is CreateOrderUIEvent.OnScreenLoaded -> handleScreenLoaded(event.draftId)
            is CreateOrderUIEvent.OnAddGods -> handleAddGods()
            is CreateOrderUIEvent.OnCustomerNameChange -> handleCustomerNameChange(event.name)
            is CreateOrderUIEvent.OnDismissCustomerDropDown -> handleDismissCustomerDropDown()
            is CreateOrderUIEvent.OnCustomerSelected -> handleCustomerSelected(event.customer)
            is CreateOrderUIEvent.OnResponsibleSelected -> handleResponsibleSelected(event.responsible)
//            is CreateOrderUIEvent.OnGodsAdded -> handleGodsAdded(event.gods)
//            is CreateOrderUIEvent.OnGodAdded -> handleGodAdded(event.god)
            is CreateOrderUIEvent.OnGodRemoveClicked -> handleGodRemoved(event.god)
            is CreateOrderUIEvent.OnGodEditClick -> handleGodEdit(event.god)
            is CreateOrderUIEvent.SaveOrder -> handleSaveOrder()
        }

        super.onUIEvent(event)
    }
//
//    private fun handleGodAdded(god: GodOrderTemplate) {
//        reduce {
//            orderTemplates = mutableListOf<GodOrderTemplate>().apply {
//                orderGods.forEach {
//                    if (it.godEntity.data.refKey == god.godEntity.data.refKey) {
//                        add(god)
//                    } else {
//                        add(it)
//                    }
//                }
//            }
//        }
//    }

//
//    private fun handleGodsAdded(gods: List<GodOrderTemplate>) = launch { state ->
//        orderGods.addAll(gods)
//
//        reduce {
//            orderTemplates = mutableListOf<GodOrderTemplate>().apply {
//                orderGods.forEach {
//                    add(it)
//                }
//            }
//        }
//    }

    private fun handleSaveOrder() = launch { state ->
        repository.saveOrder(currentDraft).onSuccess {
            navigate { toBack() }
        }
    }

    private fun handleGodRemoved(god: GodOrderTemplate) = launch {
        currentDraft.gods = currentDraft.gods?.filter { it.godEntity.data.refKey != god.godEntity.data.refKey }
        reduce {
            orderTemplates = currentDraft.gods
        }
    }

    private fun handleGodEdit(godTemplate: GodOrderTemplate) = launch { state ->
        val customer = selectedCustomer ?: return@launch
        navigate {
            toGodsInfo(customer, godTemplate.godEntity, godTemplate.amount, godTemplate.price)
        }
    }

    private fun handleAddGods() = launch { state ->
        val customer = selectedCustomer ?: return@launch
        navigate { toGodsSelection(customer) }
    }

    private fun handleResponsibleSelected(responsible: ResponsibleData) = launch{
        reduce {
            responsibleName = responsible.name
        }

        currentDraft.responsibleKey = responsible.refKey
        currentDraft.responsibleName = responsible.name
        repository.saveOrder(currentDraft)
    }

    private fun handleCustomerSelected(customer: CustomerData) = launch{
        reduce {
            addGodsEnabled = true
            customerName = customer.description.orEmpty()
            filteredCustomers = emptyList()
        }

        currentDraft.customerKey = customer.refKey
        currentDraft.customerName = customer.description
        repository.saveOrder(currentDraft)
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
            addGodsEnabled = false
        }

        launch {
            currentDraft.customerName = null
            currentDraft.customerKey = null

            repository.saveOrder(currentDraft)
        }
    }

    private fun handleScreenLoaded(draftId: Long) = launch {
        reduce { requestInProgress = true }

        currentDraft = repository.getDraft(draftId)

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
            }
        }

        reduce {
            customerName = currentDraft.customerName.orEmpty()
            responsibleName = currentDraft.responsibleName
            orderTemplates = currentDraft.gods
            responsible = loadedResponsible
            requestInProgress = false
            addGodsEnabled = !currentDraft.customerKey.isNullOrBlank()
        }
    }

    private fun handleBackClicked() = launch {
        navigate { toBack() }
    }
}