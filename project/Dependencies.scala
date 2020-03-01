import sbt._

object Dependencies {

  val `mdoc-modifier-akka-http`: Seq[ModuleID] =
    Seq(
      Artifact.akkaHttp,
      Artifact.akkaStream,
      Artifact.aspectj,
      Artifact.scalaTest,
      Artifact.akkaHttpTestKit
    )

  val `mdoc-modifier-plantuml`: Seq[ModuleID] =
    Seq(
      Artifact.plantUml
    )

  private object Artifact {

    // A K K A
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.akka
    val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp

    // A S P E C T J
    val aspectj = "org.aspectj" % "aspectjrt" % Version.aspectj
    // P L A N T  U M L
    val plantUml = "net.sourceforge.plantuml" % "plantuml" % Version.plantUml

    // S C A L A  T E S T
    val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest

  }

  private object Version {
    val akkaHttp = "10.1.11"
    val akka = "2.6.3"
    val aspectj = "1.9.5"
    val plantUml = "1.2020.2"
    val scalaTest = "3.1.0"
  }

}