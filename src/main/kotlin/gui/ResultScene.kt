package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * this class is used for showing the end game menu scene after the game is ended.
 * the winner will be shown on the menu
 * [restartButton] can be pressed to restart the game
 * [quitButton] can be pressed to quit the game
 */
class ResultScene(private val rootService: RootService) : MenuScene(400, 1080), Refreshable {

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Result",
        font = Font(size = 22)
    )

     private val result = Label(
        width = 300, height = 50,
        posX = 50, posY = 125,
        text = "",
        font = Font(size = 36)
    )



    val quitButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 240,
        text = "Quit"
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }

    val restartButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 240,
        text = "Restart"
    ).apply {
        visual = ColorVisual(136, 221, 136)
    }


    init {
        opacity = .5
        addComponents(
            headlineLabel,
            result,
            restartButton, quitButton
        )
    }

    override fun refreshAfterEndGame() {
        val game = rootService.currentGame
        checkNotNull(game)

        when (game.winner) {
            0 -> result.text = game.player1.name + " won"
            1 -> result.text = game.player2.name + " won"
            2 -> result.text = "it's a tie"
        }
    }
}
