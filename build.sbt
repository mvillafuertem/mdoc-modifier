lazy val infoSettings = Seq(
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

lazy val `mdoc-modifier` = (project in file("."))
  .aggregate(
    `mdoc-modifier-akka-http`,
    `mdoc-modifier-plantuml`,
    docs
  )
  // S E T T I N G S
  .settings(infoSettings)
  .settings(Settings.value)
  .settings(Settings.noPublish)
  .settings(Settings.noAssemblyTest)
  .settings(crossScalaVersions := Nil)

lazy val `mdoc-modifier-akka-http` = (project in file("akka-http"))
  // S E T T I N G S
  .settings(infoSettings)
  .settings(Settings.value)
  .settings(Settings.noAssemblyTest)
  .settings(Settings.noPublish)
  .settings(crossScalaVersions := Settings.supportedScalaVersions)
  .settings(libraryDependencies ++= Dependencies.`mdoc-modifier-akka-http`)
  // P L U G I N S
  .enablePlugins(MdocPlugin)

lazy val `mdoc-modifier-plantuml` = (project in file("plantuml"))
  // S E T T I N G S
  .settings(infoSettings)
  .settings(Settings.value)
  .settings(Settings.noAssemblyTest)
  .settings(NexusSettings.value)
  .settings(crossScalaVersions := Settings.supportedScalaVersions)
  .settings(libraryDependencies ++= Dependencies.`mdoc-modifier-plantuml`)
  // P L U G I N S
  .enablePlugins(MdocPlugin)

lazy val docs = (project in file("docs"))
  .dependsOn(`mdoc-modifier-plantuml`)
  // S E T T I N G S
  .settings(scalaSource in Compile := baseDirectory.value / "src/main/mdoc")
  .settings(infoSettings)
  .settings(Settings.value)
  .settings(Settings.noPublish)
  .settings(crossScalaVersions := Nil)
  .settings(MdocSettings.value)
  // P L U G I N S
  .enablePlugins(MdocPlugin)