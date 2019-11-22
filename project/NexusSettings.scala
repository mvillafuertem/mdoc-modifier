import sbt.Keys._
import sbt._

object NexusSettings {

  val value: Seq[Def.Setting[_]] = Seq(

    // Remove all additional repository other than Maven Central from POM
    pomIncludeRepository := { _ => false },

    publishTo := {

      val nexus = "https://oss.sonatype.org/"

      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")

      else Some("releases" at nexus + "service/local/staging/deploy/maven2")

    },

    publishMavenStyle := true,

    credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credential")

  )

}
