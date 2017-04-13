name := "basic-project"

organization := "example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "3.0.1",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "org.mockito" % "mockito-all" % "1.10.19"
)

initialCommands := "import example._"
