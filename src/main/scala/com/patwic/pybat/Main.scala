package com.patwic.pybat

import com.github.tkqubo.html2md.Html2Markdown
import org.json4s._
import org.json4s.native.JsonMethods.parse
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import java.util.Properties

import javax.script.ScriptEngineManager

import scala.io.Source
import org.python.core.Py
import org.python.core.PySystemState
import javax.script.ScriptEngine

import scala.util.{Failure, Success, Try}

object Main {
  implicit val formats = DefaultFormats

  val filename = "/Users/filip/Learn/parse-pybat/exer.json"

  case class Group(id: String,
                   name: String,
                   enabled: Boolean,
                   desc: String,
                   exercises: List[Exercise])

  case class Exercise(argnames: List[String],
                      custombody : Boolean,
                      custombodycontent: String,
                      desc: String,
                      fname: String,
                      id: String,
                      links: List[String],
                      pydocs: List[String],
                      shortdesc: String,
                      tags: List[String],
                      tests: String,
                      title: String)

  def initializePythonEngine = {
    val props = new Properties()
    props.setProperty("python.import.site", "false")
    PySystemState.initialize(props, new Properties())
    Py.setSystemState(new PySystemState)
    new ScriptEngineManager().getEngineByName("python")
  }

  implicit val engine = initializePythonEngine

  def generateArgList(argNames: List[String]): String =
      argNames.map(x => '"' + x + '"').mkString("[", ",", "]")


  def generateHeader(argNames: List[String]) : String =
    argNames.map(x => s" *$x* ").mkString("|","|","|")

  def generateUnderline(argNames: List[String]) : String =
    argNames.map(x => " " + "-" * x.length + " ").mkString("|","|","|")

  def parseScriptToString(s: String)(implicit engine: ScriptEngine): Try[String] =
        Try(engine.eval(s).asInstanceOf[String])


  def main(args: Array[String]): Unit = {

    def generateMarkdownPage(exercise: Exercise) : Try[String] = {
      val args = generateArgList(exercise.argnames)

      val testExp = s"""'\\n'.join([ " | ".join([str(q) for q in x['args']] + [x['exp']]) for x in ${exercise.tests}])"""
      println(testExp)

      parseScriptToString(testExp) match {
        case a:Failure[_] => a
        case Success(testString) =>
           val s = testString.split("\n") map(x => "|" + x + "|") mkString("\n")


        println(s)

        Try(s"""# ${exercise.title}
           | ${exercise.shortdesc}
           |### Description
           |
           |${Html2Markdown.toMarkdown(exercise.desc)}
           |
           |### Function
           |
           | ```python
           | def ${exercise.fname}(${exercise.argnames.mkString(",")}):
           | ${if (exercise.custombody) exercise.custombody else "\tpass"}
           | ```
           |
           |### Tests
           |
           |${ generateHeader(exercise.argnames) }     Expected Result |
           |${ generateUnderline(exercise.argnames) }  --------------- |
           """.stripMargin + s)

      }
    }

    def generatePage(ex: Exercise) : Unit = {
      val content = generateMarkdownPage(ex)
      if (content.isSuccess) {
        val path = ex.id + ".md"
        Files.write(Paths.get(path), content.get.getBytes(StandardCharsets.UTF_8))
      }
    }


    println(parseScriptToString("str({1})"))
    val j  = parse(Source.fromFile(args(0)).getLines.mkString)

    val g = j.extract[Group]

    // val markdown = Html2Markdown.toMarkdown(g.exercises(0).desc)
    //val markdown = Html2Markdown.toMarkdown("<h1>Header</h1>")

    println(generatePage(g.exercises(0)))

  }
}
