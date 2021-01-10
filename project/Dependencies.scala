import sbt._

object Dependencies {
  val scalaVer = "2.13.4"
  // #deps
  val AkkaVersion = "2.6.4"
  val AlpakkaVersion = "2.0.0"
  val AlpakkaKafkaVersion = "2.0.5"
  val JacksonDatabindVersion = "2.10.3"

  val circeVersion = "0.12.3"

  // #deps

  val dependencies = List(
    //scala module
    "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0",
    // #deps
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    // Logging
    "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

    //serialisation
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,

    "com.github.pathikrit" %% "better-files" % "3.9.1",
    "com.github.scopt" %% "scopt" % "4.0.0"
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.2" % Test,
    "com.softwaremill.diffx" %% "diffx-core" % "0.4.0" % Test,
    "com.softwaremill.diffx" %% "diffx-scalatest" % "0.4.0" % Test
  )
}
