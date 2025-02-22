package service

import entity.*
import kotlin.random.Random

/**
 * Diese Klasse enthaelt Methoden, die nicht direct mit Spielern zu tun haben
 * @param rootService
 */

class GameService(val rootService: RootService) {

    /**
     * Die Methode startNewGame erlaubt das Starten des Spieles und das Setzen von Spielernamen.
     * Dabei werden die Hände, Nachziehstapel und Zentralstapel erzeugt.
     * Setzt zufällig den Startspieler.
     * @param playerName1
     * @param playerName2
     */
    fun startNewGame(playerName1 : String, playerName2 : String) {

        // Grundstapel erstellen und mischen
        val initialDeck = MutableList<Card>(52) { index ->
            Card(
                CardSuit.values()[index / 13],
                CardValue.values()[(index % 13)]
            )
        }.shuffled().toMutableList()

        // Spieler, ihre Handkarten und den Nachziehstapel zu initialisieren
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

        // UpAndDownGame erstellen
        val game = UpAndDownGame(playerIndex,
            player1, player2,
            mutableListOf(initialDeck.removeLast()),
            mutableListOf(initialDeck.removeLast())
            )

        // weise das jetzige Spiel zu Rootservice zu
        rootService.currentGame = game
    }
}