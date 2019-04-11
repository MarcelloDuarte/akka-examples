name := "reactive-scala"
version := "0.1-SNAPSHOT"
scalaVersion := "2.12.8"

lazy val actor = project

lazy val remote = (project in file ("remote"))
  .dependsOn(actor)

lazy val local = (project in file ("local"))
  .dependsOn(actor)