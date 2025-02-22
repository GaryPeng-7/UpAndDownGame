package service

import entity.*
import kotlin.test.*

class PlayActionServiceCanDrawCardTest {
    private val rootService = RootService()

    fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

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

    @Test
    fun testDeckIsEmpty() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.drawDeck.removeLast()
        assertFalse(rootService.playActionService.canDrawCard())
    }

    @Test
    fun testHandSizeTen() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.TEN))
        assertFalse(rootService.playActionService.canDrawCard())
    }

    @Test
    fun testSuccess() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        assertTrue(rootService.playActionService.canDrawCard())
    }
}