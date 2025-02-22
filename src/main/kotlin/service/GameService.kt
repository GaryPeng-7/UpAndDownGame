package service

import entity.*
import kotlin.random.Random

class GameService(val rootService: RootService) {

    fun startNewGame(playerName1 : String, playerName2 : String) {

        // Grundstapel erzeugen und mischen
        val initialDeck = MutableList<Card>(52) { index ->
            Card(
                CardSuit.values()[index / 13],
                CardValue.values()[(index % 13)]
            )
        }.shuffled().toMutableList()


        // Spieler ihre Handkarten und Nachziehstapel zu initialisieren
        val player1 = Player(playerName1,
            MutableList<Card>(5) { index ->
                initialDeck.removeLast()},
            MutableList<Card>(20) { index ->
                initialDeck.removeLast()}
        )

        val player2 = Player(playerName2,
            MutableList<Card>(5) { index ->
                initialDeck.removeLast()},
            MutableList<Card>(20) { index ->
                initialDeck.removeLast()}
        )

        // ersten Spieler zu entscheiden
        val playerIndex = Random.nextInt(0,2)

        // UpAndDownGame erzeugen
        val game = UpAndDownGame(playerIndex,
            player1, player2,
            mutableListOf(initialDeck.removeLast()),
            mutableListOf(initialDeck.removeLast())
            )

        rootService.currentGame = game
    }
}