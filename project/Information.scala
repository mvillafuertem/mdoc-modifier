import sbt.Keys.{ description, developers, homepage, licenses, organization, scmInfo }
import sbt.{ url, Developer, ScmInfo }

object Information {

  val value = Seq(
    organization := "io.github.mvillafuertem",
    description := "Mdoc Modifier is a set of modifiers",
    homepage := Some(url(s"https://github.com/mvillafuertem/mdoc-modifier")),
    licenses := List("MIT" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "mvillafuertem",
        "Miguel Villafuerte",
        "mvillafuertem@email.com",
        url("https://github.com/mvillafuertem")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/mvillafuertem/mdoc-modifier"),
        "scm:git@github.com:mvillafuertem/mdoc-modifier.git"
      )
    )
  )

}
