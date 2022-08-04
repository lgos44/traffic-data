ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val CirceVersion = "0.15.0-M1"
val ScalaTestVersion = "3.2.13"
val CatsVersion = "2.8.0"
val CatsEffectVersion = "3.3.13"
val scoptVersion = "4.1.0"

val jsonDependencies = Seq(
  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion
)

val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % CatsVersion,
  "org.typelevel" %% "cats-effect" % CatsEffectVersion
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % ScalaTestVersion
)

val utils = Seq(
  "com.github.scopt" %% "scopt" % scoptVersion
)

lazy val root = (project in file("."))
  .settings(
    organization := "co.topl",
    name := "traffic-data",
    mainClass := Some("co.topl.traffic.Main"),
    libraryDependencies ++= jsonDependencies ++ testDependencies ++ catsDependencies ++ utils
  )
