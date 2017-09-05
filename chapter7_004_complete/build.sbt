val akkaVersion = "2.5.4"
val logbackVer = "1.2.3"

scalaVersion := "2.12.3"

name := "catalogLoader"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-stream"      % akkaVersion,
  "com.typesafe.akka"      %% "akka-slf4j"       % akkaVersion,
  "ch.qos.logback"         %  "logback-classic"  % logbackVer
)
