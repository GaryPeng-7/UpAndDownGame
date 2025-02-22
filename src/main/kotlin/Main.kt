import entity.*
import service.*
import java.util.*

fun main() {
    //SopraApplication().show()

    val rootService = RootService()
    //val gameService = GameService(rootService)
    //val playActionService = PlayActionService(rootService)
    rootService.gameService.startNewGame("Kassel", "Duisburg")
    val game = rootService.currentGame
    checkNotNull(game)
    val player1 = game.player1
    println(player1)
    game.currentPlayer = 0
    rootService.playActionService.drawCard()
    println(player1)
    println(game.currentPlayer())
    println(game.centerDeck1)
    println(game.centerDeck2)
    val card = Card(CardSuit.CLUBS, CardValue.KING)
    game.currentPlayer = 0
    game.player1.hand.add(Card(CardSuit.CLUBS, CardValue.KING))
    game.centerDeck1.add(Card(CardSuit.CLUBS, CardValue.ACE))

    rootService.playActionService.playCard(card, 0)
    println(game.centerDeck1)




    //println("Application ended. Goodbye")
}