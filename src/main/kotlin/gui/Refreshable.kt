package gui

import entity.*

interface Refreshable {

    fun refreshAfterGameStart() {}

    fun refreshAfterPlayCard(centerDeck : Int) {}

    fun refreshAfterDrawCard() {}

    fun refreshAfterRedrawHand() {}

    fun refreshAfterAfterPass() {}

    fun refreshAfterEndGame() {}

    fun refreshAfterStartTurn() {}

    fun refreshAfterSwitchPlayerTurn() {}

}
