val akkaVersion = "2.4.17"

organization := "com.example"
name := "atleastonce"

libraryDependencies ++= Seq(
"com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
"com.typesafe.akka"      %% "akka-persistence" % akkaVersion
)
