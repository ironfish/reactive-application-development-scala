val akkaVersion = "2.4.17"
val logbackVer = "1.2.1"

lazy val commonSettings = Seq(
  organization := "com.rarebooks",
  version := "0.3-SNAPSHOT",
  scalaVersion := "2.12.1",
  parallelExecution in Test := false,
  logBuffered in Test := false,
  parallelExecution in ThisBuild := false,
  resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
  libraryDependencies ++= Seq(
    "com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
    "com.typesafe.akka"      %% "akka-remote"      % akkaVersion,
    "com.typesafe.akka"      %% "akka-slf4j"       % akkaVersion,
    "ch.qos.logback"         %  "logback-classic"  % logbackVer,
    "com.typesafe.akka"      %% "akka-testkit"     % akkaVersion    % "test",
    "org.scalatest"          %% "scalatest"        % "3.0.1"        % "test"
  )
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
