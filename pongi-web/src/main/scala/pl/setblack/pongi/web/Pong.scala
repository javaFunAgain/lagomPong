package pl.setblack.pongi.web

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactElement}
import japgolly.scalajs.react.vdom.prefix_<^._
import pl.setblack.pongi.web.api.{GameInfo, ServerApi}
import pl.setblack.pongi.web.pong.GameState

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
            .orElse( state.currentGame.map( game =>PlayField.GameStateComponent(game.state) ))
          .getOrElse(<.p("empty one"))
    }

    def toGameList( ) = {
      server.listGames.onComplete( games => {
        $.modState( ps => ps.toGamesList(games.get)  ).runNow()
      })
    }

    def toGame(uuid: String, state: GameState ) = {
      println(s"going to game ${uuid}")
      $.modState( ps => ps.toGame(uuid, state)).runNow()
    }

    def joinGame(uuid : String) = {
      api.joinGame(uuid).onComplete( state => toGame(uuid,state.get))
    }

    def createGame(name : String ) = {
        api.createGame(name).onComplete( _ => toGameList())
    }

  }

  val  page =
    ReactComponentB[PongClientState]("PongGame")
      .initialState( new PongClientState())
      .backend( new PongBackend(_))
      .renderBackend
      .build

}


