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

    private var selectedCardView = -1

    private val centerDeck1 = CardStack<CardView>(posX = 800, posY = 540).apply {
        onMouseClicked = {
            require(selectedCardView != -1) {"please select the card in your hand first"}
            val game = rootService.currentGame
            checkNotNull(game)
            val card = numMap.forward(selectedCardView)
            if (game.currentPlayer().hand.contains(card)) {
                game.let {
                    rootService.playerActionService.playCard(card, 0)
                }
            }
        }
    }
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
        width = 900,
        height = 200,
        spacing = -60
    )

    private val centerDeck2 = CardStack<CardView>(posX = 990, posY = 540).apply {
        onMouseClicked = {
            require(selectedCardView != -1) {"please select the card in your hand first"}
            val game = rootService.currentGame
            checkNotNull(game)
            val card = numMap.forward(selectedCardView)
            if (game.currentPlayer().hand.contains(card)) {
                game.let {
                    rootService.playerActionService.playCard(card, 1)
                }
            }
        }
    }
    private val player2DrawDeck = CardStack<CardView>(posX = 1390, posY = 540).apply {
        onMouseClicked = {
            rootService.currentGame?.let { game ->
                rootService.playerActionService.drawCard()
            }
        }
    }
    private val player2Hand = LinearLayout<CardView>(
        posX = 1160,
        posY = 880,
        width = 900,
        height = 200,
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


    private val redrawButton = Button(
        width = 140, height = 35,
        posX = 900, posY = 880,
        text = "redraw",
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
        posX = 250, posY = 800,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )
    private val player2DeckSizeText = Label(
        width = 200, height = 60,
        posX = 1240, posY = 480,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )
    private val player2HandSizeText = Label(
        width = 200, height = 60,
        posX = 1190, posY = 800,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )



    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()
    private val numMap: BidirectionalMap<Int, Card> = BidirectionalMap()

    init {
        background = ColorVisual(108, 168, 59)

        addComponents(
            centerDeck1, player1Hand, player1DrawDeck,
            centerDeck2, player2Hand, player2DrawDeck,
            currentPlayerText, player1Text, player2Text,
            redrawButton, passButton,
            player1DeckSizeText,player1HandSizeText,
            player2DeckSizeText,player2HandSizeText
        )
    }

    override fun refreshAfterGameStart() {
        val game = rootService.currentGame
        checkNotNull(game) { "The game has not started yet." }

        cardMap.clear()
        numMap.clear()


        val cardImageLoader = CardImageLoader()
        val currentPlayer = game.currentPlayer()

        initializeCardView(game.centerDeck1, centerDeck1, true, cardImageLoader)
        initializeCardView(game.player1.hand, player1Hand, false, cardImageLoader)
        game.player1.hand.forEach { card ->
            cardMap.forward(card).isFocusable = true
        }
        initializeCardView(game.player1.drawDeck, player1DrawDeck, false, cardImageLoader)
        initializeCardView(game.centerDeck2, centerDeck2, true, cardImageLoader)
        initializeCardView(game.player2.hand, player2Hand, false, cardImageLoader)
        initializeCardView(game.player2.drawDeck, player2DrawDeck, false, cardImageLoader)


        currentPlayerText.text = "current player: " + currentPlayer.name
        player1Text.text = "player1: " + game.player1.name
        player2Text.text = "player2: " + game.player2.name
        player1DeckSizeText.text = game.player1.drawDeck.size.toString() + "/ 20"
        player1HandSizeText.text = game.player1.hand.size.toString() + "/ 10"
        player2DeckSizeText.text = game.player2.drawDeck.size.toString() + "/ 20"
        player2HandSizeText.text = game.player2.hand.size.toString() + "/ 10"

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
                numMap.removeBackward(card)
            }
            if (flip) {
                //cardView.isFocusable = true
                cardView.showFront()
            }
            cardStack.add(cardView)
            cardMap.add(card to cardView)
            numMap.add((card.suit.ordinal) * 100 + (card.value.ordinal) to card)
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
            ).apply {
                this.isFocusable = true
                onMouseClicked = {
                    //if (selectedCardView == -1){
                        val card = cardMap.backward(this)
                        selectedCardView = (card.suit.ordinal) * 100 + (card.value.ordinal)

                }
            }

            if (cardMap.containsForward(card)){
                cardMap.removeForward(card)
                numMap.removeBackward(card)
            }
            if (flip) {
                cardView.showFront()
            }
            linearLayout.add(cardView)
            cardMap.add(card to cardView)
            numMap.add((card.suit.ordinal) * 100 + (card.value.ordinal) to card)
        }

    }

    override fun refreshAfterPlayCard(centerDeck: Int) {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        val currentPlayer = game .currentPlayer()

        when (game.getCenterDeck(centerDeck)) {
            game.centerDeck1 -> {
                val cardView = cardMap.forward(game.getCenterDeck(centerDeck).last())
                val cardImageLoader = CardImageLoader()
                cardView.removeFromParent()
                initializeCardView(game.getCenterDeck(centerDeck), centerDeck1, true, cardImageLoader)
            }
            game.centerDeck2 -> {
                val cardView = cardMap.forward(game.getCenterDeck(centerDeck).last())
                val cardImageLoader = CardImageLoader()
                cardView.removeFromParent()
                initializeCardView(game.getCenterDeck(centerDeck), centerDeck2, true, cardImageLoader)
                //centerDeck2.add(cardView)
            }
        }
        player1DeckSizeText.text = game.player1.drawDeck.size.toString() + "/ 20"
        player1HandSizeText.text = game.player1.hand.size.toString() + "/ 10"
        player2DeckSizeText.text = game.player2.drawDeck.size.toString() + "/ 20"
        player2HandSizeText.text = game.player2.hand.size.toString() + "/ 10"
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }
    }

    override fun refreshAfterDrawCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        val currentPlayer = game .currentPlayer()

        when (currentPlayer) {
            game.player1 -> {
                val cardView = cardMap.forward(currentPlayer.hand.last())
                val cardImageLoader = CardImageLoader()
                cardView.removeFromParent()
                initializeCardView(game.player1.hand, player1Hand, true, cardImageLoader)
            }
            game.player2 -> {
                val cardView = cardMap.forward(currentPlayer.hand.last())
                val cardImageLoader = CardImageLoader()
                cardView.removeFromParent()
                initializeCardView(game.player2.hand, player2Hand, true, cardImageLoader)
                }
            }
        player1DeckSizeText.text = game.player1.drawDeck.size.toString() + "/ 20"
        player1HandSizeText.text = game.player1.hand.size.toString() + "/ 10"
        player2DeckSizeText.text = game.player2.drawDeck.size.toString() + "/ 20"
        player2HandSizeText.text = game.player2.hand.size.toString() + "/ 10"
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
            }
            game.player2 -> {
                initializeCardView(currentPlayer.drawDeck, player2DrawDeck, false, cardImageLoader)
                initializeCardView(currentPlayer.hand, player2Hand, true, cardImageLoader)
            }
        }
        player1DeckSizeText.text = game.player1.drawDeck.size.toString() + "/ 20"
        player1HandSizeText.text = game.player1.hand.size.toString() + "/ 10"
        player2DeckSizeText.text = game.player2.drawDeck.size.toString() + "/ 20"
        player2HandSizeText.text = game.player2.hand.size.toString() + "/ 10"
        currentPlayer.hand.forEach { card ->
            flipCardView(cardMap.forward(card))
        }
    }

    override fun refreshAfterAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.currentPlayer().hand.forEach { card ->
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
}
