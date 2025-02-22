package service

import entity.UpAndDownGame

/**
 * Diese Klasse dient als eine Bruecke zwischen den Serviceklassen und
 * dem Entity-Schicht
 * @property gameService
 * @property playActionService
 * @property currentGame
 */
class RootService() {

    val gameService = GameService(this)
    val playActionService = PlayActionService(this)

    var currentGame : UpAndDownGame? = null

}