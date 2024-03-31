package com.simple.games.tradeassist.ui.gods

import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.data.api.response.GodsData
import kotlinx.serialization.Serializable

@Serializable
data class GodOrderTemplate(
    val customerData: CustomerData,
    val godsData: GodsData,
    val amount: Float,
    val price: Float,
) : java.io.Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GodOrderTemplate

        if (customerData != other.customerData) return false
        if (godsData != other.godsData) return false
        if (amount != other.amount) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = customerData.hashCode()
        result = 31 * result + godsData.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}