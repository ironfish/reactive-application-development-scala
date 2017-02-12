scalaVersion := "2.12.1"

val akkaVersion = "2.4.17"

organization := "com.example"
name := "echo"
version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
"com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
"com.typesafe.akka"      %% "akka-persistence" % akkaVersion
)
