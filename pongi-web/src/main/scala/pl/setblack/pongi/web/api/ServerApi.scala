package pl.setblack.pongi.web.api

import scala.scalajs.js.annotation.JSExportAll
import org.scalajs.jquery.jQuery
import upickle.Js

import scala.scalajs.js
import upickle.default._

import scala.concurrent.{Future, Promise}
import scala.scalajs.js.JSON


/**
  * Created by jarek on 1/22/17.
  */
class ServerApi {


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

  def loginUser(login: String, pass: String): Future[Session] = {
    val data = NewUser(pass)
    val result = Promise[Session]
    jQuery.post("/api/users/login/" + login, write[NewUser](data).asInstanceOf[js.Any], null, "text").done(
      (response: String) => {
        result.success(read[Session](response))
      }
    )
    result.future
  }
}