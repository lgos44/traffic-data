ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val CirceVersion = "0.15.0-M1"
val ScalaTestVersion = "3.2.13"

val jsonDependencies = Seq(
  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % ScalaTestVersion
)

lazy val root = (project in file("."))
  .settings(
    organization := "co.topl",
    name := "traffic-data",
    mainClass := Some("co.topl.traffic.Main"),
    libraryDependencies ++= jsonDependencies ++ testDependencies
  )
