package service

import entity.*
import kotlin.test.*

/**
 * Testmethoden fuer die Methode canPlayCard() in [PlayerActionService]
 */
class PlayerActionServiceCanPlayCardTest {
    private val rootService = RootService()

    /**
     * die Initialisierungsfunktion fuer das Spiel
     */
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

    /**
     * Testfall:
     * gleiche Farbe + Diffenrenz = 1
     */
    @Test
    fun testSameSuit1() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.KING))
        assertTrue(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * gleiche Farbe + Diffenrenz = 2
     */
    @Test
    fun testSameSuit2() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.QUEEN))
        assertTrue(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * unterschiedliche Farbe + Diffenrenz = 1
     */
    @Test
    fun testDifferentSuit1() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))
        assertTrue(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * unterschiedliche Farbe + Diffenrenz = 2
     */
    @Test
    fun testDifferentSuit2() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.QUEEN))
        assertFalse(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * gleiche Farbe + Diffenrenz = 12
     */
    @Test
    fun testSameSuit12() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.TWO))
        assertTrue(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * gleiche Farbe + Diffenrenz = 11
     */
    @Test
    fun testSameSuit11() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.THREE))
        assertTrue(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * unterschiedliche Farbe + Diffenrenz = 12
     */
    @Test
    fun testDifferentSuit12() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TWO))
        assertTrue(rootService.playerActionService.canPlayCard())
    }

    /**
     * Testfall:
     * unterschiedliche Farbe + Diffenrenz = 11
     */
    @Test
    fun testDifferentSuit11() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.THREE))
        assertFalse(rootService.playerActionService.canPlayCard())
    }
}
