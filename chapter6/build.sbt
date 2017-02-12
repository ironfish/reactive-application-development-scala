lazy val commonSettings = Seq(
  organization := "com.rarebooks",
  version := "0.2-SNAPSHOT",
  scalaVersion := "2.11.8",
  parallelExecution in Test := false,
  logBuffered in Test := false,
  parallelExecution in ThisBuild := false
)

lazy val common = project.
  settings(commonSettings)

lazy val catalog = project.
  settings(commonSettings).
  dependsOn(
    common % "test->test;compile->compile")

lazy val customer = project.
  settings(commonSettings).
  dependsOn(
    common % "test->test;compile->compile",
    catalog % "compile->compile")

lazy val rarebooks = project.
  settings(commonSettings).
  dependsOn(
    common % "test->test;compile->compile",
    catalog % "compile->compile")
