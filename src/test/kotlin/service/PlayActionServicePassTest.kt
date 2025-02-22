package service

import entity.*
import kotlin.test.*

class PlayActionServicePassTest {
    private val rootService = RootService()

    fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

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

    @Test
    fun testCanNotPass() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        assertFails{rootService.playActionService.pass()}
    }

    @Test
    fun testSuccess() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.drawDeck.removeLast()
        assertFails {
            assertFails {
                rootService.playActionService.pass()
            }
        }
        assertTrue(game.lastPass)
        assertEquals(game.currentPlayer, 1)
    }
}