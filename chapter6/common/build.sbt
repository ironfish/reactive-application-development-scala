scalaVersion := "2.11.8"

val akkaVersion = "2.4.17"
val logbackVer = "1.2.1"
val parserVer = "1.0.5"

name := "common"
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies ++= Seq(
  "ch.qos.logback"           %  "logback-classic"           % logbackVer,
  "org.scala-lang.modules"   %% "scala-parser-combinators"  % parserVer,
  "com.typesafe.akka"        %% "akka-actor"                % akkaVersion,
  "com.typesafe.akka"        %% "akka-testkit"              % akkaVersion    % "test",
  "org.scalatest"            %% "scalatest"                 % "2.2.6"        % "test"
)
