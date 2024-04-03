package com.simple.games.tradeassist.data.api

import com.simple.games.tradeassist.data.api.request.RequestPublishGod
import com.simple.games.tradeassist.data.api.request.RequestPublishOrder
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.data.api.response.StorageRecordData
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

    suspend fun publishOrder(
        customerKey: String,
        responsibleKey: String,
        gods: List<GodOrderTemplate>
    ): Result<EmptyResponse> {
        val currentDate = c1DateFormatter.format(Calendar.getInstance().time)
        val orderSum = gods.map { it.sum }.sum()

        return apiCall {
            c1Api.publishOrder(auth = authKey, RequestPublishOrder(
                customerKey = customerKey,
                orderResponsibleKey = responsibleKey,
                date = currentDate,
                dateOfDelivery = currentDate,
                orderSum = orderSum,
                orderGods = gods.mapIndexed { index, it ->
                    RequestPublishGod(
                        godRefKey = it.godEntity.data.refKey,
                        deliveryDate = currentDate,
                        godsItemsAmount = it.amount,
                        godsPriceSum = it.sum,
                        godsPriceSumAll = it.sum,
                        measureKey = it.godEntity.data.measureKey.orEmpty(),
                        price = it.price,
                        sortNumber = (index + 1).toString()
                    )
                }
            ))
        }
    }

    suspend fun getGods(): Result<List<GodsData>> {
        return apiCall { c1Api.fetchGods(authKey) }.map { it.data }
    }

    suspend fun getResponsible(): Result<List<ResponsibleData>> {
        return apiCall { c1Api.fetchResponsible(authKey) }.map { it.data }
    }

    suspend fun getOrderHistory(customerKey: String): Result<List<OrderHistoryData>> {
        return apiCall {
            c1Api.fetchOrderHistory(
                auth = authKey,
                customerFilter = "Контрагент_Key eq guid'$customerKey'"
            )
        }.map { it.data }
    }


    suspend fun getCustomers(): Result<List<CustomerData>> {
        return apiCall { c1Api.fetchCustomers(authKey) }.map { it.data }
    }

    suspend fun getStorage(): Result<List<StorageRecordData>> {
        return apiCall { c1Api.fetchStorage(authKey) }.map {
            it.data.map { data ->
                data.storageSet.forEach {
                    it.recorderType = data.recorderType
                }

                data.storageSet
            }.flatten()
        }
    }

    suspend fun getMeasure(): Result<List<MeasureData>> {
        return apiCall { c1Api.fetchMeasure(authKey) }.map { it.data }
    }
}