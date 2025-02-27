package service

import entity.UpAndDownGame
import gui.Refreshable

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



    private fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}
