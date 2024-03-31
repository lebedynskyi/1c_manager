package com.simple.games.tradeassist.ui.gods.list

import com.simple.games.tradeassist.ui.base.AppUIEvent
import com.simple.games.tradeassist.data.api.response.CustomerData
import com.simple.games.tradeassist.ui.gods.GodOrderTemplate

sealed class GodsSelectionUIEvent : AppUIEvent() {
    class OnScreenLoaded(val customer: CustomerData?) : GodsSelectionUIEvent()
    class OnShowAllToggleChanged(val showAll: Boolean) : GodsSelectionUIEvent()
    class OnFilterQueryChanged(val query: String) : GodsSelectionUIEvent()
    class OnGodsClicked(val node: TreeNode) : GodsSelectionUIEvent()
    class OnAddGodOrder(val order: GodOrderTemplate): GodsSelectionUIEvent()
    object OnDoneClick : GodsSelectionUIEvent()
    object OnCollapseClick : GodsSelectionUIEvent()
}