package entity

/** die Klasse enthält die Information wie: die Spieler, die gespielten Stapel,
 *  ob der letzte Spieler gepasst hat und wer Spielt gerade
 *  @param currentPlayer Das Index des aktuellen Spieler
 *  @param player1 Der linke Spieler
 *  @param player2 Der rechte Spieler
 *  @param centerDeck1 der linke gespielte Stapel
 *  @param centerDeck2 der rechte gespielte Stapel
 */
class UpAndDownGame(var currentPlayer : Int, val player1 : Player, val player2 : Player,
                    val centerDeck1 : MutableList<Card>,
                    val centerDeck2 : MutableList<Card>) {
    var lastPass : Boolean = false
}