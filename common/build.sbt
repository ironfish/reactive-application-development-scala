val akkaVersion = "2.4.17"

name := "common"

organization := "com.rarebooks"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor"                % akkaVersion,
  "org.scala-lang.modules"   %% "scala-parser-combinators"  % "1.0.5",
  "com.typesafe.akka"        %% "akka-testkit"              % akkaVersion    % "test",
  "org.scalatest"            %% "scalatest"                 % "3.0.1"        % "test"
)
