import AssemblyKeys._

assemblySettings

name := "db-cleaner"

version := "0.1.0"

organization := "com.actonml"

val pioVersion = "0.11.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.apache.predictionio" %% "apache-predictionio-core" % pioVersion % "provided",
  "org.apache.spark" %% "spark-core" % "1.4.0" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.4.0" % "provided",
  "org.xerial.snappy" % "snappy-java" % "1.1.1.7"
)

