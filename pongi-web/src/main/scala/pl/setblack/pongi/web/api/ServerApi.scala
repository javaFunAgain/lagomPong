package pl.setblack.pongi.web.api

import scala.scalajs.js.annotation.JSExportAll
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryPromise, jQuery}
import upickle.Js

import scala.scalajs.js
import upickle.default._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js.JSON


/**
  * Created by jarek on 1/22/17.
  */
class ServerApi {
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


  def listGames() : Future[Seq[GameInfo]] = {
    val settings :JQueryAjaxSettings = js.Dictionary(
      "dataType" -> "text",
      "headers" -> js.Dictionary( "Authorization"-> ("Bearer "+ session.map( s=>s.uuid).getOrElse("")))
    ).asInstanceOf[JQueryAjaxSettings]

    val result = Promise[Seq[GameInfo]]
    jQuery.ajax("/api/games/games", settings)
      .asInstanceOf[JQueryPromise]
      .done( (response:String) => {
          val games = read[Seq[GameInfo]](response)
          result.success(games)
      })

    result.future
  }
}