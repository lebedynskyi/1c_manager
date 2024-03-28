package com.simple.games.tradeassist.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CustomerData {
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
}

@Serializable
class CustomerContactData {
    @SerialName("Ref_Key")
    lateinit var refKey: String

    @SerialName("Представление")
    var description: String? = null
}