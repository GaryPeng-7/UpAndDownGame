package service

import entity.*
import gui.Refreshable
import kotlin.test.*

/**
 * Testmethoden fuer die Methode drawCard() in [PlayerActionService]
 */
class PlayerActionServiceDrawCardTest {
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
         * Der Spieler hat 9 Handkarten und eine Karte im Nachziehstapel
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
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.NINE))

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
            rootService.playerActionService.drawCard()
        }
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
        assertFails { rootService.playerActionService.drawCard() }
    }

    /**
     * Testfall:
     * 10 Karten in der Hand
     */
    @Test
    fun testHandSizeTen() {
        val rootService = setUp()

        val game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TEN))
        assertFails { rootService.playerActionService.drawCard() }
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


        val drewCard = game.player1.drawDeck.last()
        rootService.playerActionService.drawCard()

        assertEquals(game.player1.hand.last(), drewCard)
        assertTrue(game.player1.drawDeck.isEmpty())
        assertFalse(game.lastPass)
        assertEquals(game.currentPlayer, 1)
        assertTrue(testRefreshable.refreshAfterDrawCardCalled)
        assertTrue(testRefreshable.refreshAfterSwitchPlayerTurnCalled)
    }
}
