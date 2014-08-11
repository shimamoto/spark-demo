name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Archiver Managed Spark Repository" at "https://repository.apache.org/content/repositories/orgapachespark-1025/",
  "conjars.org" at "http://conjars.org/repo"
)

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.1.0",
  "org.elasticsearch" % "elasticsearch-hadoop" % "2.0.0"
)
