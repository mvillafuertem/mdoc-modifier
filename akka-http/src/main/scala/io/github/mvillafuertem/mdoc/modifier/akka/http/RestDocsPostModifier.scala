package io.github.mvillafuertem.mdoc.modifier.akka.http

import java.io.{ByteArrayOutputStream, PrintStream}

import com.vladsch.flexmark.util.options.MutableDataSet
import mdoc.internal.cli.Context
import mdoc.internal.io.ConsoleReporter
import mdoc.internal.markdown._
import mdoc.{MainSettings, PostModifier, PostModifierContext}

import scala.meta.inputs.Input

final class RestDocsPostModifier extends PostModifier {

  override val name: String = "postrestdocs"

  override def process(ctx: PostModifierContext): String = {

    val myStdout = new ByteArrayOutputStream()
    val myReporter = new ConsoleReporter(new PrintStream(myStdout))
    val markdownSettings = Markdown.baseSettings()


    ctx.reporter.warning(ctx.info)
    ctx.reporter.warning(ctx.outputCode)


    val reporter = newReporter()
    val context = newContext(settings, reporter)

    val cleanInput = scala.meta.inputs.Input.VirtualFile(name + ".md", ctx.originalCode.text)
    val markdown = Markdown.toMarkdown(cleanInput, markdownSettings, myReporter, )

    ctx.reporter.warning(markdown)

    markdown

  }

  def getMarkdownSettings(context: Context): MutableDataSet = {
    myStdout.reset()
    Markdown.mdocSettings(context)
  }
}
