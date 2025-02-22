package service

import entity.UpAndDownGame

class RootService() {

    val gameService = GameService(this)
    val playActionService = PlayActionService(this)

    var currentGame : UpAndDownGame? = null

}