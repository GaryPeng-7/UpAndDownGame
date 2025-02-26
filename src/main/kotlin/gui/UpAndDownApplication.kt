package gui

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

class UpAndDownApplication : BoardGameApplication("UpAndDownGame"), Refreshable {

    private val rootService = RootService()

    private val gameScene = GameScene(rootService)

    private val mainMenuScene = MainMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    private val waitingScene = WaitingScene(rootService).apply {
        okButton.onMouseClicked = {
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
            waitingScene
        )

        rootService.gameService.startNewGame("Kassel", "Duisburg")
        this.showGameScene(gameScene)
        this.showMenuScene(mainMenuScene, 0)
    }

    override fun refreshAfterGameStart() {
        this.hideMenuScene()
    }

    override fun refreshAfterSwitchPlayerTurn() {
        this.showMenuScene(waitingScene)

    }

    override fun refreshAfterEndGame() {
        this.showMenuScene(resultScene)
    }
}

