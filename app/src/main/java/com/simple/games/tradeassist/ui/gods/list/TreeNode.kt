package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.data.api.response.GodsData

data class TreeNode(
    val content: GodsData,
    var children: MutableList<TreeNode> = mutableListOf(),
    var expanded: Boolean = false,
)