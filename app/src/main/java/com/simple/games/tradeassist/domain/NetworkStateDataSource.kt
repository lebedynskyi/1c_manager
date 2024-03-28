
package com.simple.games.tradeassist.domain

import kotlinx.coroutines.flow.StateFlow

interface NetworkStateDataSource {

    val isNetworkConnectedFlow: StateFlow<Boolean>

    val isNetworkConnected: Boolean

    fun startListeningNetworkState()

    fun stopListeningNetworkState()
}
