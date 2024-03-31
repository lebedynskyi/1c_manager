package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData

data class TreeNode(
    val content: GodsData,
    var children: MutableList<TreeNode> = mutableListOf(),
    @Deprecated("Do not use this flag")
    var expanded: Boolean = false,
    var godOrderData: GodOrderData? =null
)