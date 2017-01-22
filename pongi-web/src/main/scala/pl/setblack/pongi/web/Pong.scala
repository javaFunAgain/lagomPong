package pl.setblack.pongi.web

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactElement}
import japgolly.scalajs.react.vdom.prefix_<^._

/**
  * Created by jarek on 1/22/17.
  */
class Pong {

}

object Pong {

  private var mainBackend:Option[PongBackend] = None

  def getMainBackend = mainBackend

  class PongBackend($: BackendScope[PongClientState, PongClientState]) {

    mainBackend = Some(this)

    def render(state: PongClientState):ReactElement = {
      println(s"I have such state: $state")
      val elem : Option[ReactElement] = state.welcome.map( ws => {
        Welcome.page()
      })

        elem.getOrElse(<.p("empty one"))
    }

    def toGameList( list: Seq[String]) = {
      println("new... modding")
      $.modState( ps => ps.toGamesList  ).runNow()
    }

  }

  val  page =
    ReactComponentB[PongClientState]("PongGame")
      .initialState( new PongClientState())
      .backend( new PongBackend(_))
      .renderBackend
      .build

}


