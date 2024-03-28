package com.simple.games.tradeassist.ui.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.games.dexter.ui.base.AppUIEvent
import com.simple.games.tradeassist.core.navigation.Navigator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class AppViewModel<STATE : AppViewState>(
    defaultState: STATE,
) : ViewModel() {
    private val _viewState = MutableStateFlow(defaultState)
    val viewState: StateFlow<STATE> = _viewState.asStateFlow()

    protected abstract val viewStateCopy: STATE

    @Inject
    protected lateinit var navigator: Navigator

    @CallSuper
    open fun onUIEvent(event: AppUIEvent) {
        System.err.println("On UI Event -> $event")
    }

    protected fun handleError(t: Throwable) = launch {
        System.err.println("XXX: Error occurred -> ${t.message}")

        reduce {
            contentInProgress = false
            refreshInProgress = false
        }
    }

    protected fun launch(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend (STATE) -> Unit,
    ): Job {
        val stateCopy = viewStateCopy
        return viewModelScope.launch(dispatcher) {
            block(stateCopy)
        }
    }

    protected fun reduce(block: STATE.() -> Unit) {
        _viewState.update {
            viewStateCopy.apply(block)
        }
    }

    protected suspend fun navigate(block: suspend Navigator.() -> Unit) {
        withContext(Dispatchers.Main) {
            navigator.block()
        }
    }
}
