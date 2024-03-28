package com.simple.games.tradeassist.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.simple.games.tradeassist.domain.NetworkStateDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AndroidNetworkStateDataSource(
    context: Context,
    private val coroutineScope: CoroutineScope
) : NetworkStateDataSource {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _currentNetwork = MutableStateFlow(CurrentNetwork(false, null, false))

    override val isNetworkConnectedFlow: StateFlow<Boolean>
        get() = _currentNetwork
            .map { it.isConnected() }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = _currentNetwork.value.isConnected()
            )

    override val isNetworkConnected: Boolean
        get() = isNetworkConnectedFlow.value

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _currentNetwork.update { it.copy(isAvailable = true) }
        }

        override fun onLost(network: Network) {
            _currentNetwork.update {
                it.copy(
                    isAvailable = false,
                    networkCapabilities = null
                )
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            _currentNetwork.update { it.copy(networkCapabilities = networkCapabilities) }
        }
    }

    init {
        startListeningNetworkState()
    }

    private fun CurrentNetwork.isConnected(): Boolean =
        isListening && isAvailable && networkCapabilities.isNetworkCapabilitiesValid()

    private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean =
        when {
            this == null -> false
            hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            hasTransport(NetworkCapabilities.TRANSPORT_VPN)) -> true

            else -> false
        }

    override fun startListeningNetworkState() {
        if (!_currentNetwork.value.isListening) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
            _currentNetwork.update { it.copy(isListening = true) }
        }
    }

    override fun stopListeningNetworkState() {
        if (_currentNetwork.value.isListening) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
            _currentNetwork.update { it.copy(isListening = false) }
        }
    }

    private data class CurrentNetwork(
        val isListening: Boolean,
        val networkCapabilities: NetworkCapabilities?,
        val isAvailable: Boolean
    )
}
