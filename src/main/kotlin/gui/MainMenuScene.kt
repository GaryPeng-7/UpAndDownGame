package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * this class is used for showing the starting game menu scene
 * after the game is initialized and after "restart" is clicked
 * in [ResultScene].
 * [startButton] can be pressed to start the game
 * [quitButton] can be pressed to quit the game
 */
class MainMenuScene(private val rootService: RootService) : MenuScene(1920, 1080,
    ColorVisual(Color.black)), Refreshable {

    private val startButton = Button(
        width = 200, height = 50,
        posX = 1010, posY = 440,
        text = "Start",
        font = Font(size = 28)
    )

    private val headlineLabel = Label(
        width = 800, height = 100,
        posX = 600, posY = 100,
        text = "Welcome to UpAndDownGame",
        font = Font(size = 40, Color.white),
        visual = ColorVisual(0,0,0)
    )

    private val p1Label = Label(
        width = 150, height = 65,
        posX = 750, posY = 240,
        text = "Player 1:",
        font = Font(size = 28,Color.white)
    )

    private val p1Input: TextField = TextField(
        width = 300, height = 65,
        posX = 910, posY = 240,
        text = "Kassel",
        font = Font(size = 28)
    )

    private val p2Label = Label(
        width = 150, height = 65,
        posX = 750, posY = 330,
        text = "Player 2:",
        font = Font(size = 28 ,Color.white)
    )

    private val p2Input: TextField = TextField(
        width = 300, height = 65,
        posX = 910, posY = 330,
        text = "Duisburg",
        font = Font(size = 28)
    )


    val quitButton = Button(
        width = 200, height = 50,
        posX = 760, posY = 440,
        text = "Quit",
        font = Font(size = 28)
    ).apply {
        visual = ColorVisual(221, 136, 136)
    }


    init {
        opacity = .5
        addComponents(
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            startButton, quitButton
        )

        p1Input.onKeyPressed = {
                startButton.isDisabled = p1Input.text.isBlank() || p2Input.text.isBlank()
            }

        p2Input.onKeyPressed = {
                startButton.isDisabled = p1Input.text.isBlank() || p2Input.text.isBlank()
            }

        startButton.apply {
            visual = ColorVisual(136, 221, 136)
            onMouseClicked = {
                rootService.gameService.startNewGame(
                    p1Input.text.trim(),
                    p2Input.text.trim()
                )
            }
        }
    }
}
