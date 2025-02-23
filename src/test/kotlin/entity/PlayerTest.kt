package entity

import kotlin.test.Test
import kotlin.test.assertTrue

/** Testklasse für die Klasse [Player]
 */
class PlayerTest {
    private val player1 = Player("player1", mutableListOf(Card(CardSuit.HEARTS, CardValue.TEN)),
        mutableListOf(Card(CardSuit.SPADES, CardValue.FIVE)))
    private val player2 = Player("player2", mutableListOf(Card(CardSuit.SPADES, CardValue.ACE)),
        mutableListOf(Card(CardSuit.CLUBS, CardValue.QUEEN)))

    /** Teste die Configuration der Player
     *
     */
    @Test
    fun configTest() {
        assertTrue(player1.hand.size > 0)
        assertTrue(player1.name != player2.name)
    }
}
