package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.data.api.response.GodOrderData
import com.simple.games.tradeassist.data.api.response.GodsData
import com.simple.games.tradeassist.domain.GodEntity

data class TreeNode(
    val content: GodEntity,
    var children: MutableList<TreeNode> = mutableListOf(),
    @Deprecated("Do not use this flag")
    var expanded: Boolean = false
)