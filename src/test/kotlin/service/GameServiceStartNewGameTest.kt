package service

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Testmethoden fuer die Methode startNewGame() in [GameService]
 */
class GameServiceStartNewGameTest {

    @Test
    fun testStartNewGame() {
        val rootService = RootService()
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val game = rootService.currentGame
        // ueberpruefe ob das Spiel richtig initialisiert wurde
        checkNotNull(game)

        // ueberpruefe ob alle Handkarten unterscedlich sind
        val originalDeck = game.player1.drawDeck +
            game.player1.hand + game.player2.drawDeck +
            game.player2.hand + game.centerDeck1 + game.centerDeck2

        var counter = originalDeck.size

        for (card1 in originalDeck) {
            for (card2 in originalDeck) {
                if (card1.equals(card2)) {
                    counter--
                }
            }
        }

        // wenn counter kein 0 ist, heisst es, dass es wiederholte Karte im Stapel
        assertEquals(counter, 0)

    }
}