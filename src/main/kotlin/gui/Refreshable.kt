package gui

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterGameStart() {}

    /**
     * perform refreshes that are necessary after the player played a card
     */
    fun refreshAfterPlayCard(centerDeck : Int) {}

    /**
     * perform refreshes that are necessary after the player drew a card
     */
    fun refreshAfterDrawCard() {}

    /**
     * perform refreshes that are necessary after the player mixed his cards and
     * then drew five cards
     */
    fun refreshAfterRedrawHand() {}

    /**
     * perform refreshes that are necessary after the player passed the round
     */
    fun refreshAfterAfterPass() {}

    /**
     * perform refreshes that are necessary after the game is ended
     */
    fun refreshAfterEndGame() {}

    /**
     * perform refreshes that are necessary after the turn is started
     */
    fun refreshAfterStartTurn() {}

    /**
     * perform refreshes that are necessary after the played is switched
     */
    fun refreshAfterSwitchPlayerTurn() {}
}
