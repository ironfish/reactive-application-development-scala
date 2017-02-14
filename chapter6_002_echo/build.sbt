val akkaVersion = "2.4.17"

organization := "com.example"
name := "echo"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"       % akkaVersion
)
