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
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

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
    private val player2Hand = LinearLayout<CardView>(
        posX = -50,
        posY = 880,
        width = 1920,
        height = 200,
        alignment = Alignment.BOTTOM_RIGHT,
        spacing = -60
    )

    private val currentPlayerText = Label(
        width = 300, height = 80,
        posX = 800, posY = 250,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 28)
    )
    private val player1Text = Label(
        width = 300, height = 80,
        posX = 300, posY = 250,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 28)
    )
    private val player2Text = Label(
        width = 300, height = 80,
        posX = 1300, posY = 250,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 28)
    )

    private val tempCardStack = CardStack<CardView>()
    private val redrawButton = Button(
        width = 140, height = 35,
        posX = 900, posY = 880,
        text = "redraw",
        //alignment = Alignment.BOTTOM_CENTER
    ).apply {
        visual = ColorVisual(221, 136, 136)
        onMouseClicked = {

            rootService.currentGame?.let { game ->
                rootService.playerActionService.redrawHand()
            }
        }
    }
    private val passButton = Button(
        width = 140, height = 35,
        posX = 900, posY = 1000,
        text = "pass",
        //alignment = Alignment.BOTTOM_CENTER
    ).apply {
        visual = ColorVisual(221, 136, 136)
        onMouseClicked = {
            rootService.currentGame?.let { game ->
                rootService.playerActionService.pass()
            }
        }
    }


    private val player1DeckSizeText = Label(
        width = 200, height = 60,
        posX = 340, posY = 480,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )
    private val player1HandSizeText = Label(
        width = 200, height = 60,
        posX = 250, posY = 820,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )



    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {
        background = ColorVisual(108, 168, 59)

        addComponents(
            centerDeck1, player1Hand, player1DrawDeck,
            centerDeck2, player2Hand, player2DrawDeck,
            currentPlayerText, player1Text, player2Text,
            redrawButton, passButton,
            player1DeckSizeText,player1HandSizeText
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
        val cardView = player1DrawDeck.peek()
        println(cardView.parent)

        currentPlayerText.text = "current player: " + currentPlayer.name
        player1Text.text = game.player1.name
        player2Text.text = game.player2.name
        player1DeckSizeText.text = game.player1.drawDeck.size.toString() + "/ 20"
        player1HandSizeText.text = game.player1.hand.size.toString() + "/ 10"

        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }

        redrawButton.isDisabled = !rootService.playerActionService.canRedrawHand()
        passButton.isDisabled = !rootService.playerActionService.canPass()
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
            if (cardMap.containsForward(card)){
                cardMap.removeForward(card)
            }
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
            if (cardMap.containsForward(card)){
                cardMap.removeForward(card)
            }
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

        val currentPlayer = game .currentPlayer()

        when (currentPlayer) {
            game.player1 -> {
                moveCardView(cardMap.forward(currentPlayer.hand.last()), player1Hand, player1DrawDeck, true)
                player1DeckSizeText.text = currentPlayer.drawDeck.size.toString() + "/ 20"
                player1HandSizeText.text = currentPlayer.hand.size.toString() + "/ 10"
            }
            game.player2 -> {
                moveCardView(cardMap.forward(currentPlayer.hand.last()), player2Hand, player2DrawDeck, true)
                }
            }
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }
    }

    override fun refreshAfterRedrawHand() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        val currentPlayer = game.currentPlayer()
        val cardImageLoader = CardImageLoader()

        when (currentPlayer) {
            game.player1 -> {
                initializeCardView(currentPlayer.drawDeck, player1DrawDeck, false, cardImageLoader)
                initializeCardView(currentPlayer.hand, player1Hand, true, cardImageLoader)
                player1DeckSizeText.text = currentPlayer.drawDeck.size.toString() + "/ 20"
                player1HandSizeText.text = currentPlayer.hand.size.toString() + "/ 10"
            }
            game.player2 -> {
                initializeCardView(currentPlayer.drawDeck, player2DrawDeck, false, cardImageLoader)
                initializeCardView(currentPlayer.hand, player2Hand, true, cardImageLoader)
                //player2DeckSizeText.text = currentPlayer.drawDeck.size.toString() + "/ 20"
                //player2HandSizeText.text = currentPlayer.hand.size.toString() + "/ 10"
            }
        }
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }
    }

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        //val currentPlayer = game.currentPlayer()

        val currentPlayer = game.currentPlayer()
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }
        currentPlayerText.text = "current player: " + currentPlayer.name

        redrawButton.isDisabled = !rootService.playerActionService.canRedrawHand()
        passButton.isDisabled = !rootService.playerActionService.canPass()
    }

    private fun flipCardView(cardView: CardView) {
        when (cardView.currentSide) {
            CardView.CardSide.BACK -> cardView.showFront()
            CardView.CardSide.FRONT -> cardView.showBack()
        }
    }

    private fun moveCardView(cardView: CardView, linearLayout: LinearLayout<CardView>, cardStack: CardStack<CardView>, flip: Boolean) {

        if (flip) flipCardView(cardView)

        cardView.removeFromParent()
        //cardStack.remove(cardView)
        linearLayout.add(cardView)
    }

    private fun shuffleCardView(handCards : LinearLayout<CardView>, cardStack: CardStack<CardView>) {

        val game = rootService.currentGame
        checkNotNull(game)

        handCards.forEach { cardView ->
            cardView.removeFromParent()
            cardStack.add(cardView)
        }

        game.currentPlayer().drawDeck.forEach { card ->
            val cardIndex = game.currentPlayer().drawDeck.indexOf(card)
            val cardView = cardMap.forward(Card(card.suit, card.value))
        }

    }
}
