val akkaVersion = "2.4.2"
val logbackVer = "1.1.2"

name := "chapter5_001_remoting_catalog"
organization := "com.rarebooks"
version := "0.1-SNAPSHOT"
scalaVersion := "2.11.8"
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)
parallelExecution in Test := false
logBuffered in Test := false
parallelExecution in ThisBuild := false
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-remote"      % akkaVersion,
  "com.typesafe.akka"      %% "akka-slf4j"       % akkaVersion,
  "ch.qos.logback"         %  "logback-classic"  % logbackVer,
  "com.typesafe.akka"      %% "akka-testkit"     % akkaVersion    % "test",
  "org.scalatest"          %% "scalatest"        % "2.2.6"        % "test"
)
