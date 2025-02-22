package entity

import entity.CardSuit.*

/** die Klasse beschreibt, welche Karte ist es
 * @param suit Farbe der Karte
 * @param value Nummer der Karte
 */
data class Card(val suit : CardSuit, val value : CardValue) {

    override fun toString() = "$suit$value"
}
