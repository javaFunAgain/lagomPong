package pl.setblack.pongi.web

/**
  * Created by jarek on 1/22/17.
  */
case class PongClientState(
                          welcome  : Option[Welcome.WelcomeState],
                          games : Option[Long]

                          ) {
  def this()  = this(Some(Welcome.WelcomeState()), None)


  def toGamesList(): PongClientState = {
    PongClientState(None, Some(42))
  }

}


