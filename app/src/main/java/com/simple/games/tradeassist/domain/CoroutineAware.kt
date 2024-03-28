package com.simple.games.tradeassist.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CoroutineAware(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    private val coroutineScope = CoroutineScope(coroutineDispatcher)

    protected suspend fun <T> async(block: suspend () -> T) = withContext(coroutineDispatcher) {
        return@withContext block()
    }

    protected fun <T> launch(block: suspend () -> T) = coroutineScope.launch {
        block.invoke()
    }
}
