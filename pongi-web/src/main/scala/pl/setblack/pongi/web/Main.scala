package pl.setblack.pongi.web

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.vdom.ReactAttr.{Generic, ValueType}
import japgolly.scalajs.react.{ReactComponentB, ReactDOM}
import org.scalajs.dom.document
import pl.setblack.pongi.web.pong._

import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.ScalaJSDefined
/**
  * Created by jarek on 1/16/17.
  */
object Main extends JSApp{



  def main(): Unit = {
    println("Hello world!")


    val gameLoop  = new GameLoop

    implicit val asJSObj = ValueType.map[StylePositioned](a=>a)
    val gameState = new GameState

    val PlayerComponent = ReactComponentB[Player]("Player")
      .render_P(ply => <.figure(
        ^.`class` := "player",
        ^.style :=  new StylePositioned(ply.paddle),
        Generic("x") := ply.paddle.x.toString,
        Generic("y") := ply.paddle.y.toString
      ))
      .build

    val BallComponent = ReactComponentB[Ball]("Ball")
      .render_P(ball=> <.figure(
        ^.`class` := "ball",
        ^.style :=  new StylePositioned(ball)
      ))
      .build


    val PlayFieldComponent =
      ReactComponentB[GameState]("Playfield")
        .render_P(game => <.section(
          ^.`class` := "playfield",
          PlayerComponent(game.players._1),
          PlayerComponent(game.players._2),
          BallComponent(game.ball)))
        .build


    val myState = new PongClientState

    ReactDOM.render(Pong.page(myState), document.getElementById("react"))
  }




  @ScalaJSDefined
  class StylePositioned(o : GameObject)  extends js.Object {
    val left : String =  toPercent(o.x)
    val top : String =  toPercent(o.y)
  }

  private def toPercent(v :Float) : String = {
    s"${Math.round( v*100f).toString}%"
  }
}


