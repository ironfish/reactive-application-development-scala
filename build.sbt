scalaVersion in ThisBuild := "2.12.1"

version in ThisBuild := "0.3-SNAPSHOT"

parallelExecution in Test := false

logBuffered in Test := false

parallelExecution in ThisBuild := false

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

lazy val common = project

lazy val chapter3_001_messaging = project.dependsOn(common % "test->test;compile->compile")
lazy val chapter3_002_elasticity = project.dependsOn(common % "test->test;compile->compile")
lazy val chapter3_003_faulty = project.dependsOn(common % "test->test;compile->compile")
lazy val chapter3_004_resilience = project.dependsOn(common % "test->test;compile->compile")

lazy val chapter6_001_catalog = project.dependsOn(common % "test->test;compile->compile")
lazy val chapter6_001_customer = project.dependsOn(
  common % "test->test;compile->compile",
  chapter6_001_catalog % "compile->compile")
lazy val chapter6_001_rarebooks = project.dependsOn(
  common % "test->test;compile->compile",
  chapter6_001_catalog % "compile->compile")
lazy val chapter6_002_echo = project
lazy val chapter6_003_atleastonce = project
