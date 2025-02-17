package entity

class Player(val name : String,
             val hand : MutableList<Card>,
             val drawDeck : MutableList<Card> ) {
}