package pl.setblack.pongi.web.api

import org.scalajs.dom

import scala.scalajs.js.annotation.JSExportAll
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryPromise, jQuery}
import pl.setblack.pongi.web.Pong
import pl.setblack.pongi.web.pong.GameState
import upickle.Js

import scala.scalajs.js
import js.JSConverters._
import upickle.default._

import scala.collection.mutable
import scala.concurrent.{Future, Promise}
import scala.scalajs.js.JSON


/**
  * Created by jarek on 1/22/17.
  */
class ServerApi {
  import scala.concurrent.ExecutionContext.Implicits.global

  var session:Option[Session] = None

  case class NewUser(password: String)

  case class RegisterResult(problem: String, ok: Boolean)

  case class Session(userId: String, uuid: String, expirationTime: Vector[Int])

  def registerUser(login: String, pass: String): Future[RegisterResult] = {
    val data = NewUser(pass)
    val result = Promise[RegisterResult]
    jQuery.post("/api/users/add/" + login, write[NewUser](data).asInstanceOf[js.Any], null, "text").done(
      (response: String) => {
        result.success(read[RegisterResult](response))
      }
    )
    result.future
  }

  def loginUser(login: String, pass: String): Future[Option[Session]] = {
    val data = NewUser(pass)
    val result = Promise[Option[Session]]
    jQuery.post("/api/users/login/" + login, write[NewUser](data).asInstanceOf[js.Any], null, "text").done(
      (response: String) => {
        val newSession  = read[Session](response)
        this.session = Some(newSession)
        result.success( this.session)
      }
    )
    result.future
  }

  def joinGame(gameId : String) : Future[Option[GameState]] = {
    val host = dom.window.location.host
    val changedPort = host.replace("9001","9000")
    var wsUrl  = s"ws://${changedPort}/api/games/stream/${gameId}"
    val socket = new dom.WebSocket(wsUrl)
    socket.onmessage = {
      (e: dom.MessageEvent) =>
        readOptional(e.data.toString, read[GameState](_)).foreach( newState =>
        Pong.getMainBackend.foreach(back => back.toGame(gameId, newState)  ))
    }
    socket.onopen = { (e: dom.Event) =>

    }


    doAjax("/api/games/join", Some(gameId))
      .map( str =>
        readOptional(str, read[GameState](_)))
  }

  def movePaddle(gameId : String, targetY : Double) : Future[Boolean] = {
    doAjax("/api/games/move/"+gameId, Some(targetY.toString))
      .map( str => true)
  }

  def getGame(gameId : String) : Future[Option[GameState]] = {
    doAjax("/api/games/"+gameId)
      .map( str => {

        readOptional(str, read[GameState](_))
      })
  }


  private def readOptional[T]( value : String, parser : String=>T):Option[T] = {
    if ( value == "null") {
      None
    } else {
      Some(parser(value))
    }
  }

  def createGame(name : String) : Future[GameInfo] = {
    doAjax("/api/games/create", Some(name))
      .map( str => read[GameInfo](str))
  }


  def listGames() : Future[Seq[GameInfo]] = {
      doAjax("/api/games/games")
      .map( str => read[Seq[GameInfo]](str))
  }

  def  doAjax(url: String, data: Option[String]= None) : Future[String] = {
    val baseSettings = Map(
      "dataType" -> "text",
      "contentType" -> "text/plain",
      "headers" -> createAuthorizationHeader)
    val settinegsMap = data.map ( input => {
      baseSettings ++ Map( "method" -> "POST",
        "data" -> input)
    }).getOrElse(baseSettings)

    val settings :JQueryAjaxSettings =
      settinegsMap
        .toJSDictionary
        .asInstanceOf[JQueryAjaxSettings]


    val result = Promise[String]
    jQuery.ajax(url, settings)
      .asInstanceOf[JQueryPromise]
      .done( (response:String) => {
          result.success(response)
      })

    result.future
  }

  private def createAuthorizationHeader = {
    js.Dictionary("Authorization" -> ("Bearer " + session.map(s => s.uuid).getOrElse("")))
  }
}