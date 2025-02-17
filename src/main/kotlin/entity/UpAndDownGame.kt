package entity

class UpAndDownGame(player : List<String>) {
    var currentPlayer : Int = 0
    var lastPass : Boolean = false
    val player1 : Player = Player(player[0], mutableListOf(), mutableListOf())
    val player2 : Player = Player(player[0], mutableListOf(), mutableListOf())
    val centerDeck1 : MutableList<Card> = mutableListOf()
    val centerDeck2 : MutableList<Card> = mutableListOf()
}