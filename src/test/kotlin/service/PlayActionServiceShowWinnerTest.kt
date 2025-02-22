package service

import entity.*
import kotlin.test.*

class PlayActionServiceShowWinnerTest {
    private val rootService = RootService()

    fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

        game.player1.hand.removeAll(game.player1.hand)
        game.player1.drawDeck.removeAll(game.player1.drawDeck)
        game.player2.hand.removeAll(game.player2.hand)
        game.player2.drawDeck.removeAll(game.player2.drawDeck)
        game.centerDeck1.removeAll(game.centerDeck1)
        game.centerDeck2.removeAll(game.centerDeck2)


        game.centerDeck1.add(Card(CardSuit.CLUBS, CardValue.ACE))
        game.centerDeck2.add(Card(CardSuit.DIAMONDS, CardValue.ACE))

        return rootService
    }

    @Test
    fun testPlayer1HandEmpty() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        val player1 = game.player1
        game.currentPlayer = 0
        player1.hand.add(Card(CardSuit.CLUBS, CardValue.TWO))
        game.player2.hand.add(Card(CardSuit.CLUBS, CardValue.TWO))
        rootService.playActionService.playCard(player1.hand.last(), 0)

        assertEquals(game.winner, 0)
    }

    @Test
    fun testPlayer2HandEmpty() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        val player2 = game.player2
        game.currentPlayer = 1
        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.THREE))
        player2.hand.add(Card(CardSuit.CLUBS, CardValue.TWO))
        rootService.playActionService.playCard(player2.hand.last(), 0)

        println(player2)
        assertEquals(game.winner, 1)
    }

    @Test
    fun testPlayer1HandLess() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        val player1 = game.player1
        val player2 = game.player2
        game.currentPlayer = 0
        player1.hand.add(Card(CardSuit.CLUBS, CardValue.FIVE))
        player2.hand.add(Card(CardSuit.CLUBS, CardValue.SIX))
        player2.hand.add(Card(CardSuit.CLUBS, CardValue.EIGHT))
        rootService.playActionService.pass()
        rootService.playActionService.pass()

        assertEquals(game.winner, 0)
    }

    @Test
    fun testPlayer2HandLess() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        val player1 = game.player1
        val player2 = game.player2
        game.currentPlayer = 1
        player2.hand.add(Card(CardSuit.CLUBS, CardValue.FIVE))
        player1.hand.add(Card(CardSuit.CLUBS, CardValue.SIX))
        player1.hand.add(Card(CardSuit.CLUBS, CardValue.EIGHT))
        rootService.playActionService.pass()
        rootService.playActionService.pass()

        assertEquals(game.winner, 1)
    }

    @Test
    fun testTie() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        val player1 = game.player1
        val player2 = game.player2
        game.currentPlayer = 1
        player2.hand.add(Card(CardSuit.CLUBS, CardValue.FIVE))
        player2.hand.add(Card(CardSuit.CLUBS, CardValue.SEVEN))
        player1.hand.add(Card(CardSuit.CLUBS, CardValue.SIX))
        player1.hand.add(Card(CardSuit.CLUBS, CardValue.EIGHT))
        rootService.playActionService.pass()
        rootService.playActionService.pass()

        assertEquals(game.winner, 2)
    }
}