name := "parse-pybat"

version := "0.1"

scalaVersion := "2.12.6"

val specs2Ver = "3.9.5"
val jsoupVer = "1.10.3"

val json4sNative = "org.json4s" %% "json4s-native" % "3.5.4"

libraryDependencies += json4sNative

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % jsoupVer,
  "org.specs2" %% "specs2-core" % specs2Ver % "test",
  "org.specs2" %% "specs2-matcher" % specs2Ver % "test",
  "org.specs2" %% "specs2-matcher-extra" % specs2Ver % "test",
  "org.specs2" %% "specs2-mock" % specs2Ver % "test"
)

libraryDependencies += "org.python" % "jython" % "2.7.1b3"
libraryDependencies += "com.github.tkqubo" % "html-to-markdown" % "0.7.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"


//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.1")