lazy val common = project

lazy val catalog = project.dependsOn(
  common % "test->test;compile->compile")

lazy val customer = project.dependsOn(
  common % "test->test;compile->compile",
  catalog % "compile->compile")

lazy val rarebooks = project.dependsOn(
  common % "test->test;compile->compile",
  catalog % "compile->compile")
