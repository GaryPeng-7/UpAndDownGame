package service

import entity.*
import gui.Refreshable
import kotlin.test.*

/**
 * Testmethoden fuer die Methode pass() in [PlayerActionService]
 */
class PlayerActionServicePassTest {
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
     * das Spiel existiert nicht
     */
    @Test
    fun testGameNull() {
        val rootService = setUp()

        rootService.currentGame = null
        assertFails{
            rootService.playerActionService.pass()
        }
    }

    /**
     * Testfall:
     * man darf nicht passen
     */
    @Test
    fun testCanNotPass() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        assertFails{rootService.playerActionService.pass()}
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

        game.player1.drawDeck.removeLast()

        assertFails {
            assertFails {
                rootService.playerActionService.pass()
            }
        }
        assertTrue(game.lastPass)
        assertEquals(game.currentPlayer, 1)
        assertTrue(testRefreshable.refreshAfterAfterPassCalled)
        assertTrue(testRefreshable.refreshAfterSwitchPlayerTurnCalled)
    }
}
