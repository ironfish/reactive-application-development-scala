import CommonSettings._

name := "chap3"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.5"

sbtVersion := "0.13.7"

resolvers := ResolverSettings.resolvers

lazy val chap3 = project.in(file("."))
  .settings(CommonSettings.commonSettings :_*)

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-actor"      % Akka      % "compile",
  "com.typesafe.akka"          %% "akka-slf4j"      % Akka      % "compile",
  "ch.qos.logback"              % "logback-classic" % "1.1.2"   % "compile",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.0.0"   % "compile",
  "com.typesafe.akka"          %% "akka-testkit"    % Akka      % "test"
)
