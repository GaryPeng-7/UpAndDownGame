package service

import entity.*

class PlayActionService(val rootService: RootService) {


    fun playCard(card : Card, centerDeck : Int) {
        val game = rootService.currentGame
        checkNotNull(game)

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

        val currentPlayer = game.currentPlayer()
        val cardIndex = currentPlayer.hand.indexOf(card)
        game.getCenterDeck(centerDeck).add(currentPlayer.hand.removeAt(cardIndex))

        if (currentPlayer.hand.isEmpty() && currentPlayer.drawDeck.isEmpty()) {
            showWinner()
        } else {
            game.lastPass = false
            switchPlayerTurn()
        }

    }

    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game)

        require(canDrawCard()) {"You can't draw a card"}

        val currentPlayer = game.currentPlayer()
        currentPlayer.hand.add(currentPlayer.drawDeck.removeLast())

        game.lastPass = false
        switchPlayerTurn()

    }

    fun redrawHand() {
        val game = rootService.currentGame
        checkNotNull(game)

        require(canRedrawHand()) {"You can't redraw your hand"}

        val currentPlayer = game.currentPlayer()
        while (currentPlayer.hand.isNotEmpty()) {
            currentPlayer.drawDeck.add(currentPlayer.hand.removeLast())
        }

        currentPlayer.drawDeck.shuffle()

        for (i in 0 until 5) {
            currentPlayer.hand.add(currentPlayer.drawDeck.removeLast())
        }

        game.lastPass = false
        switchPlayerTurn()

    }

    fun pass() {
        val game = rootService.currentGame
        checkNotNull(game)

        require(canPass()) {"You can't pass"}

        if (game.lastPass) {
            showWinner()
        } else {
            game.lastPass = true
            switchPlayerTurn()
        }

    }

    fun canPlayCard() : Boolean {
        val game = rootService.currentGame
        checkNotNull(game)

        val centerLeftCard = game.centerDeck1.last()
        val centerRightCard = game.centerDeck2.last()

        var difference : Int

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

    fun canDrawCard() : Boolean {
        val game = rootService.currentGame
        checkNotNull(game)

        // Decksize > 0
        if (game.currentPlayer().drawDeck.isEmpty()) {
            return false
        }
        // Handsize < 10
        if (game.currentPlayer().hand.size >= 10) {
            return false
        }
        return true
    }

    fun canRedrawHand() : Boolean {

        val game = rootService.currentGame
        checkNotNull(game)

        // Decksize sollte > 0
        if (game.currentPlayer().drawDeck.isEmpty()) {
            return false
        }
        // Handsize >= 8
        if (game.currentPlayer().hand.size < 8) {
            return false
        }
        return true
    }

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

    fun switchPlayerTurn() {
        val game = rootService.currentGame
        checkNotNull(game)

        game.currentPlayer = (game.currentPlayer + 1) % 2
    }

    fun showWinner() {
        val game = rootService.currentGame
        checkNotNull(game)


        val cardSum1 = game.player1.hand.size + game.player1.drawDeck.size
        val cardSum2 = game.player2.hand.size + game.player2.drawDeck.size

        if (cardSum1 == 0) {
            println("player " + game.player1.name + " has won!")
            game.winner = 0
            return
        }
        if (cardSum2 == 0) {
            println("player " + game.player2.name + " has won!")
            game.winner = 1
            return
        }
        if ((cardSum1 < cardSum2)) {
            println("player " + game.player1.name + " has won!")
            game.winner = 0
            return
        }
        if ((cardSum1 > cardSum2)) {
            println("player " + game.player2.name + " has won!")
            game.winner = 1
            return
        }
        println("It's a tie!!")
        game.winner = 2
    }
}