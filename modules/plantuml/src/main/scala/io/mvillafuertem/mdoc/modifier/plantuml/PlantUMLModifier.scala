package io.mvillafuertem.mdoc.modifier.plantuml

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream, File, FileOutputStream }

import javax.imageio.ImageIO
import mdoc.{ Reporter, StringModifier }
import net.sourceforge.plantuml.{ FileFormat, FileFormatOption, SourceStringReader }
import zio._
import zio.clock.Clock
import zio.console.Console
import zio.logging.{ log, Logging }

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

  case class PlantUMLInformation(pathName: String, fileFormat: String)
  case class PlantUMLError(message: String)

  def buildFromString(info: String): ZIO[Any, PlantUMLError, PlantUMLInformation] =
    (for {
      splitted <- Task.effect(info.split(":"))
      pathName <- Task.effect(splitted(0))
      format   <- Task.effect(splitted(1))
    } yield PlantUMLInformation(pathName, format)).mapError {
      case _: ArrayIndexOutOfBoundsException => PlantUMLError(s"[ $info ] modifier doesn't match with the correct format [ file/path/name:fileFormat ]")
      case e                                 => PlantUMLError(s"$e")
    }

  def selectFileFormat(fileFormat: String): ZIO[Any, PlantUMLError, FileFormat] =
    Task
      .effect(FileFormat.valueOf(fileFormat.toUpperCase))
      .withFilter(a => (a == FileFormat.PNG) | a == (FileFormat.ATXT) | a == (FileFormat.SVG))
      .mapError {
        case _: IllegalArgumentException | _: NoSuchElementException => PlantUMLError(createError(fileFormat))
        case e                                                       => PlantUMLError(s"$e")
      }

  def createError(message: String) = s"[ $message ] file format is invalid, only supports [${FileFormat.PNG}, ${FileFormat.ATXT}]"

  def createDiagram(fileFormat: FileFormat, code: Input, plantUMLInformation: PlantUMLInformation): ZIO[Logging, PlantUMLError, String] =
    fileFormat match {
      case FileFormat.PNG  => createDiagramPNG(code, plantUMLInformation)
      case FileFormat.ATXT => createDiagramATXT(code: Input)
      case FileFormat.SVG  => createDiagramSVG(code: Input, plantUMLInformation)
      case _               => ZIO.fail(PlantUMLError(createError(fileFormat.toString)))
    }

  def createDiagramSVG(code: Input, plantUMLInformation: PlantUMLInformation) = {

    val fileName = s"${plantUMLInformation.pathName}.${plantUMLInformation.fileFormat}"

    ZManaged
      .makeEffect(new FileOutputStream(fileName))(_.close())
      .use { os =>
        for {
          effect <- Task
                      .effect(new SourceStringReader(code.text))
                      .map(reader => reader.outputImage(os, new FileFormatOption(FileFormat.SVG)))
                      .map(_ => s"![$fileName]($fileName)")
                      .tap(_ => log.info(s"Created image $fileName"))
        } yield effect
      }
      .mapError(e => PlantUMLError(s"$e"))
  }
  def createDiagramPNG(code: Input, plantUMLInformation: PlantUMLInformation): ZIO[Logging, PlantUMLError, String] =
    ZManaged
      .makeEffect(new ByteArrayOutputStream())(_.close())
      .use { os =>
        val fileName = s"${plantUMLInformation.pathName}.${plantUMLInformation.fileFormat}"
        for {
          effect <- Task
                      .effect(new SourceStringReader(code.text))
                      .map(reader => reader.outputImage(os, new FileFormatOption(FileFormat.PNG)))
                      .map(_ => new ByteArrayInputStream(os.toByteArray))
                      .map(bis => ImageIO.read(bis))
                      .map(bufferedImage => ImageIO.write(bufferedImage, s"${plantUMLInformation.fileFormat}", new File(fileName)))
                      .map(_ => s"![$fileName]($fileName)")
                      .tap(_ => log.info(s"Created image $fileName"))
        } yield effect
      }
      .mapError(e => PlantUMLError(s"$e"))

  def createDiagramATXT(code: Input): ZIO[Any, PlantUMLError, String] = ???

  def program(info: String, code: Input, reporter: Reporter): ZIO[Logging, PlantUMLError, String] =
    for {
      umlInfo    <- buildFromString(info)
      fileFormat <- selectFileFormat(umlInfo.fileFormat)
      effect     <- createDiagram(fileFormat, code, umlInfo)
    } yield effect

  private lazy val loggingLayer: URLayer[Console with Clock, Logging] =
    Logging.console((_, logEntry) => logEntry)

}
