val akkaVersion = "2.4.17"
val logbackVer = "1.2.1"

name := "rarebooks"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-cluster"     % akkaVersion,
  "com.typesafe.akka"      %% "akka-slf4j"       % akkaVersion,
  "ch.qos.logback"         %  "logback-classic"  % logbackVer,
  "com.typesafe.akka"      %% "akka-testkit"     % akkaVersion    % "test",
  "org.scalatest"          %% "scalatest"        % "2.2.6"        % "test"
)

addCommandAlias("rb", "runMain com.rarebooks.library.RareBooksApp -Dakka.remote.netty.tcp.port=2551")
