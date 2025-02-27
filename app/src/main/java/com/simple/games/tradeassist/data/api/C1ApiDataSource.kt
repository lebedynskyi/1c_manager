package com.simple.games.tradeassist.data.api

import com.simple.games.tradeassist.data.api.request.RequestPublishGod
import com.simple.games.tradeassist.data.api.request.RequestPublishOrder
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.DebtRecordData
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.data.api.response.StorageRecordData
import com.simple.games.tradeassist.domain.chain
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import kotlinx.coroutines.CoroutineDispatcher
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class C1ApiDataSource @Inject constructor(
    private val c1Api: C1Api,
    private val coroutineDispatcher: CoroutineDispatcher
) : RetrofitApiDataSource(coroutineDispatcher) {

    private var authKey: String? = null
    private val c1DateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    fun init(user: String, pass: String) {
        val key = "Basic " + Base64.getEncoder().encodeToString("$user:$pass".toByteArray())
        authKey = key
    }

    suspend fun login(user: String, pass: String): Result<EmptyResponse> {
        init(user, pass)
        return apiCall { c1Api.login(authKey) }.onSuccess {
        }
    }


    suspend fun publishOrder(
        customerKey: String,
        responsibleKey: String,
        gods: List<GodOrderTemplate>,
        comment: String?,
        date: Date
    ): Result<EmptyResponse> {
        val orderSum = gods.map { it.sum }.sum()
        val currentDate = c1DateFormatter.format(date)

        return apiCall {
            c1Api.publishOrder(auth = authKey, RequestPublishOrder(
                customerKey = customerKey,
                orderResponsibleKey = responsibleKey,
                date = currentDate,
                dateOfDelivery = currentDate,
                orderSum = orderSum,
                comment = comment.orEmpty(),
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

    suspend fun getPrices(): Result<List<PriceData>> {
        return apiCall { c1Api.fetchPriceTypes(auth = authKey) }.map { it.data }.chain { types ->
            apiCall { c1Api.fetchPrices(authKey) }.map { it.data }.onSuccess { prices ->
                prices.forEach { price ->
                    price.priceTypeName =
                        types.firstOrNull { it.refKey == price.priceTypeKey }?.description
                }
            }
        }
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

    suspend fun getCustomerDebt(customerKey: String): Result<List<DebtRecordData>> {
        return apiCall {
            c1Api.getCustomerDebt(
                auth = authKey,
                customerFilter = "Контрагент_Key eq guid'$customerKey'"
            )
        }.map { it.data }
    }
}