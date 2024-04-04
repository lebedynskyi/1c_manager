package com.simple.games.tradeassist.domain

import com.simple.games.tradeassist.data.api.C1ApiDataSource
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.data.api.response.StorageRecordData
import com.simple.games.tradeassist.data.db.DataBase
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class C1Repository @Inject constructor(
    private val apiDataSource: C1ApiDataSource,
    private val dataBase: DataBase,
    dispatcher: CoroutineDispatcher
) : CoroutineAware(dispatcher) {
    suspend fun login(user: String, pass: String): Result<EmptyResponse> {
        return apiDataSource.login(user, pass)
    }

    suspend fun hasData(): Boolean {
        return dataBase.customersDao().getCount() > 0 &&
                dataBase.measureDao().getCount() > 0 &&
                dataBase.godsDao().getCount() > 0 &&
                dataBase.storageDao().getCount() > 0 &&
                dataBase.responsibleDao().getCount() > 0
    }

    suspend fun syncData(): Result<Boolean> {
        return fetchCustomers().onSuccess {
            dataBase.customersDao().insertAll(it)
        }.chain { fetchResponsible() }.onSuccess {
            dataBase.responsibleDao().insertAll(it)
        }.chain { fetchMeasure() }.onSuccess {
            dataBase.measureDao().insertAll(it)
        }.chain { fetchStorage() }.onSuccess {
            dataBase.storageDao().insertAll(it)
        }.chain { fetchGods() }.onSuccess {
            dataBase.godsDao().insertAll(it)
        }.map {
            true
        }
    }

    // TODO calculate price? WTF to do
    suspend fun getGods(): Result<List<GodEntity>> {
        val measures = dataBase.measureDao().getAll()
        val storage = dataBase.storageDao().getAll().groupBy { it.godKey }
        val gods = dataBase.godsDao().getAll()

        val result = buildList {
            for (g in gods) {
                val godMeasure = measures.firstOrNull { it.refKey == g.measureKey }
                val godStorage = storage[g.refKey] ?: emptyList()
                add(GodEntity(g, godMeasure, 0F, calculateAmount(godStorage)))
            }
        }

        return Result.success(result)
    }

    suspend fun getGod(refKey: String): Result<GodEntity?> {
        val measures = dataBase.measureDao().getAll()
        val storage = dataBase.storageDao().getAll().groupBy { it.godKey }
        val god = dataBase.godsDao().get(refKey) ?: return Result.success(null)

        val godMeasure = measures.firstOrNull { it.refKey == god.measureKey }
        val godStorage = storage[god.refKey] ?: emptyList()
        val result = GodEntity(god, godMeasure, 0F, calculateAmount(godStorage))

        return Result.success(result)
    }

    suspend fun getCustomers(): Result<List<CustomerData>> {
        return Result.success(dataBase.customersDao().getAll())
    }

    suspend fun getResponsible(): Result<List<ResponsibleData>> {
        return Result.success(dataBase.responsibleDao().getAll())
    }

    suspend fun getOrderHistory(customerKey: String): Result<List<OrderHistoryData>> {
        return apiDataSource.getOrderHistory(customerKey)
    }

    suspend fun getOrderHistory(
        customerKey: String, godKey: String
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

    suspend fun getOrders(isDraft: Boolean): Result<List<OrderEntity>> {
        val orders = if (isDraft) {
            dataBase.ordersDao().getDrafts()
        } else {
            dataBase.ordersDao().getPublished()
        }

        return Result.success(orders)
    }

    suspend fun saveOrder(template: OrderEntity): Result<Unit> {
        dataBase.ordersDao().insertAll(template)
        return Result.success(Unit)
    }

    suspend fun deleteOrder(order: OrderEntity) {
        dataBase.ordersDao().delete(order)
    }

    suspend fun publishOrder(
        order: OrderEntity
    ): Result<EmptyResponse> {
        return apiDataSource.publishOrder(order.customerKey, order.responsibleKey, order.gods).onSuccess {
            order.refKey = "Created"
            dataBase.ordersDao().insertAll(order)
        }
    }

    private suspend fun fetchResponsible(): Result<List<ResponsibleData>> {
        return apiDataSource.getResponsible()
    }

    private suspend fun fetchGods(): Result<List<GodsData>> {
        return apiDataSource.getGods()
    }

    private suspend fun fetchCustomers(): Result<List<CustomerData>> {
        return apiDataSource.getCustomers()
    }

    private suspend fun fetchStorage(): Result<List<StorageRecordData>> {
        return apiDataSource.getStorage()
    }

    private suspend fun fetchMeasure(): Result<List<MeasureData>> {
        return apiDataSource.getMeasure()
    }

    private fun calculateAmount(storageRecordData: List<StorageRecordData>): Float {
        var itemsAmount = 0.0F
        storageRecordData.forEach {
            val amount = it.amount ?: 0F
            when (it.recorderType) {
                "StandardODATA.Document_ОприходованиеЗапасов" -> {
                    itemsAmount += amount
                }

                "StandardODATA.Document_ПриходнаяНакладная" -> {
                    itemsAmount += amount
                }

                "StandardODATA.Document_РасходнаяНакладная" -> {
                    itemsAmount -= amount
                }

                "StandardODATA.Document_СписаниеЗапасов" -> {
                    itemsAmount -= amount
                }
            }
        }

        return itemsAmount
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

enum class SyncStatus() {

}