package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.domain.entity.GodEntity

data class TreeNode(
    val content: GodEntity,
    var children: MutableList<TreeNode> = mutableListOf(),
    @Deprecated("Do not use this flag")
    var expanded: Boolean = false
)