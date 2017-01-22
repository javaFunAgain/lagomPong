package pl.setblack.pongi.web.pong

/**
  * Created by jarek on 1/18/17.
  */
class GameState {


  val players : Tuple2[Player, Player] = Tuple2(defaultPlayer(1), defaultPlayer(2))

  val ball = Ball(0.5f, 0.5f)

  private def defaultPlayer(i: Int) = {
    val paddle = Paddle(0f + 1f*(i-1), 0.5f)
    Player(0, s"Player:${i-1}","lele", paddle)
  }
}

