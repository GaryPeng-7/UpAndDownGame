package service

import entity.*
import kotlin.test.*

/**
 * Testmethoden fuer die Methode CanPass() in [PlayerActionService]
 */
class PlayerActionServiceCanPassTest {
    private val rootService = RootService()

    private fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

        /**
         * der Spieler hat 8 Handkarten und eine Karte im Nachziehstapel
         * die linke gespielte Karte ist clubs jack
         * die rechte gespielte Karte ist club queen
         */
        game.player1.hand.removeAll(game.player1.hand)
        game.player1.drawDeck.removeAll(game.player1.drawDeck)
        game.centerDeck1.removeLast()
        game.centerDeck2.removeLast()

        game.player1.drawDeck.add(Card(CardSuit.SPADES, CardValue.FIVE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.ACE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TWO))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.THREE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.FOUR))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.FIVE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SIX))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SEVEN))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.EIGHT))
        game.centerDeck1.add(Card(CardSuit.CLUBS, CardValue.JACK))
        game.centerDeck2.add(Card(CardSuit.CLUBS, CardValue.QUEEN))

        game.currentPlayer = 0

        return rootService
    }

    /**
     * Testfall:
     * wenn man Karten spielen darf
     */
    @Test
    fun testCanPlayCard() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.drawDeck.removeLast()
        game.centerDeck1.add(Card(CardSuit.HEARTS, CardValue.NINE))
        assertFalse(rootService.playerActionService.canPass())
    }

    /**
     * Testfall:
     * wenn man Karten ziehen darf
     */
    @Test
    fun testCanDrawCard() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        assertFalse(rootService.playerActionService.canPass())
    }

    /**
     * Testfall:
     * wenn man Karten mischen darf
     */
    @Test
    fun testCanRedrawHand() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.SIX))
        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.SEVEN))
        assertFalse(rootService.playerActionService.canPass())
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

        game.player1.drawDeck.removeLast()
        assertTrue(rootService.playerActionService.canPass())
    }
}
