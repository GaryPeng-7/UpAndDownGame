package gui

import service.*
import entity.*
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * this is the main scene for the whole game, where it will be shown most of the time
 * player 1 is on the left side and player 2 is on the right side
 * each player has their own hand card area, drawpile area
 * there are two additional played piles, which every player can use
 * when the player click on their drawpile, they draw a card
 * when the player click on a card in their hand and then click on one of the centerpile, they can play that card on it
 * if the player is allowed to redraw or pass, then the buttons would be available for them to click on
 */
class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    // variable to translate card and cardview
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()
    // variable to store the clicked card in the hand
    private var selectedCard : Card? = null


    // decks and hands
    private val centerDeck1 = CardStack<CardView>(posX = 800, posY = 540).apply {
        onMouseClicked = {
            requireNotNull(selectedCard) {"please select the card in your hand first"}
            val game = rootService.currentGame
            checkNotNull(game)
            if (game.currentPlayer().hand.contains(selectedCard)) {
                    rootService.playerActionService.playCard(checkNotNull(selectedCard), 0)
            }
        }
    }
    private val player1DrawDeck = CardStack<CardView>(posX = 400, posY = 540).apply {
        onMouseClicked = {
            val game = rootService.currentGame
            checkNotNull(game)
            if  (game.currentPlayer() == game.player1) {
                rootService.currentGame?.let {
                    rootService.playerActionService.drawCard()
                }
            }
        }
    }
    private val player1Hand = LinearLayout<CardView>(
        posX = 0,
        posY = 880,
        width = 900,
        height = 200,
        spacing = -60
    )
    private val centerDeck2 = CardStack<CardView>(posX = 990, posY = 540).apply {
        onMouseClicked = {
            requireNotNull(selectedCard) {"please select the card in your hand first"}
            val game = rootService.currentGame
            checkNotNull(game)
            if (game.currentPlayer().hand.contains(selectedCard)) {
                    rootService.playerActionService.playCard(checkNotNull(selectedCard), 1)
            }
        }
    }
    private val player2DrawDeck = CardStack<CardView>(posX = 1390, posY = 540).apply {
        onMouseClicked = {
            val game = rootService.currentGame
            checkNotNull(game)
            if  (game.currentPlayer() == game.player2) {
                rootService.currentGame?.let {
                    rootService.playerActionService.drawCard()
                }
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

    // the labels
    private val currentSelectText = Label(
        width = 100, height = 50,
        posX = 910, posY = 460,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 40),
        visual = ColorVisual(255,255,255,0.5)
    )
    private val currentPlayerText = Label(
        width = 450, height = 100,
        posX = 740, posY = 100,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 40, Color.WHITE),
        visual = ColorVisual(0,0,0,0.5)
    )
    private val player1Text = Label(
        width = 300, height = 80,
        posX = 300, posY = 300,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 32),
    )
    private val player2Text = Label(
        width = 300, height = 80,
        posX = 1300, posY = 300,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 32)
    )
    private val player1DeckSizeText = Label(
        width = 100, height = 50,
        posX = 410, posY = 480,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 24, Color.WHITE),
        visual = ColorVisual(0,0,0,0.5)
    )
    private val player1HandSizeText = Label(
        width = 100, height = 50,
        posX = 200, posY = 810,
        alignment = Alignment.CENTER,
        font = Font(size = 24, Color.WHITE),
        visual = ColorVisual(0,0,0,0.5)
    )
    private val player2DeckSizeText = Label(
        width = 100, height = 50,
        posX = 1400, posY = 480,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 24, Color.WHITE),
        visual = ColorVisual(0,0,0,0.5)
    )
    private val player2HandSizeText = Label(
        width = 100, height = 50,
        posX = 1360, posY = 810,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 24, Color.WHITE),
        visual = ColorVisual(0,0,0,0.5)
    )

    // the buttons
    private val redrawButton = Button(
        width = 180, height = 60,
        posX = 870, posY = 800,
        text = "redraw",
        font = Font(size = 30)
    ).apply {
        visual = ColorVisual(255,255,255)
        onMouseClicked = {
            rootService.currentGame?.let { _ ->
                rootService.playerActionService.redrawHand()
            }
        }
    }
    private val passButton = Button(
        width = 180, height = 60,
        posX = 870, posY = 900,
        text = "pass",
        font = Font(size = 30)
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let { _ ->
                rootService.playerActionService.pass()
            }
        }
    }

    init {
        background = ColorVisual(108, 168, 59)

        addComponents(
            centerDeck1, player1Hand, player1DrawDeck,
            centerDeck2, player2Hand, player2DrawDeck,
            currentPlayerText, player1Text, player2Text,
            redrawButton, passButton, currentSelectText,
            player1DeckSizeText,player1HandSizeText,
            player2DeckSizeText,player2HandSizeText
        )
    }

    override fun refreshAfterGameStart() {
        val game = rootService.currentGame
        checkNotNull(game) { "The game has not started yet." }

        cardMap.clear()

        val cardImageLoader = CardImageLoader()
        val currentPlayer = game.currentPlayer()

        // to initialise all the cardviews
        initializeCardView(game.centerDeck1, centerDeck1, true, cardImageLoader)
        initializeCardView(game.centerDeck2, centerDeck2, true, cardImageLoader)
        initializeCardView(game.player1.hand, player1Hand, cardImageLoader)
        initializeCardView(game.player1.drawDeck, player1DrawDeck, false, cardImageLoader)
        initializeCardView(game.player2.hand, player2Hand, cardImageLoader)
        initializeCardView(game.player2.drawDeck, player2DrawDeck, false, cardImageLoader)


        currentPlayerText.text = "current player: " + currentPlayer.name
        player1Text.text = "player1: " + game.player1.name
        player2Text.text = "player2: " + game.player2.name

        refreshNumber()
        flipHand(currentPlayer)
        buttonEnabled()
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
                                   cardImageLoader: CardImageLoader) {
        linearLayout.clear()


        cards.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage,
            ).apply {
                onMouseClicked = {
                    selectedCard = cardMap.backward(this)
                    currentSelectText.text = selectedCard.toString()
                }
            }
            linearLayout.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    override fun refreshAfterPlayCard(centerDeck: Int) {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        val cardView = cardMap.forward(game.getCenterDeck(centerDeck).last())
        cardView.onMouseClicked = null
        cardView.removeFromParent()

        when (game.getCenterDeck(centerDeck)) {
            game.centerDeck1 -> centerDeck1.add(cardView)
            game.centerDeck2 -> centerDeck2.add(cardView)
        }

        refreshNumber()
        flipHand(game.currentPlayer())
    }

    override fun refreshAfterDrawCard() {
        val game = rootService.currentGame
        val delay = DelayAnimation(500)
        checkNotNull(game) { "Game is not found." }

        val currentPlayer = game .currentPlayer()
        val cardView = cardMap.forward(currentPlayer.hand.last())
        cardView.apply {
            cardView.showFront()
            onMouseClicked = {
                selectedCard = cardMap.backward(this)
                currentSelectText.text = selectedCard.toString()
            }
        }
        cardView.removeFromParent()


        when (currentPlayer) {
            game.player1 -> {
                player1Hand.add(cardView)
                delay.onFinished = {
                    flipHand(game.player1)
                }
            }
            game.player2 -> {
                player2Hand.add(cardView)
                delay.onFinished = {
                    flipHand(game.player2)
                }
            }
        }
        playAnimation(delay)
        refreshNumber()
    }

    override fun refreshAfterRedrawHand() {
        val game = rootService.currentGame
        val delay = DelayAnimation(1000)
        checkNotNull(game) { "Game is not found." }


        when (val currentPlayer = game.currentPlayer()) {
            game.player1 -> {
                shuffleView(currentPlayer.hand, currentPlayer.drawDeck, player1Hand, player1DrawDeck)
                delay.onFinished = {
                    flipHand(game.player1)
                }
            }
            game.player2 -> {
                shuffleView(currentPlayer.hand, currentPlayer.drawDeck, player2Hand, player2DrawDeck)
                delay.onFinished = {
                    flipHand(game.player1)
                }
            }
        }
        playAnimation(delay)
        refreshNumber()
    }

    override fun refreshAfterAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game)
        flipHand(game.currentPlayer())
    }

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }


        val currentPlayer = game.currentPlayer()

        currentPlayerText.text = "current player: " + currentPlayer.name
        selectedCard = null
        currentSelectText.text = ""
        buttonEnabled()
        flipHand(currentPlayer)
    }

    // private function to update the numbers
    private fun refreshNumber() {
        val game = rootService.currentGame
        checkNotNull(game)
        player1DeckSizeText.text = game.player1.drawDeck.size.toString() + "/ 20"
        player1HandSizeText.text = game.player1.hand.size.toString() + "/ 10"
        player2DeckSizeText.text = game.player2.drawDeck.size.toString() + "/ 20"
        player2HandSizeText.text = game.player2.hand.size.toString() + "/ 10"
    }

    private fun flipHand(player: Player) {
        player.hand.forEach { card ->
            cardMap.forward(card).flip()
        }
    }

    // help function to update the cardviews after the cards were being shuffled
    private fun shuffleView(hand : MutableList<Card>, drawDeck: MutableList<Card>,
                            handView : LinearLayout<CardView>, stackView : CardStack<CardView>) {

        handView.clear()
        stackView.clear()
        hand.forEach { card ->
            val cardView = cardMap.forward(card).apply {
                this.showFront()
                onMouseClicked = {
                    selectedCard = cardMap.backward(this)
                    currentSelectText.text = selectedCard.toString()
                }
            }
            handView.add(cardView)
        }

        drawDeck.forEach { card ->
            val cardView = cardMap.forward(card)
            cardView.showBack()
            cardView.onMouseClicked = null
            stackView.add(cardView)
        }
    }

    // private function to help track the buttons
    private fun buttonEnabled() {
        if (rootService.playerActionService.canRedrawHand()) {
            redrawButton.visual = ColorVisual(255,255,255)
            redrawButton.font = Font(color = Color.BLACK, size =30)
            redrawButton.isDisabled = false
        } else {
            redrawButton.visual = ColorVisual(50, 50, 50)
            redrawButton.font = Font(color = Color.WHITE, size =30)
            redrawButton.isDisabled = true
        }
        if (rootService.playerActionService.canPass()) {
            redrawButton.visual = ColorVisual(255,255,255)
            passButton.font = Font(color = Color.BLACK, size =30)
            passButton.isDisabled = false
        } else {
            passButton.visual = ColorVisual(50, 50, 50)
            passButton.font = Font(color = Color.WHITE, size =30)
            passButton.isDisabled = true
        }
    }
}
