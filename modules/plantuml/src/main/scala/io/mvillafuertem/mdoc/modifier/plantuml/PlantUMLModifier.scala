package io.mvillafuertem.mdoc.modifier.plantuml

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream, File }

import javax.imageio.ImageIO
import mdoc.{ Reporter, StringModifier }
import net.sourceforge.plantuml.{ FileFormat, FileFormatOption, SourceStringReader }
import zio.clock.Clock
import zio.console.Console
import zio.logging.{ log, Logging }
import zio.{ BootstrapRuntime, Task, URLayer, ZIO }

import scala.meta.Input

class PlantUMLModifier extends StringModifier with BootstrapRuntime {

  override val name = "plantuml"

  override def process(info: String, code: Input, reporter: Reporter): String =
    unsafeRun(
      PlantUMLModifier
        .program(info, code, reporter)
        .provideLayer(PlantUMLModifier.loggingLayer)
    )

}

object PlantUMLModifier {

  case class PlantUMLInformation(pathName: String, format: String)

  def program(info: String, code: Input, reporter: Reporter): ZIO[Logging, Throwable, String] =
    for {
      umlInfo <- Task
                   .effect(info.split(":"))
                   .map(strings => PlantUMLInformation(strings(0), strings(1)))
      nameFile = s"${umlInfo.pathName}.${umlInfo.format}"
      effect  <- Task
                  .effect(new SourceStringReader(code.text))
                  .map { reader =>
                    val os = new ByteArrayOutputStream()
                    reader.outputImage(os, new FileFormatOption(FileFormat.PNG))
                    os
                  }
                  .map(os => new ByteArrayInputStream(os.toByteArray))
                  .map(bis => ImageIO.read(bis))
                  .map(bufferedImage => ImageIO.write(bufferedImage, s"${umlInfo.format}", new File(nameFile)))
                  .map(_ => s"![$nameFile]($nameFile)")
                  .tap(_ => log.info(s"Created image $nameFile"))
    } yield effect

  private lazy val loggingLayer: URLayer[Console with Clock, Logging] =
    Logging.console((_, logEntry) => logEntry)

}
