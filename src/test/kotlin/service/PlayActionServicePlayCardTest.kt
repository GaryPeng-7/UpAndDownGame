package service

import entity.*
import kotlin.test.*

/**
 * Testmethoden fuer die Methode playCard() in [PlayActionService]
 */
class PlayActionServicePlayCardTest {
    private val rootService = RootService()

    fun setUp() : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        checkNotNull(game)

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

    @Test
    fun testSameSuit12() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.TWO))

        // expected to be successful
        assertFails{
            assertFails{
                rootService.playActionService.playCard(game.player1.hand.last(), 0)
            }
        }
    }

    @Test
    fun testSameSuit11() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.THREE))

        // expected to be successful
        assertFails{
            assertFails{
                rootService.playActionService.playCard(game.player1.hand.last(), 0)
            }
        }
    }

    @Test
    fun testDifferentSuit12() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.TWO))

        // expected to be successful
        assertFails{
            assertFails{
                rootService.playActionService.playCard(game.player1.hand.last(), 0)
            }
        }
    }

    @Test
    fun testDifferentSuit11() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.THREE))

        // expected to fail
        assertFails {
            rootService.playActionService.playCard(game.player1.hand.last(), 0)
        }

    }

    @Test
    fun testSameSuit1() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.KING))

        // expected to be successful
        assertFails{
            assertFails{
                rootService.playActionService.playCard(game.player1.hand.last(), 0)
            }
        }
    }

    @Test
    fun testSameSuit2() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.QUEEN))

        // expected to be successful
        assertFails{
            assertFails{
                rootService.playActionService.playCard(game.player1.hand.last(), 0)
            }
        }
    }

    @Test
    fun testDifferentSuit1() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.KING))

        // expected to be successful
        assertFails{
            assertFails{
                rootService.playActionService.playCard(game.player1.hand.last(), 0)
            }
        }
    }

    @Test
    fun testDifferentSuit2() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.DIAMONDS, CardValue.QUEEN))

        // expected to fail
        assertFails {
            rootService.playActionService.playCard(game.player1.hand.last(), 0)
        }

    }

    @Test
    fun testSuccess() {
        val rootService = setUp()

        var game = rootService.currentGame
        assertNotNull(game)

        game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.QUEEN))

        rootService.playActionService.playCard(game.player1.hand.last(), 0)

        assertEquals(game.player1.hand.size, 1)
        assertEquals(game.centerDeck1.size, 2)
        assertTrue(game.centerDeck1.contains(Card(CardSuit.CLUBS, CardValue.QUEEN)))
        assertFalse(game.lastPass)
        assertEquals(game.currentPlayer, 1)
    }

}