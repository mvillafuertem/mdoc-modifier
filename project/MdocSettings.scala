import mdoc.MdocPlugin.autoImport._
import sbt.Keys._
import sbt._

object MdocSettings {

  val value: Seq[Def.Setting[_]] = Seq(
    mdocIn := baseDirectory.value / "src/main/mdoc/",
    mdocOut := file("."),
    mdocVariables := Map(
      "PROJECT_NAME"  -> "Mdoc Modifier",
      "VERSION"       -> version.value,
      "SCALA_VERSION" -> scalaVersion.value,
      "SBT_VERSION"   -> sbtVersion.value,
      "RESOURCES"     -> "modules/docs/src/main/resources"
    )
  )

}
