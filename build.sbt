enablePlugins(JavaAppPackaging)
scalafmtOnCompile := true
cancelable in Global := true
fork in Test := false

organization := "u.mr"
version := "2020-01-09"
scalaVersion := Dependencies.scalaVer
libraryDependencies ++= Dependencies.dependencies
libraryDependencies ++= Dependencies.testDependencies

dockerBaseImage := "openjdk:11-slim-buster"
dockerEntrypoint := Seq("/opt/docker/bin/word-count")


