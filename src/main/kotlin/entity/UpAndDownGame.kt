package entity

class UpAndDownGame(var currentPlayer : Int, val player1 : Player, val player2 : Player,
                    val centerDeck1 : MutableList<Card>,
                    val centerDeck2 : MutableList<Card>) {
    var lastPass : Boolean = false
}