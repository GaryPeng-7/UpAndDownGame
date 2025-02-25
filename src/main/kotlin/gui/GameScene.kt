package gui

import service.*
import entity.*
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    private val centerDeck1 = CardStack<CardView>(posX = 800, posY = 540)
    private val player1DrawDeck = CardStack<CardView>(posX = 400, posY = 540).apply {
        onMouseClicked = {
            rootService.currentGame?.let { game ->
                rootService.playerActionService.drawCard()
            }
        }
    }
    private val player1Hand = LinearLayout<CardView>(
        posX = 50,
        posY = 880,
        width = 1920,
        height = 200,
        alignment = Alignment.BOTTOM_LEFT,
        spacing = -60
    )

    private val centerDeck2 = CardStack<CardView>(posX = 990, posY = 540)
    private val player2DrawDeck = CardStack<CardView>(posX = 1390, posY = 540).apply {
        onMouseClicked = {
            rootService.currentGame?.let { game ->
                rootService.playerActionService.drawCard()
            }
        }
    }
    private val player3Hand = LabeledStackView(posX = 1590, posY = 880, "p2's hand")
    private val player2Hand = LinearLayout<CardView>(
        posX = -50,
        posY = 880,
        width = 1920,
        height = 200,
        alignment = Alignment.BOTTOM_RIGHT,
        spacing = -60
    )

    private val currentPlayerText = Label(
        width = 200, height = 50,
        posX = 200, posY = 300,
        alignment = Alignment.CENTER,
        text = ""
    )



    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {
        background = ColorVisual(108, 168, 59)

        addComponents(
            centerDeck1,
            player1Hand,
            player1DrawDeck,
            centerDeck2,
            player2Hand,
            player2DrawDeck,
            currentPlayerText
        )
    }

    override fun refreshAfterGameStart() {
        val game = rootService.currentGame
        checkNotNull(game) { "The game has not started yet." }

        cardMap.clear()


        val cardImageLoader = CardImageLoader()
        val currentPlayer = game.currentPlayer()

        initializeCardView(game.centerDeck1, centerDeck1, true, cardImageLoader)
        initializeCardView(game.player1.hand, player1Hand, false, cardImageLoader)
        initializeCardView(game.player1.drawDeck, player1DrawDeck, false, cardImageLoader)
        initializeCardView(game.centerDeck2, centerDeck2, true, cardImageLoader)
        initializeCardView(game.player2.hand, player2Hand, false, cardImageLoader)
        initializeCardView(game.player2.drawDeck, player2DrawDeck, false, cardImageLoader)

        currentPlayerText.text = "current player: " + currentPlayer.name
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }



    }

    private fun initializeCardView(cards: MutableList<Card>, cardStack: CardStack<CardView>,
                                   flip: Boolean, cardImageLoader: CardImageLoader) {
        cardStack.clear()


        cards.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage,
            )
            if (flip) {
                cardView.showFront()
            }
            cardStack.add(cardView)
            cardMap.add(card to cardView)
        }

    }

    private fun initializeCardView(cards: MutableList<Card>, linearLayout: LinearLayout<CardView>,
                                   flip: Boolean, cardImageLoader: CardImageLoader) {
        linearLayout.clear()

        cards.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage,
            )
            if (flip) {
                cardView.showFront()
            }
            linearLayout.add(cardView)
            cardMap.add(card to cardView)
        }

    }

    override fun refreshAfterDrawCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }


        when (val currentPlayer = game .currentPlayer()) {
            game.player1 -> {
                moveCardView(cardMap.forward(currentPlayer.hand.last()), player1Hand, true)
                currentPlayer.hand.forEach { card ->
                    flipCardView(cardMap.forward(card))
                }
            }
            game.player2 -> {
                moveCardView(cardMap.forward(currentPlayer.hand.last()), player2Hand, true)
                currentPlayer.hand.forEach { card ->
                    flipCardView(cardMap.forward(card))
                }
            }
        }
    }

    /**
    override fun refreshAfterSwitchPlayerTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }



        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }

    }
    */

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        //val currentPlayer = game.currentPlayer()

        val currentPlayer = game.currentPlayer()
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }
        currentPlayerText.text = "current player: " + currentPlayer.name
    }

    private fun flipCardView(cardView: CardView) {
        when (cardView.currentSide) {
            CardView.CardSide.BACK -> cardView.showFront()
            CardView.CardSide.FRONT -> cardView.showBack()
        }
    }

    private fun moveCardView(cardView: CardView, linearLayout: LinearLayout<CardView>, flip: Boolean) {

        if (flip) flipCardView(cardView)

        cardView.removeFromParent()
        linearLayout.add(cardView)
    }


}
