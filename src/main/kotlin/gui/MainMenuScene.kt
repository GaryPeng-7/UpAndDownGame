package gui

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * this class is used for showing the starting game menu scene
 * after the game is initialized and after "restart" is clicked
 * in [ResultScene].
 * [startButton] can be pressed to start the game
 * [quitButton] can be pressed to quit the game
 */
class MainMenuScene(private val rootService: RootService) : MenuScene(400, 1080), Refreshable {

    private val startButton = Button(
        width = 140, height = 35,
        posX = 210, posY = 240,
        text = "Start"
    )

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Start New Game",
        font = Font(size = 22)
    )

    private val p1Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 125,
        text = "Player 1:"
    )

    private val p1Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 125,
        text = "Kassel"
    )

    private val p2Label = Label(
        width = 100, height = 35,
        posX = 50, posY = 170,
        text = "Player 2:"
    )

    private val p2Input: TextField = TextField(
        width = 200, height = 35,
        posX = 150, posY = 170,
        text = "Duisburg",
        prompt = "please enter..."
    )

    val quitButton = Button(
        width = 140, height = 35,
        posX = 50, posY = 240,
        text = "Quit"
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
