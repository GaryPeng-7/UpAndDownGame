package service

import entity.*
import kotlin.test.*

/**
 * Testmethoden fuer die Methode redrawHand() in [PlayerActionService]
 */
class PlayerActionServiceRedrawHandTest {
    private val rootService = RootService()

    /**
     * die Initialisierungsfunktion fuer das Spiel
     */
    fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

        /**
         * Der Spieler hat 8 Handkarten und eine Karte im Nachziehstapel
         */
        game.player1.hand.removeAll(game.player1.hand)
        game.player1.drawDeck.removeAll(game.player1.drawDeck)

        game.player1.drawDeck.add(Card(CardSuit.SPADES, CardValue.FIVE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.ACE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TWO))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.THREE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.FOUR))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.FIVE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SIX))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SEVEN))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.EIGHT))

        game.currentPlayer = 0

        return rootService
    }

    /**
     * Testfall:
     * keine Karte im Nachziehstapel
     */
    @Test
    fun testDeckIsEmpty() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.drawDeck.removeLast()
        assertFails{rootService.playerActionService.redrawHand()}
    }

    /**
     * Testfall:
     * 7 Karten in der Hand (weniger als 8)
     */
    @Test
    fun testHandSizeSeven() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.removeLast()
        assertFails{rootService.playerActionService.redrawHand()}
    }

    /**
     * Testfall:
     * die Nachbedingung nach der erfolgreichen Aktion
     */
    @Test
    fun testSuccess() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)


        rootService.playerActionService.redrawHand()
        val cardSum = game.player1.hand.size + game.player1.drawDeck.size

        assertEquals(game.player1.hand.size, 5)
        assertEquals(game.player1.drawDeck.size, cardSum - 5)
        assertFalse(game.lastPass)
        assertEquals(game.currentPlayer, 1)
    }
}