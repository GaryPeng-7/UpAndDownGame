package service

import entity.UpAndDownGame

/**
 * Diese Klasse dient als eine Bruecke zwischen den Serviceklassen und
 * dem Entity-Schicht
 * @property gameService
 * @property playerActionService
 * @property currentGame
 */
class RootService {

    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    var currentGame : UpAndDownGame? = null

}
