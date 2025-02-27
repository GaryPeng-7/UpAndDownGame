package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * this class is used for showing the end game menu scene after the game is ended.
 * the winner will be shown on the menu
 * [restartButton] can be pressed to restart the game
 * [quitButton] can be pressed to quit the game
 */
class ResultScene(private val rootService: RootService) : MenuScene(1920, 1080,
    ColorVisual(Color.black)), Refreshable {

     private val result = Label(
         width = 500, height = 80,
         posX = 730, posY = 100,
         text = "",
         font = Font(size = 40, Color.black),
         visual = ColorVisual(255,255,255,0.5)
    )

    val quitButton = Button(
        width = 200, height = 50,
        posX = 760, posY = 300,
        text = "Quit",
        font = Font(size = 28)
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }

    val restartButton = Button(
        width = 200, height = 50,
        posX = 1000, posY = 300,
        text = "Restart",
        font = Font(size = 28)
    ).apply {
        visual = ColorVisual(136, 221, 136)
    }


    init {
        opacity = .5
        addComponents(
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
