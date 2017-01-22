package pl.setblack.pongi.web

import japgolly.scalajs.react.ReactComponentB
import japgolly.scalajs.react.vdom.prefix_<^._
import pl.setblack.pongi.web.api.GameInfo

/**
  * Created by jarek on 1/22/17.
  */
object GamesList {

  val page = ReactComponentB[Seq[GameInfo]]("Games")
    .render(cb => <.section(
      ^.`class` := "games",
      <.ul(cb.props.map( game => <.li(game.name) ) )
    )).build

}
