package com.patwic.pybat
import org.scalatest._
import com.patwic.pybat.Main._

class ParseTest extends FlatSpec with Matchers {

  "An empty arglist" should "produce an empty arg string" in {
    val argString = generateArgList(List())

    argString should be ("[]")
  }

  "A single arg" should "produce a single arg string" in {
    val argString = generateArgList(List("x"))
    argString should be ("""["x"]""")
  }

  " Single header arg " should "generate single header " in {
    val res = generateHeader(List("x"))
    res should be("| *x* |")
  }

  " Multiple header args " should "generate multiple headers " in {
    val res = generateHeader(List("x","longer","short"))
    res should be ("| *x* | *longer* | *short* |")
  }

  " Single arg " should "generate single column " in {
    val res = generateUnderline(List("x"))
    res should be("| - |")
  }

  " Multiple args " should "generate multiple columns " in {
    val res = generateUnderline(List("x","longer","short"))
    res should be ("| - | ------ | ----- |")
  }
}
