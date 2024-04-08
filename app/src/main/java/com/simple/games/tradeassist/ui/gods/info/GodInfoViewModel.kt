package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.core.navigation.AppRoute
import com.simple.games.tradeassist.core.round
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.GodEntity
import com.simple.games.tradeassist.ui.base.AppViewModel
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Float.min
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class GodInfoViewModel @Inject constructor(
    private val c1Repository: C1Repository
) : AppViewModel<GodInfoViewState>(GodInfoViewState()) {
    override val viewStateCopy: GodInfoViewState get() = viewState.value.copy()

    private var currentGod: GodEntity? = null

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is GodInfoUIEvent.OnScreenLoaded -> handleScreenLoaded(event.god, event.customerName, event.customerKey, event.price, event.amount)
            is GodInfoUIEvent.OnAddClick -> handleOnAddGods()
            is GodInfoUIEvent.OnAmountChanged -> handleAmountChanged(event.amount)
            is GodInfoUIEvent.OnPriceChanged -> handlePriceChanged(event.price)
        }
        super.onUIEvent(event)
    }

    private fun handleOnAddGods() = launch { state ->
        val amount = state.amountInput?.toFloatOrNull() ?: return@launch
        val price = state.priceInput?.toFloatOrNull() ?: return@launch
        val god = currentGod ?: return@launch

        val orderModel = GodOrderTemplate(god, amount, price)
        navigate {
            toBack(AppRoute.GodsInfoRoute.resultOrder to orderModel)
        }
    }

    private fun handleAmountChanged(amountInput: String) {
        reduce {
            this.amountInput = amountInput
        }

        if (amountInput.isBlank()) {
            reduce {
                amountError = false
                addBtnEnabled = false
            }
            return
        }

        if (amountInput.toFloatOrNull() == null) {
            reduce {
                amountError = true
                addBtnEnabled = false
            }
        } else {
            reduce {
                this.addBtnEnabled = !priceError && priceInput?.isNotBlank() == true
            }
        }
    }

    private fun handlePriceChanged(priceInput: String) {
        reduce {
            this.priceInput = priceInput
        }

        if (priceInput.isBlank()) {
            reduce {
                priceError = false
                addBtnEnabled = false
                priceMarga = 0.00
            }
            return
        }

        val enteredPrice = priceInput.toFloatOrNull()
        if (enteredPrice == null) {
            reduce {
                priceError = true
                addBtnEnabled = false
                priceMarga = 0.00
            }
        } else {
            reduce {
                this.addBtnEnabled = !amountError && amountInput?.isNotBlank() == true

                val price1 = currentGod?.price?.getOrNull(0)?.priceValue
                val price2 = currentGod?.price?.getOrNull(1)?.priceValue
                if (price1 != null && price1 != 0F && price2 !=null && price2 != 0F) {
                    val min = min(price1, price2)
                    priceMarga = ((enteredPrice - min) / (min / 100.0)).round(2)
                }
            }
        }
    }

    private fun handleScreenLoaded(
        god: GodEntity,
        customerName: String?,
        customerKey: String?,
        enteredPrice: Float?,
        amount: Float?
    ) = launch {
        currentGod = god

        reduce {
            historyName = customerName
        }

        c1Repository.getGod(god.data.refKey).onSuccess {
            reduce {
                godsEntity = it
                amount?.let {
                    this.amountInput = it.toString()
                }

                val price1 = god.price.getOrNull(0)?.priceValue
                val price2 = god.price.getOrNull(1)?.priceValue
                if (price1 != null && price1 != 0F && price2 !=null && price2 != 0F) {
                    val max = max(price1, price2)
                    val min = min(price1, price2)

                    if (enteredPrice != null){
                        this.priceInput = enteredPrice.toString()
                        priceMarga = ((enteredPrice - min) / (min / 100.0)).round(2)
                    } else {
                        this.priceInput = max.toString()
                        priceMarga = ((max - min) / (min / 100.0)).round(2)
                    }
                }
            }
        }

        customerKey?.let {
            c1Repository.getOrderHistory(it, god.data.refKey)
                .onSuccess {
                    reduce {
                        orderHistory = if (it.size > 7) it.subList(0, 6) else it
                        historyName = customerName
                    }
                }
        }
    }

    private fun handleBackClicked() {
        navigator.toBack()
    }
}