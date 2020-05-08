package io.github.mvillafuertem.mdoc.modifier.akka.http

import java.nio.file.Files

import mdoc.internal.cli.{MdocProperties, Settings}
import mdoc.internal.markdown.Markdown
import mdoc.{Reporter, StringModifier}

import scala.meta.inputs.Input
import scala.meta.internal.io.PathIO
import scala.meta.io.AbsolutePath
import scala.util.matching.Regex

final class RestDocsModifier extends StringModifier {

  def createTempDirectory(): AbsolutePath = {
    val dir = AbsolutePath(Files.createTempDirectory("mdoc"))
    dir.toFile.deleteOnExit()
    dir
  }

  def baseSettings: Settings =
    Settings
      .default(createTempDirectory())
      .withProperties(MdocProperties.default(PathIO.workingDirectory))

  override val name: String = "restdocs"

  override def process(info: String, code: Input, reporter: Reporter): String = {

    val restDocsPattern: Regex = "(?:\\[RESTDOCS\\]((?:.*?\\r?\\n?)*)\\[RESTDOCS\\])+".r
    restDocsPattern.findAllIn(code.text).toString()

    val obtained = Markdown.toMarkdown(code, Markdown.baseSettings(), reporter, baseSettings)

    reporter.error(obtained)
    reporter.error(obtained)
    restDocsPattern.findFirstIn(code.text).getOrElse("no match")

  }
}
