package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * this class is used for showing the waiting menu when it's the
 * next player's turn to play.
 * [okButton] can be pressed to continue the game
 */
class WaitingScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {


    private val currentPlayer = Label(
        width = 400, height = 100, posX = 750, posY = 50,
        text = "",
        alignment = Alignment.CENTER,
        font = Font(size = 40)
    )

    val okButton = Button(
        width = 300, height = 300,
        posX = 800, posY = 300,
        text = "OK",
        alignment = Alignment.CENTER
        ).apply {
        visual = ColorVisual(221, 136, 136)
    }

    init {
        opacity = .5
        addComponents(
            currentPlayer,
            okButton
        )
    }

    //private fun UpAndDownGame.currentPlayer() : String = currentPlayer().name

    override fun refreshAfterSwitchPlayerTurn() {
        val game = rootService.currentGame
        checkNotNull(game)

        currentPlayer.text = "It's ${game.currentPlayer().name}'s turn"
    }

}
