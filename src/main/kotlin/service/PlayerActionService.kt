package service

import entity.*

/**
 * Diese Klasse enthaelt alle Methoden, die mit Spielern zu tun haben,
 * und ihre Submethoden
 * @param rootService
 */

class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Die Methode playCard ermöglicht es einem Spieler, eine Karte von seiner Hand zu spielen.
     * Diese Karte wird auf einen der zwei möglichen Ablagestapel gelegt, sofern sie den Spielregeln entspricht.
     * @param card die gespielte Karte
     * @param centerDeck das Index des Spielstaples
     */
    fun playCard(card : Card, centerDeck : Int) {
        val game = rootService.currentGame
        checkNotNull(game)

        // ueberpruefe, ob man die Karte auf den Stapel ablegen kann
        val centerCard = game.getCenterDeck(centerDeck).last()
        val difference = Math.abs(card.value.compareTo(centerCard.value))
        val canPlay : Boolean = when (difference) {
            1 -> true
            2 -> centerCard.suit.compareTo(card.suit) == 0
            11 -> centerCard.suit.compareTo(card.suit) == 0
            12 -> true
            else -> false
        }
        require(canPlay) { "It's not a legal move."}

        // die Karte wird von der Hand entfernt und
        // die entfernte Karte wird zum Stapel hinzugefuegt
        val currentPlayer = game.currentPlayer()
        val cardIndex = currentPlayer.hand.indexOf(card)
        game.getCenterDeck(centerDeck).add(currentPlayer.hand.removeAt(cardIndex))

        // wenn der Spieler keine Karte in der Hand und dem Nachziehstapel
        // nach der Aktion hat, dann ueberpruefe die Gewinnbedingungen.
        // Ansonsten ist der naechste Spieler an der Reihe
        if (currentPlayer.hand.isEmpty() && currentPlayer.drawDeck.isEmpty()) {
            showWinner()
        } else {

            onAllRefreshables { refreshAfterPlayCard(centerDeck) }
            game.lastPass = false
            switchPlayerTurn()
        }
    }

    /**
     * Die Methode ermöglicht es einem Spieler,
     * eine Karte von seinem Nachziehstapel zu ziehen.
     */
    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game)

        require(canDrawCard()) {"You can't draw a card"}

        // Der Spieler bekommt eine Karte vom Nachziehstapel
        val currentPlayer = game.currentPlayer()
        currentPlayer.hand.add(currentPlayer.drawDeck.removeLast())


        onAllRefreshables { refreshAfterDrawCard() }
        game.lastPass = false
        switchPlayerTurn()
    }

    /**
     * Die Methode ermöglicht es dem aktiven Spieler die zugehörigen Handkarten ins Deck
     * zu mischen und fünf neue zu ziehen.
     */
    fun redrawHand() {
        val game = rootService.currentGame
        checkNotNull(game)

        require(canRedrawHand()) {"You can't redraw your hand"}

        val currentPlayer = game.currentPlayer()

        // lade alle Karten zum Nachziehstapel und dann mische den
        while (currentPlayer.hand.isNotEmpty()) {
            currentPlayer.drawDeck.add(currentPlayer.hand.removeLast())
        }
        currentPlayer.drawDeck.shuffle()

        // ziehe 5 Karten vom Nachziehstapel
        repeat(5) {
            currentPlayer.hand.add(currentPlayer.drawDeck.removeLast())
        }


        onAllRefreshables { refreshAfterRedrawHand() }
        game.lastPass = false
        switchPlayerTurn()
    }

    /**
     * Die Methode ermöglicht es einem Spieler, seinen Zug zu überspringen,
     * wenn es keine anderen gueltigen Aktionen gibt
     */
    fun pass() {
        val game = rootService.currentGame
        checkNotNull(game)

        require(canPass()) {"You can't pass"}

        // wenn der letzte Spieler gepasst hat, dann ueberpruefe die Gewinnbedingungen,
        // ansonsten ist der naechste Spieler an der Reihe
        if (game.lastPass) {
            showWinner()
        } else {
            onAllRefreshables { refreshAfterAfterPass() }
            game.lastPass = true
            switchPlayerTurn()
        }

    }

    /**
     * Diese Methode ueberpruefe, ob man seine Handkarten ablegen kann
     */
    fun canPlayCard() : Boolean {
        val game = rootService.currentGame
        checkNotNull(game)

        val centerLeftCard = game.centerDeck1.last()
        val centerRightCard = game.centerDeck2.last()

        // Die Differenz zwischen der Handkarte und der oberen Stapelkarte
        var difference : Int

        // berechne die Differenz der Handkarten sowohl mit der linken Karte
        // als auch mit der rechten Karten
        for (card in game.currentPlayer().hand) {
            difference = Math.abs(card.value.compareTo(centerLeftCard.value))
            if (card.suit.compareTo(centerLeftCard.suit) == 0) {
                if (difference == 2) difference--
                if (difference == 11) difference ++
            }
            if (difference == 1 || difference == 12) {
                return true
            }

            difference = Math.abs(card.value.compareTo(centerRightCard.value))
            if (card.suit.compareTo(centerRightCard.suit) == 0) {
                if (difference == 2) difference--
                if (difference == 11) difference ++
            }
            if (difference == 1 || difference == 12) {
                return true
            }
        }
        return false
    }

    /**
     * Diese Methode ueberpruefe, ob man eine Karte ziehen kann
     */
    fun canDrawCard() : Boolean {
        val game = rootService.currentGame
        checkNotNull(game)

        // Nachziehstapel > 0
        if (game.currentPlayer().drawDeck.isEmpty()) {
            return false
        }
        // Handsize < 10
        if (game.currentPlayer().hand.size >= 10) {
            return false
        }
        return true
    }

    /**
     * Diese Methode ueberpruefe, ob man es kann,
     * seine Handkarten mit dem Nachziehstapel zu mischen und dann
     * 5 Karten vom Nachziehstapel zu ziehen
     */
    fun canRedrawHand() : Boolean {

        val game = rootService.currentGame
        checkNotNull(game)

        // Decksize > 0
        if (game.currentPlayer().drawDeck.isEmpty()) {
            return false
        }
        // Handsize >= 8
        if (game.currentPlayer().hand.size < 8) {
            return false
        }
        return true
    }

    /**
     * Diese Methode ueberpruefe, ob man passen kann
     */
    fun canPass() : Boolean {

        if (canPlayCard()) {
            return false
        }
        if (canDrawCard()) {
            return false
        }
        if (canRedrawHand()) {
            return false
        }

        return true
    }

    /**
     * Hilfsmethode
     */
    private fun switchPlayerTurn() {
        val game = rootService.currentGame
        checkNotNull(game)

        game.currentPlayer = (game.currentPlayer + 1) % 2
        onAllRefreshables { refreshAfterSwitchPlayerTurn() }
    }

    /**
     * Diese Methode berechnet es, wer der Gewinner ist
     * 0 = player 1 hat gewonnen
     * 1 = player 2 hat gewonnen
     * 2 = Unentschieden
     */
    fun showWinner() {
        val game = rootService.currentGame
        checkNotNull(game)


        val cardSum1 = game.player1.hand.size + game.player1.drawDeck.size
        val cardSum2 = game.player2.hand.size + game.player2.drawDeck.size

        if (cardSum1 == 0) {
            println("player " + game.player1.name + " has won!")
            game.winner = 0
            onAllRefreshables { refreshAfterEndGame() }
            return
        }
        if (cardSum2 == 0) {
            println("player " + game.player2.name + " has won!")
            game.winner = 1
            onAllRefreshables { refreshAfterEndGame() }
            return
        }
        if ((cardSum1 < cardSum2)) {
            println("player " + game.player1.name + " has won!")
            game.winner = 0
            onAllRefreshables { refreshAfterEndGame() }
            return
        }
        if ((cardSum1 > cardSum2)) {
            println("player " + game.player2.name + " has won!")
            game.winner = 1
            onAllRefreshables { refreshAfterEndGame() }
            return
        }
        println("It's a tie!!")
        game.winner = 2
        onAllRefreshables { refreshAfterEndGame() }
    }
}
