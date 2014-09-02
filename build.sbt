name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.10.4", "2.11.1")

resolvers ++= Seq(
  "Archiver Managed Spark Repository" at "https://repository.apache.org/content/repositories/orgapachespark-1030/"
)

libraryDependencies ++= Seq(
  "org.apache.spark"  % "spark-core_2.10" % "1.1.0" % "provided" changing(),
  "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.1.0.Beta1"
)

assemblySettings
