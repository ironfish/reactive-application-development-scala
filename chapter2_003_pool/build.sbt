name := "Guidebook_Tourist_Pool"

version := "1.0"

val akkaVersion = "2.5.4"

scalaVersion := "2.12.3"

resolvers += "Lightbend Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion
)
