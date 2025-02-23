package entity

/** die Klasse enthält die Information wie: die Spieler, die gespielten Stapel,
 *  ob der letzte Spieler gepasst hat und wer Spielt gerade
 *  @param currentPlayer Das Index des aktuellen Spieler
 *  @param player1 Der linke Spieler
 *  @param player2 Der rechte Spieler
 *  @param centerDeck1 der linke gespielte Stapel
 *  @param centerDeck2 der rechte gespielte Stapel
 *  @property lastPass ob der letzte Spieler gepasst hat
 */
data class UpAndDownGame(var currentPlayer : Int, val player1 : Player, val player2 : Player,
                    val centerDeck1 : MutableList<Card>,
                    val centerDeck2 : MutableList<Card>) {
    var lastPass : Boolean = false
    var winner : Int = -1

    /**
     * ein Getter fuer den jetzigen Spiler,
     * 0 ist Player1
     * 1 ist Player2
     */
    fun currentPlayer() : Player{
        if (currentPlayer == 0) {
            return player1
        }
        return player2
    }

    /**
     * ein Getter fuer den Spielstapel,
     * 0 ist der linke Spielstapel
     * 1 ist der rechte Spielstapel
     */
    fun getCenterDeck(index : Int) : MutableList<Card> {
        if (index == 0) {
            return centerDeck1
        }
        return centerDeck2
    }
}
