package gui

import service.RootService
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.style.BlurFilter
import tools.aqua.bgw.visual.ImageVisual

/**
 * Implementation of the BGW [BoardGameApplication] for the game application
 */
class UpAndDownApplication : BoardGameApplication("UpAndDownGame"), Refreshable {

    private val rootService = RootService()

    private val gameScene = GameScene(rootService)

    private val mainMenuScene = MainMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    private val waitingScene = WaitingScene(rootService).apply {
        invisibleButton.onMouseClicked = {
            gameScene.refreshAfterStartTurn()
            this@UpAndDownApplication.hideMenuScene()
        }
    }

    private val resultScene = ResultScene(rootService).apply {
        restartButton.onMouseClicked = {
            this@UpAndDownApplication.showMenuScene(mainMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    init {
        rootService.addRefreshables(
            this,
            gameScene,
            mainMenuScene,
            waitingScene,
            resultScene
        )

        rootService.gameService.startNewGame("Kassel", "Duisburg")
        val background = ImageVisual("background1.jpeg")
        background.filters.blur = BlurFilter(5.0)
        gameScene.background = background
        this.showGameScene(gameScene)
        this.showMenuScene(mainMenuScene, 0)
    }

    override fun refreshAfterGameStart() {
        val delay = DelayAnimation(500)
        delay.onFinished = {
            hideMenuScene()
        }
        gameScene.playAnimation(delay)
    }

    override fun refreshAfterSwitchPlayerTurn() {
        val delay = DelayAnimation(500)
        delay.onFinished = {
            showMenuScene(waitingScene)
        }
        gameScene.playAnimation(delay)
    }

    override fun refreshAfterEndGame() {
        showMenuScene(resultScene)
    }
}
