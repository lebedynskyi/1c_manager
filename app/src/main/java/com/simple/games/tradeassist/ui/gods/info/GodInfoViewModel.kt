package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.core.navigation.AppRoute
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.ui.base.AppViewModel
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GodInfoViewModel @Inject constructor(
    private val c1Repository: C1Repository
) : AppViewModel<GodInfoViewState>(GodInfoViewState()) {
    override val viewStateCopy: GodInfoViewState get() = viewState.value.copy()

    private var currentCustomer: CustomerData? = null
    private var currentGod: GodEntity? = null

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is GodInfoUIEvent.OnScreenLoaded -> handleScreenLoaded(event.customer, event.god, event.price, event.amount)
            is GodInfoUIEvent.OnAddClick -> handleOnAddGods()
            is GodInfoUIEvent.OnAmountChanged -> handleAmountChanged(event.amount)
            is GodInfoUIEvent.OnPriceChanged -> handlePriceChanged(event.price)
        }
        super.onUIEvent(event)
    }

    private fun handleOnAddGods() = launch { state ->
        val amount = state.amount?.toFloatOrNull() ?: return@launch
        val price = state.price?.toFloatOrNull() ?: return@launch
        val customer = currentCustomer ?: return@launch
        val god = currentGod ?: return@launch

        val orderModel = GodOrderTemplate(customer, god, amount, price)
        navigate {
            toBack(AppRoute.GodsInfoRoute.resultOrder to orderModel)
        }
    }

    private fun handleAmountChanged(amountInput: String) {
        if (amountInput.isBlank()) {
            reduce {
                amount = amountInput
                amountError = false
                addBtnEnabled = false
            }
            return
        }

        if (amountInput.toFloatOrNull() == null) {
            reduce {
                amountError = true
                amount = amountInput
                addBtnEnabled = false
            }
        } else {
            reduce {
                this.amount = amountInput
                this.addBtnEnabled = !priceError && price?.isNotBlank() == true
            }
        }
    }

    private fun handlePriceChanged(priceInput: String) {
        if (priceInput.isBlank()) {
            reduce {
                price = priceInput
                priceError = false
                addBtnEnabled = false
            }
            return
        }

        if (priceInput.toFloatOrNull() == null) {
            reduce {
                this.price = priceInput
                priceError = true
                addBtnEnabled = false
            }
        } else {
            reduce {
                this.price = priceInput
                this.addBtnEnabled = !amountError && amount?.isNotBlank() == true
            }
        }
    }

    private fun handleScreenLoaded(
        customer: CustomerData?,
        god: GodEntity,
        price: Float?,
        amount: Float?
    ) = launch {
        currentCustomer = customer
        currentGod = god

        reduce { requestInProgress = true }

        c1Repository.getGod(god.data.refKey).onSuccess {
            reduce {
                godsEntity = it
                price?.let {
                    this.price = it.toString()
                }
                amount?.let {
                    this.amount = it.toString()
                }
            }
        }

        customer?.let {
            c1Repository.getOrderHistory(customer.refKey, god.data.refKey)
                .onSuccess {
                    reduce {
                        orderHistory = if (it.size > 7) it.subList(0, 6) else it
                    }
                }
        }

        reduce { requestInProgress = false }
    }

    private fun handleBackClicked() {
        navigator.toBack()
    }
}