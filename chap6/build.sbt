import CommonSettings._

name := "chap6"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers := ResolverSettings.resolvers

lazy val chap4 = project.in(file("."))
  .settings(CommonSettings.commonSettings :_*)

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-actor"                    % Akka    % "compile",
  "com.typesafe.akka"          %% "akka-slf4j"                    % Akka    % "compile",
  "com.typesafe.akka"          %% "akka-persistence-experimental" % Akka    % "compile",
  "org.scalactic"               % "scalactic_2.11"                % "2.2.4" % "compile",
  "ch.qos.logback"              % "logback-classic"               % "1.1.2" % "compile",
  "com.typesafe.scala-logging" %% "scala-logging"                 % "3.0.0" % "compile",
  "com.typesafe.akka"          %% "akka-testkit"                  % Akka    % "test"
)
