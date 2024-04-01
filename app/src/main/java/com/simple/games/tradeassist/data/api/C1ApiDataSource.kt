package com.simple.games.tradeassist.data.api

import com.simple.games.tradeassist.data.api.request.RequestPublishGod
import com.simple.games.tradeassist.data.api.request.RequestPublishOrder
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.DataResponse
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.StorageData
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import kotlinx.coroutines.CoroutineDispatcher
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class C1ApiDataSource @Inject constructor(
    private val c1Api: C1Api,
    private val coroutineDispatcher: CoroutineDispatcher
) : RetrofitApiDataSource(coroutineDispatcher) {

    private var authKey: String? = null
    private val c1DateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())


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
        return apiCall {
            c1Api.fetchOrderHistory(
                auth = authKey,
                customerFilter = "Контрагент_Key eq guid'$customerKey'"
            )
        }
    }

    suspend fun publishOrder(
        customerKey: String,
        gods: List<GodOrderTemplate>
    ): Result<EmptyResponse> {
        val currentDate = c1DateFormatter.format(Calendar.getInstance().time)
        val orderSum = gods.map { it.sum }.sum()

        return apiCall {
            c1Api.publishOrder(auth = authKey, RequestPublishOrder(
                customerKey = customerKey,
                date = currentDate,
                dateOfDelivery = currentDate,
                orderSum = orderSum,
                orderGods = gods.mapIndexed { index, it ->
                    RequestPublishGod(
                        godRefKey = it.godsData.refKey,
                        deliveryDate = currentDate,
                        godsItemsAmount = it.amount,
                        godsPriceSum = it.sum,
                        godsPriceSumAll = it.sum,
                        measureKey = it.godsData.measureKey.orEmpty(),
                        price = it.price,
                        sortNumber = (index + 1).toString()
                    )
                }
            ))
        }
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