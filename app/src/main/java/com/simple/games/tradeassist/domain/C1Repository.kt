package com.simple.games.tradeassist.domain

import androidx.room.withTransaction
import com.simple.games.tradeassist.data.PrefDataSource
import com.simple.games.tradeassist.data.api.C1ApiDataSource
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.DebtRecordData
import com.simple.games.tradeassist.data.api.response.EmptyResponse
import com.simple.games.tradeassist.data.api.response.MeasureData
import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.data.api.response.OrderHistoryData
import com.simple.games.tradeassist.data.api.response.PriceData
import com.simple.games.tradeassist.data.api.response.ResponsibleData
import com.simple.games.tradeassist.data.api.response.StorageRecordData
import com.simple.games.tradeassist.data.db.DataBase
import com.simple.games.tradeassist.domain.entity.CustomerDebtEntity
import com.simple.games.tradeassist.domain.entity.GodEntity
import com.simple.games.tradeassist.domain.entity.OrderEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class C1Repository @Inject constructor(
    private val apiDataSource: C1ApiDataSource,
    private val dataBase: DataBase,
    private val preference: PrefDataSource,
    dispatcher: CoroutineDispatcher
) : CoroutineAware(dispatcher) {

    fun initialize(): Boolean {
        val login = preference.getUserName() ?: return false
        val password = preference.getPassword() ?: return false
        apiDataSource.init(login, password)

        return true
    }

    suspend fun login(user: String, pass: String): Result<Boolean> {
        return apiDataSource.login(user, pass).map { true }.onSuccess {
            preference.saveUserName(user)
            preference.savePassword(pass)
        }
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
            dataBase.withTransaction {
                dataBase.customersDao().deleteAll()
                dataBase.customersDao().insertAll(it)
            }
        }.chain { fetchResponsible() }.onSuccess {
            dataBase.withTransaction {
                dataBase.responsibleDao().deleteAll()
                dataBase.responsibleDao().insertAll(it)
            }
        }.chain { fetchMeasure() }.onSuccess {
            dataBase.withTransaction {
                dataBase.measureDao().deleteAll()
                dataBase.measureDao().insertAll(it)
            }
        }.chain { fetchStorage() }.onSuccess {
            dataBase.withTransaction {
                dataBase.storageDao().deleteAll()
                dataBase.storageDao().insertAll(it)
            }
        }.chain { fetchGods() }.onSuccess {
            dataBase.withTransaction {
                dataBase.godsDao().deleteAll()
                dataBase.godsDao().insertAll(it)
            }
        }.chain { fetchPrices() }.onSuccess {
            dataBase.withTransaction {
                dataBase.priceDao().deleteAll()
                dataBase.priceDao().insertAll(it)
            }
        }.map {
            true
        }
    }

    suspend fun syncStorage() {
        fetchStorage().onSuccess {
            dataBase.withTransaction {
                dataBase.storageDao().deleteAll()
                dataBase.storageDao().insertAll(it)
            }
        }
    }

    suspend fun getGods(): Result<List<GodEntity>> {
        val storage = dataBase.storageDao().getAll().groupBy { it.godKey }
        val prices = dataBase.priceDao().getAll().groupBy { it.godKey }
        val measures = dataBase.measureDao().getAll()
        val gods = dataBase.godsDao().getAll()

        val result = buildList {
            for (g in gods) {
                val godMeasure = measures.firstOrNull { it.refKey == g.measureKey }
                val godStorage = storage[g.refKey] ?: emptyList()
                val godPrices = prices[g.refKey] ?: emptyList()
                add(
                    GodEntity(
                        g,
                        godMeasure,
                        calculatePrice(godPrices),
                        calculateAmount(godStorage)
                    )
                )
            }
        }

        return Result.success(result)
    }

    suspend fun getGod(refKey: String): Result<GodEntity?> {
        val measures = dataBase.measureDao().getAll()
        val storage = dataBase.storageDao().getAll().groupBy { it.godKey }
        val god = dataBase.godsDao().get(refKey) ?: return Result.success(null)
        val prices = dataBase.priceDao().getAll().groupBy { it.godKey }

        val godMeasure = measures.firstOrNull { it.refKey == god.measureKey }
        val godStorage = storage[god.refKey] ?: emptyList()
        val godPrices = prices[god.refKey] ?: emptyList()
        val result =
            GodEntity(god, godMeasure, calculatePrice(godPrices), calculateAmount(godStorage))

        return Result.success(result)
    }

    suspend fun getCustomers(): Result<List<CustomerData>> {
        return Result.success(dataBase.customersDao().getAll())
    }

    suspend fun getResponsible(): Result<List<ResponsibleData>> {
        return Result.success(dataBase.responsibleDao().getAll())
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

    suspend fun getOrderHistory(customerKey: String): Result<List<OrderHistoryData>> {
        return apiDataSource.getOrderHistory(customerKey)
    }

    suspend fun getOrders(isDraft: Boolean): Result<List<OrderEntity>> {
        val orders = if (isDraft) {
            dataBase.ordersDao().getDrafts()
        } else {
            dataBase.ordersDao().getPublished()
        }

        return Result.success(orders)
    }

    suspend fun saveOrder(template: OrderEntity): Result<Long> {
        return Result.success(dataBase.ordersDao().insert(template))
    }

    suspend fun deleteOrder(order: OrderEntity) {
        dataBase.ordersDao().delete(order)
    }

    suspend fun getDraft(draftId: Long): OrderEntity {
        return dataBase.ordersDao().getDraft(draftId)
    }

    suspend fun publishOrder(
        order: OrderEntity
    ): Result<EmptyResponse> {
        val customer =
            order.customerKey ?: return Result.failure(IllegalArgumentException("Customer is null"))
        val responsible = order.responsibleKey
            ?: return Result.failure(IllegalArgumentException("Responsible is null"))
        val gods = order.gods ?: return Result.failure(IllegalArgumentException("Gods is null"))

        return apiDataSource.publishOrder(customer, responsible, gods)
            .onSuccess {
                order.refKey = "TODO"
                dataBase.ordersDao().insert(order)
            }
    }

    private suspend fun fetchResponsible(): Result<List<ResponsibleData>> {
        return apiDataSource.getResponsible()
    }

    private suspend fun fetchPrices(): Result<List<PriceData>> {
        return apiDataSource.getPrices()
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

    private fun calculatePrice(godPrices: List<PriceData>): List<PriceData> {
        return godPrices.sortedByDescending { it.date }.distinctBy { it.priceTypeKey }
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

    suspend fun getCustomerDebt(customerKey: String): Result<CustomerDebtEntity> {
        return apiDataSource.getCustomerDebt(customerKey).map {
            val plus = it.filter { it.recordType == "Receipt" }
            val minus = it.filter { it.recordType == "Expense" }
            val totalPlus = plus.sumOf { it.amount }
            val totalMinus = minus.sumOf { it.amount }
            CustomerDebtEntity(customerKey, plus, minus, totalPlus, totalMinus)
        }
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