package service

import entity.*
import kotlin.test.*

/**
 * Testmethoden fuer die Methode canPlayCard() in [PlayActionService]
 */
class PlayActionServiceCanPlayCardTest {
    private val rootService = RootService()

    fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

        /**
         * der Spieler hat 4 Karten, mit denen man nicht ablegen darf
         */
        game.player1.hand.removeAll(game.player1.hand)
        game.centerDeck1.add(Card(CardSuit.CLUBS, CardValue.ACE))
        game.centerDeck2.add(Card(CardSuit.SPADES, CardValue.ACE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.FIVE))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SIX))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SEVEN))
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.EIGHT))
        game.currentPlayer = 0

        return rootService
    }

    @Test
    fun testSameSuit1() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.KING))
        assertTrue(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.KING))
        assertTrue(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testSameSuit2() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.QUEEN))
        assertTrue(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.QUEEN))
        assertTrue(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testDifferentSuit1() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))
        assertTrue(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))
        assertTrue(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testDifferentSuit2() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.QUEEN))
        assertFalse(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.QUEEN))
        assertFalse(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testSameSuit12() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.TWO))
        assertTrue(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.TWO))
        assertTrue(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testSameSuit11() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.THREE))
        assertTrue(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.THREE))
        assertTrue(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testDifferentSuit12() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TWO))
        assertTrue(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TWO))
        assertTrue(rootService.playActionService.canPlayCard())
    }

    @Test
    fun testDifferentSuit11() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.THREE))
        assertFalse(rootService.playActionService.canPlayCard())

        game.player1.hand.removeLast()
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.THREE))
        assertFalse(rootService.playActionService.canPlayCard())
    }
}