package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/** Testklasse für die Klasse [Card].
 */
class CardTest {
    private val heartJack = Card(CardSuit.HEARTS, CardValue.JACK)
    private val spadeSeven = Card(CardSuit.SPADES, CardValue.SEVEN)

    /** Teste die Configuration der Karte
     *
     */
    @Test
            /** Teste ob [CardSuit] und [CardValue] richt sind
             *
             */
    fun configTest() {
        assertEquals(heartJack.suit, CardSuit.HEARTS)
        assertEquals(spadeSeven.value, CardValue.SEVEN)
    }
}
