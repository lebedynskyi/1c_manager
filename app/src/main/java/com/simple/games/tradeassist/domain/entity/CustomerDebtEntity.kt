package com.simple.games.tradeassist.domain.entity

import com.simple.games.tradeassist.data.api.response.DebtRecordData

class CustomerDebtEntity(
    val customerKey: String,
    val plusList: List<DebtRecordData>,
    val minus: List<DebtRecordData>,
    val totalPlus: Double,
    val totalMinus: Double
)