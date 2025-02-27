package service

import gui.Refreshable

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable: Refreshable {

    var refreshAfterGameStartCalled: Boolean = false
        private set

    var refreshAfterPlayCardCalled: Boolean = false
        private set

    var refreshAfterDrawCardCalled: Boolean = false
        private set

    var refreshAfterRedrawHandCalled: Boolean = false
        private set

    var refreshAfterAfterPassCalled: Boolean = false
        private set

    var refreshAfterEndGameCalled: Boolean = false
        private set

    var refreshAfterStartTurnCalled: Boolean = false
        private set

    var refreshAfterSwitchPlayerTurnCalled: Boolean = false
        private set

    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterEndGameCalled = false
        refreshAfterStartTurnCalled = false
        refreshAfterRedrawHandCalled = false
        refreshAfterAfterPassCalled = false
        refreshAfterDrawCardCalled = false
        refreshAfterPlayCardCalled = false
        refreshAfterGameStartCalled = false
        refreshAfterSwitchPlayerTurnCalled = false
    }

    override fun refreshAfterGameStart() {
        refreshAfterGameStartCalled = true
    }

    override fun refreshAfterPlayCard(centerDeck : Int) {
        refreshAfterPlayCardCalled = true
    }

    override fun refreshAfterDrawCard() {
        refreshAfterDrawCardCalled = true
    }

    override fun refreshAfterRedrawHand() {
        refreshAfterRedrawHandCalled = true
    }

    override fun refreshAfterAfterPass() {
        refreshAfterAfterPassCalled = true
    }

    override fun refreshAfterEndGame() {
        refreshAfterEndGameCalled = true
    }

    override fun refreshAfterStartTurn() {
        refreshAfterStartTurnCalled = true
    }

    override fun refreshAfterSwitchPlayerTurn() {
        refreshAfterSwitchPlayerTurnCalled = true
    }


}
