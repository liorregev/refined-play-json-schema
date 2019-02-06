name := "TestScala"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven"

libraryDependencies ++= Seq(
  "eu.timepit" %% "refined"                 % "0.9.4",
  "com.typesafe.play" %% "play-json" % "2.7.1",
  "com.eclipsesource"  %% "play-json-schema-validator" % "0.9.5-M4"
)