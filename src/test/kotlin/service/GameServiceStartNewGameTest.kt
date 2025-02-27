package service

import gui.Refreshable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


/**
 * Testmethoden fuer die Methode startNewGame() in [GameService]
 */
class GameServiceStartNewGameTest {

    private val rootService = RootService()

    fun setUp(vararg refreshables: Refreshable) : RootService {
        rootService.gameService.startNewGame("Kassel", "Duisburg")
        refreshables.forEach { rootService.addRefreshables(it) }

        return rootService
    }

    /**
     * Testfall der Ueberpruefung, ob alle Handkarten unterscedlich sind
     */
    @Test
    fun testStartNewGame() {
        val testRefreshable = TestRefreshable()
        val rootService = setUp(testRefreshable)
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
        assertTrue(testRefreshable.refreshAfterGameStartCalled)

    }
}
