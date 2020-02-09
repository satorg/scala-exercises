ThisBuild / organization := "satorg"

ThisBuild / scalaVersion := "2.12.6"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Ypartial-unification"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.1" % Test
)
