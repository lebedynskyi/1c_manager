package com.simple.games.tradeassist.ui.gods

import com.simple.games.tradeassist.domain.GodEntity
import kotlinx.serialization.Serializable

@Serializable
data class GodOrderTemplate(
    val godEntity: GodEntity,
    val amount: Float,
    val price: Float,
    val sum: Float = price * amount
) : java.io.Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GodOrderTemplate

        if (godEntity != other.godEntity) return false
        if (amount != other.amount) return false
        if (price != other.price) return false

        return true
    }

    override fun hashCode(): Int {
        var result = godEntity.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}