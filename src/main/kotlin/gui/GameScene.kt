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
                game.let {
                    rootService.playerActionService.playCard(checkNotNull(selectedCard), 0)
                }
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
                game.let {
                    rootService.playerActionService.playCard(checkNotNull(selectedCard), 1)
                }
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
        width = 360, height = 100,
        posX = 770, posY = 400,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 40)
    )
    private val currentPlayerText = Label(
        width = 360, height = 100,
        posX = 770, posY = 150,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 32)
    )
    private val player1Text = Label(
        width = 300, height = 80,
        posX = 300, posY = 300,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 28)
    )
    private val player2Text = Label(
        width = 300, height = 80,
        posX = 1300, posY = 300,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 28)
    )
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
        posX = 1350, posY = 480,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )
    private val player2HandSizeText = Label(
        width = 200, height = 60,
        posX = 1280, posY = 800,
        alignment = Alignment.CENTER,
        text = "",
        font = Font(size = 20)
    )

    // the buttons
    private val redrawButton = Button(
        width = 160, height = 75,
        posX = 880, posY = 800,
        text = "redraw",
        font = Font(size = 20)
    ).apply {
        visual = ColorVisual(221, 136, 136)
        onMouseClicked = {
            rootService.currentGame?.let { _ ->
                rootService.playerActionService.redrawHand()
            }
        }
    }
    private val passButton = Button(
        width = 160, height = 75,
        posX = 880, posY = 900,
        text = "pass",
        font = Font(size = 20)
    ).apply {
        visual = ColorVisual(221, 136, 136)
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
        initializeCardView(game.player1.hand, player1Hand, cardImageLoader)
        initializeCardView(game.player1.drawDeck, player1DrawDeck, false, cardImageLoader)
        initializeCardView(game.centerDeck2, centerDeck2, true, cardImageLoader)
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
        flipHand()
    }

    override fun refreshAfterDrawCard() {
        val game = rootService.currentGame
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
            }
            game.player2 -> {
                player2Hand.add(cardView)
                }
            }
        refreshNumber()
        flipHand()
    }

    override fun refreshAfterRedrawHand() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }

        when (val currentPlayer = game.currentPlayer()) {
            game.player1 -> {
                shuffleView(currentPlayer.hand, currentPlayer.drawDeck, player1Hand, player1DrawDeck)
            }
            game.player2 -> {
                shuffleView(currentPlayer.hand, currentPlayer.drawDeck, player2Hand, player2DrawDeck)
            }
        }
        refreshNumber()
        flipHand()
    }

    override fun refreshAfterAfterPass() {
        val game = rootService.currentGame
        checkNotNull(game)
        flipHand()
    }

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "Game is not found." }


        val currentPlayer = game.currentPlayer()
        flipHand(currentPlayer)
        currentPlayerText.text = "current player: " + currentPlayer.name

        when (currentPlayer) {
            game.player1 -> player2DrawDeck.isDisabled
            game.player2 -> player1DeckSizeText.isDisabled
        }
        selectedCard = null
        currentSelectText.text = ""
        buttonEnabled()
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

    // private function to flip the cards in the hand
    private fun flipHand() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.currentPlayer().hand.forEach { card ->
            cardMap.forward(card).flip()
        }
    }

    private fun flipHand(player: Player) {
        player.hand.forEach { card ->
            cardMap.forward(card).flip()
        }
    }

    // help function to update the cardviews after the cards were being shuffled
    private fun shuffleView(hand : MutableList<Card>, drawDeck: MutableList<Card>,
                            handView : LinearLayout<CardView>, stackView : CardStack<CardView>) {

        // the iterators for both of the container
        val stackIterator = stackView.iterator()
        val handIterator = handView.iterator()
        // to merge the cards into a bigger list
        val allCards : List<Card> = drawDeck + hand
        // the lists to save the original container contents
        val oriStackList = mutableListOf<CardView>()
        val oriHandList = mutableListOf<CardView>()
        // the list to record the position where the cards really are
        val numList = mutableListOf<Int>()


        // ignore this section
        //numList.forEachIndexed { index, i ->  }
        //for (index in 0..<numList.size step 2) {
        //    val i = numList[index]
        //}



        stackIterator.forEach { cardView ->
            cardView.onMouseClicked = null
            oriStackList.add(cardView)
            val card = cardMap.backward(cardView)
            numList.add(allCards.indexOf(card))
        }

        handIterator.forEach { cardView ->
            cardView.apply {
                onMouseClicked = {
                    selectedCard = cardMap.backward(this)
                    currentSelectText.text = selectedCard.toString()
                }
            }
            oriHandList.add(cardView)
            val card = cardMap.backward(cardView)
            numList.add(allCards.indexOf(card))
        }

        stackView.clear()
        handView.clear()

        // merge the lists and make a connection between the original cardviews
        // and where they should actually go
        val allList = oriStackList + oriHandList
        val correspondMap : MutableMap<Int, CardView> = mutableMapOf()
        for ((counter, num) in numList.withIndex()) {
            correspondMap[num] = allList[counter]
        }
        // sorted the map from 0 in order to loop through it with the right sequence
        // and assign them back to the original container
        val sortedMap = correspondMap.toSortedMap()
        sortedMap.forEach { (num, cardView) ->
            if (num < drawDeck.size) {
                cardView.showBack()
                stackView.add(cardView)
            } else {
                cardView.showFront()
                handView.add(cardView)
            }
        }
    }

    // private function to help track the buttons
    private fun buttonEnabled() {
        if (rootService.playerActionService.canRedrawHand()) {
            redrawButton.visual = ColorVisual(221, 136, 136)
            redrawButton.font = Font(color = Color.BLACK, size =20)
            redrawButton.isDisabled = false
        } else {
            redrawButton.visual = ColorVisual(100, 100, 100)
            redrawButton.font = Font(color = Color.WHITE, size =20)
            redrawButton.isDisabled = true
        }
        if (rootService.playerActionService.canPass()) {
            passButton.visual = ColorVisual(221, 136, 136)
            passButton.font = Font(color = Color.BLACK, size =20)
            passButton.isDisabled = false
        } else {
            passButton.visual = ColorVisual(100, 100, 100)
            passButton.font = Font(color = Color.WHITE, size =20)
            passButton.isDisabled = true
        }
    }
}
