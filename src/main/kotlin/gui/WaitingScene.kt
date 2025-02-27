package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * this class is used for showing the waiting menu when it's the
 * next player's turn to play.
 * [okButton] can be pressed to continue the game
 */
class WaitingScene(private val rootService: RootService) : MenuScene(1920, 1080,
    ColorVisual(0,0,0)), Refreshable {


    private val currentPlayer = Label(
        width = 800, height = 120,
        posX = 560, posY = 400,
        text = "",
        alignment = Alignment.CENTER,
        font = Font(size = 72, Color.WHITE)
    )

    val invisibleButton = Button(
        width = 1920, height = 1080,
        posX = 0, posY = 0,
        text = "",
    ).apply {
        visual = ColorVisual(Color.TRANSPARENT)
    }

    init {
        opacity = .5
        addComponents(
            currentPlayer,
            invisibleButton
        )
    }

    override fun refreshAfterSwitchPlayerTurn() {
        val game = rootService.currentGame
        checkNotNull(game)

        currentPlayer.text = "It's ${game.currentPlayer().name}'s turn"
    }

}
