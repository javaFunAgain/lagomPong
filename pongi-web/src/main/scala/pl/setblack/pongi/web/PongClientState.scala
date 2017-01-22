package pl.setblack.pongi.web

import pl.setblack.pongi.web.api.GameInfo

/**
  * Created by jarek on 1/22/17.
  */
case class PongClientState(
                          welcome  : Option[Welcome.WelcomeState],
                          games : Option[Seq[GameInfo]]

                          ) {
  def this()  = this(Some(Welcome.WelcomeState()), None)


  def toGamesList(games: Seq[GameInfo]): PongClientState = {
    PongClientState(None, Some(games))
  }

}


