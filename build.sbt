Global / onLoad := {
  val GREEN = "\u001b[32m"
  val RESET = "\u001b[0m"
  println(s"""$GREEN
             |$GREEN        ███╗   ███╗ ██████╗   ██████╗   ██████╗          ███╗   ███╗  ██████╗  ██████╗  ██╗ ███████╗ ██╗ ███████╗ ██████╗
             |$GREEN        ████╗ ████║ ██╔══██╗ ██╔═══██╗ ██╔════╝          ████╗ ████║ ██╔═══██╗ ██╔══██╗ ██║ ██╔════╝ ██║ ██╔════╝ ██╔══██╗
             |$GREEN        ██╔████╔██║ ██║  ██║ ██║   ██║ ██║       █████╗  ██╔████╔██║ ██║   ██║ ██║  ██║ ██║ █████╗   ██║ █████╗   ██████╔╝
             |$GREEN        ██║╚██╔╝██║ ██║  ██║ ██║   ██║ ██║       ╚════╝  ██║╚██╔╝██║ ██║   ██║ ██║  ██║ ██║ ██╔══╝   ██║ ██╔══╝   ██╔══██╗
             |$GREEN        ██║ ╚═╝ ██║ ██████╔╝ ╚██████╔╝ ╚██████╗          ██║ ╚═╝ ██║ ╚██████╔╝ ██████╔╝ ██║ ██║      ██║ ███████╗ ██║  ██║
             |$GREEN        ╚═╝     ╚═╝ ╚═════╝   ╚═════╝   ╚═════╝          ╚═╝     ╚═╝  ╚═════╝  ╚═════╝  ╚═╝ ╚═╝      ╚═╝ ╚══════╝ ╚═╝  ╚═╝
             |$RESET        v.${version.value}
             |""".stripMargin)
  (Global / onLoad).value
}

lazy val configurationPublish: Project => Project =
  _.settings(Information.value)
    .settings(Settings.value)
    .settings(Settings.noAssemblyTest)
    .settings(crossScalaVersions := Settings.supportedScalaVersions)

lazy val configurationNoPublish: Project => Project =
  _.settings(Information.value)
    .settings(Settings.value)
    .settings(Settings.noPublish)
    .settings(Settings.noAssemblyTest)
    .settings(crossScalaVersions := Nil)

lazy val `mdoc-modifier` = (project in file("."))
  .configure(configurationNoPublish)
  .aggregate(
    `mdoc-modifier-akka-http`,
    `mdoc-modifier-plantuml`,
    `mdoc-modifier-docs`
  )
  .settings(commands ++= Commands.value)

lazy val `mdoc-modifier-akka-http` = (project in file("modules/akka-http"))
  .configure(configurationPublish)
  // S E T T I N G S
  .settings(libraryDependencies ++= Dependencies.`mdoc-modifier-akka-http`)
  // P L U G I N S
  .enablePlugins(MdocPlugin)

lazy val `mdoc-modifier-plantuml` = (project in file("modules/plantuml"))
  .configure(configurationPublish)
  // S E T T I N G S
  .settings(NexusSettings.value)
  .settings(libraryDependencies ++= Dependencies.`mdoc-modifier-plantuml`)
  // P L U G I N S
  .enablePlugins(MdocPlugin)

lazy val `mdoc-modifier-docs` = (project in file("modules/docs"))
  .configure(configurationNoPublish)
  // D E P E N D S  O N
  .dependsOn(`mdoc-modifier-plantuml`)
  // S E T T I N G S
  .settings(scalaSource in Compile := baseDirectory.value / "src/main/mdoc")
  .settings(MdocSettings.value)
  // P L U G I N S
  .enablePlugins(MdocPlugin)
