package com.simple.games.tradeassist.domain

import com.simple.games.tradeassist.data.api.C1ApiDataSource
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.StorageData
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class C1Repository @Inject constructor(
    private val apiDataSource: C1ApiDataSource,
    private val dispatcher: CoroutineDispatcher
) : CoroutineAware(dispatcher) {
    private val cachedGods: MutableList<GodsData> = mutableListOf()
    private val cachedCustomers: MutableList<CustomerData> = mutableListOf()
    private val cachedStorage: MutableList<StorageData> = mutableListOf()
    private val cachedMeasure: MutableList<MeasureData> = mutableListOf()

    private var godsJob: Job? = null
    private var customerJob: Job? = null
    private var storageJob: Job? = null
    private var measureJob: Job? = null

    suspend fun login(user: String, pass: String): Result<Boolean> {
        return apiDataSource.login(user, pass).map { true }.onSuccess {
            godsJob = launch { syncGods() }
            storageJob = launch { syncStorage() }
            measureJob = launch { syncMeasure() }
            customerJob = launch { syncCustomers() }
        }
    }

    suspend fun getGods(): Result<List<GodsData>> {
        godsJob?.join()
        return syncGods()
    }

    suspend fun getGod(refKey: String): Result<GodsData> {
        godsJob?.join()
        return syncGods().chain {
            val required = it.firstOrNull { it.refKey == refKey }
            if (required == null) {
                Result.failure(IllegalArgumentException())
            } else {
                Result.success(required)
            }
        }
    }

    suspend fun getOrderHistory(customerKey: String): Result<List<OrderHistoryData>> {
        return apiDataSource.getOrderHistory(customerKey).map {
            it.data
        }
    }

    suspend fun getOrderHistory(
        customerKey: String,
        godKey: String
    ): Result<List<Pair<String, GodOrderData>>> {
        return getOrderHistory(customerKey).map {
            buildList {
                it.map {
                    val interestedGods = it.gods?.filter { it.refKey == godKey }
                    if (!interestedGods.isNullOrEmpty()) {
                        add(it.date to interestedGods.first())
                    }
                }
            }.reversed()
        }
    }

    private suspend fun saveOrder(template: GodOrderTemplate) {

    }

    suspend fun publishOrder(
        customerKey: String,
        gods: List<GodOrderTemplate>
    ): Result<EmptyResponse> {
        return apiDataSource.publishOrder(customerKey, gods)
    }

    suspend fun getCustomers(): Result<List<CustomerData>> {
        customerJob?.join()
        return syncCustomers()
    }

    private suspend fun syncGods(): Result<List<GodsData>> {
        if (cachedGods.isNotEmpty()) {
            return Result.success(cachedGods)
        }

        return apiDataSource.getGods()
            .map { it.data }
            .onSuccess {
                System.err.println("START MAP amount")
                cachedGods.clear()
                cachedGods.addAll(it.map { god ->
                    god.amount = calculateAmount(god.refKey)
                    god.measure = calculateMeasure(god.measureKey)
                    god
                })
                System.err.println("Finish MAP amount")
            }
    }

    private suspend fun syncCustomers(): Result<List<CustomerData>> {
        if (cachedCustomers.isNotEmpty()) {
            return Result.success(cachedCustomers)
        }

        return apiDataSource.getCustomers()
            .map { it.data }
            .onSuccess {
                cachedCustomers.clear()
                cachedCustomers.addAll(it)
            }
    }

    private suspend fun syncStorage(): Result<List<StorageData>> {
        return apiDataSource.getStorage()
            .map { it.data }
            .onSuccess {
                cachedStorage.clear()
                cachedStorage.addAll(it)
            }
    }

    private suspend fun syncMeasure(): Result<List<MeasureData>> {
        return apiDataSource.getMeasure()
            .map { it.data }
            .onSuccess {
                cachedMeasure.clear()
                cachedMeasure.addAll(it)
            }
    }

    private suspend fun calculateAmount(refKey: String): Float {
        storageJob?.join()

        var itemsAmount = 0.0F
        cachedStorage.groupBy { it.recorderType }.forEach { key, items ->
            when (key) {
                "StandardODATA.Document_ОприходованиеЗапасов" -> {
                    val sum = items.filter { it.refKey == refKey }.map { it.amount ?: 0.0F }.sum()
                    itemsAmount += sum
                }

                "StandardODATA.Document_ПриходнаяНакладная" -> {
                    val sum = items.filter { it.refKey == refKey }.map { it.amount ?: 0.0F }.sum()
                    itemsAmount += sum
                }

                "StandardODATA.Document_РасходнаяНакладная" -> {
                    val sum = items.filter { it.refKey == refKey }.map { it.amount ?: 0.0F }.sum()
                    itemsAmount -= sum
                }

                "StandardODATA.Document_СписаниеЗапасов" -> {
                    val sum = items.filter { it.refKey == refKey }.map { it.amount ?: 0.0F }.sum()
                    itemsAmount -= sum
                }
            }
        }

        return itemsAmount
    }

    private suspend fun calculateMeasure(refKey: String?): MeasureData? {
        measureJob?.join()
        return cachedMeasure.firstOrNull { it.refKey == refKey }
    }
}

inline fun <R : Result<*>, V> Result<V>.chain(
    onSuccess: (value: V) -> R,
): R {
    return when (val exception = exceptionOrNull()) {
        null -> onSuccess(getOrNull() as V)
        else -> Result.failure<V>(exception) as R
    }
}