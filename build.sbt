name := """littering-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.amazonaws" % "aws-java-sdk" % "1.10.4",
  "com.wordnik" % "swagger-core" % "1.5.3-M1",
  "org.imgscalr" % "imgscalr-lib" % "4.2",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.apache.commons" % "commons-imaging" % "1.0-SNAPSHOT"
)

resolvers += "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

