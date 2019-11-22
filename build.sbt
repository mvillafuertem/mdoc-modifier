lazy val commonSettings = Settings.value ++ Seq(
  organization := "io.github.mvillafuertem",
  scalaVersion := "2.13.1",
  homepage := Some(url(s"https://github.com/mvillafuertem/$name")),
  licenses := List("MIT" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "mvillafuertem",
      "Miguel Villafuerte",
      "mvillafuertem@email.com",
      url("https://github.com/mvillafuertem")
    )
  )
)

lazy val `mdoc-modifier` = (project in file("."))
  .aggregate(
    `mdoc-modifier-plantuml`,
    docs
  )
  // S E T T I N G S
  .settings(commonSettings)
  .settings(Settings.noPublish)

lazy val `mdoc-modifier-plantuml` = (project in file("plantuml"))
  // S E T T I N G S
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.`mdoc-modifier-plantuml`)
  // P L U G I N S
  .enablePlugins(MdocPlugin)

lazy val docs = (project in file("docs"))
  .dependsOn( `mdoc-modifier-plantuml`)
  // S E T T I N G S
  .settings(scalaSource in Compile := baseDirectory.value / "src/main/mdoc")
  .settings(commonSettings)
  .settings(MdocSettings.value)
  .settings(Settings.noPublish)
  // P L U G I N S
  .enablePlugins(MdocPlugin)