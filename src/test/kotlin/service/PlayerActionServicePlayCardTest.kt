package service

import entity.*
import gui.Refreshable
import kotlin.test.*

/**
 * Testmethoden fuer die Methode playCard() in [PlayerActionService]
 */
class PlayerActionServicePlayCardTest {
    private val rootService = RootService()

    /**
     * die Initialisierungsfunktion fuer das Spiel
     */
    fun setUp(vararg refreshables: Refreshable) : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)
        refreshables.forEach { rootService.addRefreshables(it) }


        /**
         * der Spieler hat nur eine Karte: herat seven und
         * auf der linken Spielstapel steht club ace
         */
        game.player1.hand.removeAll(game.player1.hand)
        game.centerDeck1.removeAll(game.centerDeck1)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.SEVEN))
        game.centerDeck1.add(Card(CardSuit.CLUBS, CardValue.ACE))
        game.currentPlayer = 0

        return rootService
    }

    /**
     * Testfall:
     * das Spiel existiert nicht
     */
    @Test
    fun testGameNull() {
        val rootService = setUp()

        rootService.currentGame = null
        val card = Card(CardSuit.CLUBS, CardValue.TWO)
        assertFails{
            rootService.playerActionService.playCard(card, 0)
        }
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

        // expected to be successful

        rootService.playerActionService.playCard(game.player1.hand.last(), 0)

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

        // expected to be successful
        rootService.playerActionService.playCard(game.player1.hand.last(), 0)

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

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.TWO))

        // expected to be successful
        rootService.playerActionService.playCard(game.player1.hand.last(), 0)

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

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.THREE))

        // expected to fail
        assertFails {
            rootService.playerActionService.playCard(game.player1.hand.last(), 0)
        }
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

        // expected to be successful
        rootService.playerActionService.playCard(game.player1.hand.last(), 0)
    }

    /**
     * Testfall:
     * gleiche Farbe + Diffenrenz = 2
     */
    @Test
    fun testSameSuit2() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.QUEEN))

        // expected to be successful
        rootService.playerActionService.playCard(game.player1.hand.last(), 0)

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

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.KING))

        // expected to be successful
        rootService.playerActionService.playCard(game.player1.hand.last(), 0)

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

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.QUEEN))

        // expected to fail
        assertFails {
            rootService.playerActionService.playCard(game.player1.hand.last(), 0)
        }

    }

    /**
     * Testfall:
     * unterschiedliche Farbe + Diffenrenz = 3
     */
    @Test
    fun testDifferentSuit3() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.JACK))

        // expected to fail
        assertFails {
            rootService.playerActionService.playCard(game.player1.hand.last(), 0)
        }

    }

    /**
     * Testfall:
     * die Nachbedingung nach der erfolgreichen Aktion
     */
    @Test
    fun testSuccess() {
        val rootService = setUp()
        val testRefreshable = TestRefreshable()
        rootService.addRefreshables(testRefreshable)

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.QUEEN))

        rootService.playerActionService.playCard(game.player1.hand.last(), 0)

        assertEquals(game.player1.hand.size, 1)
        assertEquals(game.centerDeck1.size, 2)
        assertTrue(game.centerDeck1.contains(Card(CardSuit.CLUBS, CardValue.QUEEN)))
        assertFalse(game.lastPass)
        assertEquals(game.currentPlayer, 1)
        assertTrue(testRefreshable.refreshAfterPlayCardCalled)
        assertTrue(testRefreshable.refreshAfterSwitchPlayerTurnCalled)
    }

}
