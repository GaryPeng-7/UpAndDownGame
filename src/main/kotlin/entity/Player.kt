package entity

/** die Klasse beschreibt den Zustand der Spieler und welche Karten sie haben
 * @param name Der Name der Spieler
 * @param hand Die HandKarten der Spieler
 * @param drawDeck Der Nachziehstapel der Spieler
 */
data class Player(
    val name: String,
    val hand: MutableList<Card>,
    val drawDeck: MutableList<Card> )
