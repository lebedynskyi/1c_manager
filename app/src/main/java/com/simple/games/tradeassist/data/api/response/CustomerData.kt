package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CustomerData :java.io.Serializable {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Parent_Key")
    lateinit var parentKey: String

    @SerialName("IsFolder")
    var isFolder: Boolean = false

    @SerialName("Description")
    var description: String? = null

    @SerialName("КонтактнаяИнформация")
    var contact: List<CustomerContactData>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GodsData

        return refKey == other.refKey
    }

    override fun hashCode(): Int {
        return refKey.hashCode()
    }
}

@Serializable
class CustomerContactData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Представление")
    var description: String? = null
}