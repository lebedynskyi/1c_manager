package com.simple.games.tradeassist.ui.gods.info

import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.domain.C1Repository
import com.simple.games.tradeassist.domain.chain
import com.simple.games.tradeassist.ui.base.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GodInfoViewModel @Inject constructor(
    private val c1Repository: C1Repository
) : AppViewModel<GodInfoViewState>(GodInfoViewState()) {
    override val viewStateCopy: GodInfoViewState get() = viewState.value.copy()

    override fun onUIEvent(event: AppUIEvent) {
        when (event) {
            is AppUIEvent.OnBackClicked -> handleBackClicked()

            is GodInfoUIEvent.OnScreenLoaded -> handleScreenLoaded(event.customerKey, event.godKey)
        }
        super.onUIEvent(event)
    }

    private fun handleScreenLoaded(customerKey: String?, godKey: String) = launch {
        reduce { requestInProgress = true }

        c1Repository.getGod(godKey).onSuccess {
            reduce {
                godsData = it
            }
        }

        customerKey?.let {
            c1Repository.getOrderHistory(customerKey, godKey)
                .onSuccess {
                    reduce {
                        orderHistory = if (it.size > 7) it.subList(0, 6) else it
                    }
                }
        }

        reduce { requestInProgress = false }
    }

    private fun handleBackClicked() = launch {
        navigate {
            toBack()
        }
    }
}