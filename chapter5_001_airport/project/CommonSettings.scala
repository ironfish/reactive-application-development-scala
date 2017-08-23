import sbt._
import Keys._

object CommonSettings {

  val Akka   = "2.3.9"
  val Spray  = "1.3.2"
 
  /**
   * Common project settings
   */
  val commonSettings: Seq[Def.Setting[_]] =
    Seq(
      organization := "com.weightwatchers.chassis",
      scalaVersion := "2.11.5",
      scalacOptions in Compile ++= Seq("-encoding", "UTF-8", "-target:jvm-1.8", "-deprecation", "-unchecked", "-Ywarn-dead-code", "-Xfatal-warnings", "-feature", "-language:postfixOps"),
      scalacOptions in (Compile, doc) <++= (name in (Compile, doc), version in (Compile, doc)) map DefaultOptions.scaladoc,
      javacOptions in (Compile, compile) ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation", "-Xlint:-options"),
      javacOptions in doc := Seq(),
      javaOptions += "-Xmx2G",
      fork := true,
      resolvers := ResolverSettings.resolvers
    ) 
}
