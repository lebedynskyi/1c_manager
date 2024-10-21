package com.simple.games.tradeassist.ui.order.create

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.entity.OrderEntity
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

            is CreateOrderUIEvent.OnScreenLoaded -> handleScreenLoaded(event.draftId, event.editedGod)
            is CreateOrderUIEvent.OnAddGods -> handleAddGods()
            is CreateOrderUIEvent.OnCustomerNameChange -> handleCustomerNameChange(event.name)
            is CreateOrderUIEvent.OnDismissCustomerDropDown -> handleDismissCustomerDropDown()
            is CreateOrderUIEvent.OnCustomerSelected -> handleCustomerSelected(event.customer)
            is CreateOrderUIEvent.OnResponsibleSelected -> handleResponsibleSelected(event.responsible)
            is CreateOrderUIEvent.OnGodRemoveClicked -> handleGodRemoved(event.god)
            is CreateOrderUIEvent.OnGodEditClick -> handleGodEdit(event.god)
            is CreateOrderUIEvent.SaveOrder -> handleSaveOrder()
        }

        super.onUIEvent(event)
    }

    private fun handleSaveOrder() = launch { state ->
        repository.saveOrder(currentDraft).onSuccess {
            navigate { toBack() }
        }
    }

    private fun handleGodRemoved(god: GodOrderTemplate) = launch {
        currentDraft.gods =
            currentDraft.gods?.filter { it.godEntity.data.refKey != god.godEntity.data.refKey }
        reduce {
            orderTemplates = currentDraft.gods
        }

        repository.saveOrder(currentDraft)
    }

    private fun handleGodEdit(godTemplate: GodOrderTemplate) = launch { state ->
        navigate {
            toGodsInfo(
                godTemplate.godEntity,
                currentDraft.customerName,
                currentDraft.customerKey,
                godTemplate.amount,
                godTemplate.price
            )
        }
    }

    private fun handleAddGods() = launch { state ->
        navigate { toGodsSelection(currentDraft.id) }
    }

    private fun handleResponsibleSelected(responsible: ResponsibleData) = launch {
        reduce {
            responsibleName = responsible.name
        }

        currentDraft.responsibleKey = responsible.refKey
        currentDraft.responsibleName = responsible.name
        repository.saveOrder(currentDraft)
    }

    private fun handleCustomerSelected(customer: CustomerData) = launch {
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
        if (name == viewStateCopy.customerName) {
            System.err.println("XXX: RECURSION !!!")
            return
        }

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

    private fun handleScreenLoaded(draftId: Long, editedGod: GodOrderTemplate?) = launch {
        reduce { requestInProgress = true }
        currentDraft = repository.getDraft(draftId)

        editedGod?.let {
            currentDraft.gods = buildList {
                currentDraft.gods?.forEach {existed ->
                    if (existed.godEntity.data.refKey == editedGod.godEntity.data.refKey) {
                        add(editedGod)
                    } else {
                        add(existed)
                    }
                }
            }

            repository.saveOrder(currentDraft)

            reduce {
                orderTemplates = currentDraft.gods
            }
        }

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