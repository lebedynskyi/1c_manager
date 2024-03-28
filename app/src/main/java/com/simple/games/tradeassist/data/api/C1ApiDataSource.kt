package com.simple.games.tradeassist.data.api

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.DataResponse
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.StorageData
import kotlinx.coroutines.CoroutineDispatcher
import java.util.Base64
import javax.inject.Inject

class C1ApiDataSource @Inject constructor(
    private val c1Api: C1Api,
    private val coroutineDispatcher: CoroutineDispatcher
) : RetrofitApiDataSource(coroutineDispatcher) {

    private var authKey: String? = null

    suspend fun login(user: String, pass: String): Result<EmptyResponse> {
        val key = "Basic " + Base64.getEncoder().encodeToString("$user:$pass".toByteArray())
        return apiCall { c1Api.login(key) }.onSuccess {
            authKey = key
        }
    }

    suspend fun getGods(): Result<DataResponse<GodsData>> {
        return apiCall { c1Api.fetchGods(authKey) }
    }

    suspend fun getOrderHistory(customerKey: String): Result<DataResponse<OrderHistoryData>> {
        return apiCall { c1Api.fetchOrderHistory(auth = authKey, customerFilter = "Контрагент_Key eq guid'$customerKey'") }
    }

    suspend fun getCustomers(): Result<DataResponse<CustomerData>> {
        return apiCall { c1Api.fetchCustomers(authKey) }
    }

    suspend fun getStorage(): Result<DataResponse<StorageData>> {
        return apiCall { c1Api.fetchStorage(authKey) }
    }

    suspend fun getMeasure(): Result<DataResponse<MeasureData>> {
        return apiCall { c1Api.fetchMeasure(authKey) }
    }
}