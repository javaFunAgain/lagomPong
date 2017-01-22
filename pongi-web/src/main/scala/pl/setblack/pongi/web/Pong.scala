package pl.setblack.pongi.web

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactElement}
import japgolly.scalajs.react.vdom.prefix_<^._
import pl.setblack.pongi.web.api.ServerApi

/**
  * Created by jarek on 1/22/17.
  */
class Pong {

}

object Pong {
  import scala.concurrent.ExecutionContext.Implicits.global

  private var mainBackend:Option[PongBackend] = None

  def getMainBackend = mainBackend

  class PongBackend($: BackendScope[PongClientState, PongClientState]) {

    mainBackend = Some(this)

    private val api = new ServerApi

    def server = api

    def render(state: PongClientState):ReactElement = {
      println(s"I have such state: $state")
      val elem : Option[ReactElement] = state.welcome.map( ws => {
        Welcome.page()
      })
        elem
          .orElse(state.games.map( list => GamesList.page(list)))
          .getOrElse(<.p("empty one"))
    }

    def toGameList( ) = {
      server.listGames.onComplete( games => {
        $.modState( ps => ps.toGamesList(games.get)  ).runNow()
      })
    }

  }

  val  page =
    ReactComponentB[PongClientState]("PongGame")
      .initialState( new PongClientState())
      .backend( new PongBackend(_))
      .renderBackend
      .build

}


