package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.ui.base.AppViewModel
import com.simple.games.tradeassist.ui.gods.GodOrderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GodInfoViewModel @Inject constructor(
    private val c1Repository: C1Repository
) : AppViewModel<GodInfoViewState>(GodInfoViewState()) {
    override val viewStateCopy: GodInfoViewState get() = viewState.value.copy()

    private var currentCustomer: CustomerData? = null

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is GodInfoUIEvent.OnScreenLoaded -> handleScreenLoaded(event.customer, event.god)
            is GodInfoUIEvent.OnAddClick -> handleOnAddGods()
            is GodInfoUIEvent.OnAmountChanged -> handleAmountChanged(event.amount)
            is GodInfoUIEvent.OnPriceChanged -> handlePriceChanged(event.price)
        }
        super.onUIEvent(event)
    }

    private fun handleOnAddGods() = launch { state ->
        val amount = state.amount?.toFloatOrNull() ?: return@launch
        val price = state.price?.toFloatOrNull() ?: return@launch
        val orderModel = GodOrderModel(amount, price)
        navigate {
            toBack()
        }
    }

    private fun handleAmountChanged(amountInput: String) {
        if (amountInput.isBlank()) {
            reduce {
                amountError = false
                addBtnEnabled = false
            }
            return
        }

        val amount = amountInput.toFloatOrNull()
        if (amount == null) {
            reduce {
                amountError = true
                addBtnEnabled = false
            }
        } else {
            reduce {
                this.amount = amountInput
            }
        }
    }

    private fun handlePriceChanged(priceInput: String) = launch {
        if (priceInput.isBlank()) {
            reduce {
                priceError = false
                addBtnEnabled = false
            }
            return@launch
        }

        val price = priceInput.toFloatOrNull()
        if (price == null) {
            reduce {
                priceError = true
                addBtnEnabled = false
            }
        } else {
            reduce {
                this.price = priceInput
            }
        }
    }

    private fun handleScreenLoaded(customer: CustomerData?, god: GodsData) = launch {
        currentCustomer = customer
        reduce { requestInProgress = true }

        c1Repository.getGod(god.refKey).onSuccess {
            reduce {
                godsData = it
            }
        }

        customer?.let {
            c1Repository.getOrderHistory(customer.refKey, god.refKey)
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