import sbt._

object Dependencies {

  val `mdoc-modifier-plantuml`: Seq[ModuleID] =
    Seq(
      Artifact.plantUml
    )

  private object Artifact {

    val plantUml = "net.sourceforge.plantuml" % "plantuml" % Version.plantUml
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest

  }

  private object Version {
    val plantUml = "8059"
    val scalaTest = "3.0.8"
  }

}