package pl.setblack.pongi.web

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, CallbackTo, ReactComponentB, ReactEventI}
import pl.setblack.pongi.web.api.ServerApi

object Welcome {
  import scala.concurrent.ExecutionContext.Implicits.global

  case class LoginState(login: String = "", pass: String = "")

  case class RegisterState(login: String = "", pass: String = "", conf: String = "")

  case class WelcomeState(reg: RegisterState = RegisterState())


  private val api = new ServerApi

  private val loginSection = ReactComponentB[Unit]("LoginSection")
    .initialState(new LoginState("", ""))
    .backend(new LoginBackend(_))
    .renderBackend
    .build

  private val registerSection = ReactComponentB[Unit]("RegisterSection")
    .initialState(Welcome.RegisterState("a1", "", ""))
    .backend(new RegisterBackend(_))
    .renderBackend
    .build


  val page = ReactComponentB[Unit]("Welcome")
    .render(cb => <.section(
      ^.`class` := "welcome",
      loginSection(),
      registerSection()
    )).build


  class LoginBackend($: BackendScope[Unit, Welcome.LoginState]) {

    def login(e: ReactEventI) = {
      $.state.map(reg => {
        api.loginUser(reg.login, reg.pass).onComplete(
          result => result.foreach(value => {
            if (value.isDefined) {
              println(s"logged ${value}")
            } else {
              //  $.state(_)
              println("login error")
            }
          }))
      })
    }

    def render(state: Welcome.LoginState) = {
      <.section(
        ^.`class` := "login",
        <.label("login"),
        <.input(
          ^.name := "rlogin",
          ^.value := state.login,
          ^.onChange ==> InputManipulation.update[Welcome.LoginState]($, (p, v) => p.copy(login = v))),
        <.label("passwrod"),
        <.input(
          ^.name := "rpassword",
          ^.`type` := "password",
          ^.value := state.pass,
          ^.onChange ==> InputManipulation.update[Welcome.LoginState]($, (p, v) => p.copy(pass = v))),


        <.button("Login to Pong!",
          ^.onClick ==> login)
      )
    }
  }


  class RegisterBackend($: BackendScope[Unit, Welcome.RegisterState]) {

    def register(e: ReactEventI) = {


      $.state.map(reg => {
        api.registerUser(reg.login, reg.pass).onComplete(
          result => result.foreach(value => {
            if (value.ok) {
              Pong.getMainBackend.foreach(_.toGameList(Seq()))
            } else {
              //  $.state(_)
              println("akis error")
            }

          }))
      })

    }

    def render(state: Welcome.RegisterState) = {
      <.section(
        ^.`class` := "login",
        <.label("login"),
        <.input(
          ^.name := "rlogin",
          ^.value := state.login,
          ^.onChange ==> InputManipulation.update[Welcome.RegisterState]($, (p, v) => p.copy(login = v))),
        <.label("passwrod"),
        <.input(
          ^.name := "rpassword",
          ^.`type` := "password",
          ^.value := state.pass,
          ^.onChange ==> InputManipulation.update[Welcome.RegisterState]($, (p, v) => p.copy(pass = v))),
        <.label("confirm"),
        <.input(
          ^.name := "cpassword",
          ^.`type` := "password",
          ^.value := state.conf,
          ^.onChange ==> InputManipulation.update[Welcome.RegisterState]($, (p, v) => p.copy(conf = v))),
        <.button("Register",
          ^.onClick ==> register)
      )
    }
  }

}

object InputManipulation {
  def update[STATE]($: BackendScope[Unit, STATE], modification: (STATE, String) => STATE) = {
    (e: ReactEventI) => {
      val newValue = e.target.value
      $.modState(state => modification(state, newValue))
    }
  }
}