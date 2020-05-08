package io.mvillafuertem.mdoc.modifier.plantuml

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream, File }

import javax.imageio.ImageIO
import mdoc.{ Reporter, StringModifier }
import net.sourceforge.plantuml.{ FileFormat, FileFormatOption, SourceStringReader }

import scala.meta.Input

class PlantUMLModifier extends StringModifier {
  override val name = "plantuml"

  override def process(info: String, code: Input, reporter: Reporter): String = {
    val strings  = info.split(":")
    val pathname = strings(0)
    val format   = strings(1)

    val source = s"@startuml\n${code.text}\n@enduml\n"
    val reader = new SourceStringReader(source)
    val os     = new ByteArrayOutputStream
    reader.generateImage(os, new FileFormatOption(FileFormat.PNG))
    os.close()

    val bis      = new ByteArrayInputStream(os.toByteArray);
    val bImage2  = ImageIO.read(bis);
    //new File(pathname).mkdir()
    val nameFile = s"$pathname.$format"
    ImageIO.write(bImage2, s"$format", new File(nameFile));
    System.out.println("image created");
    //val svg = new String(os.toByteArray, Charset.forName("UTF-8"))
    //s"<div align='center'>$svg</div>"

    s"![$nameFile]($nameFile)"
  }
}
