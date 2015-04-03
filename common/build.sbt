val akkaVersion = "2.3.9"
val logbackVer = "1.1.2"

name := "common"
organization := "com.rarebooks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.5"
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-slf4j"       % akkaVersion,
  "ch.qos.logback"         %  "logback-classic"  % logbackVer,
  "com.typesafe.akka"      %% "akka-testkit"     % akkaVersion    % "test",
  "org.scalatest"          %% "scalatest"        % "2.2.2"        % "test"
)
