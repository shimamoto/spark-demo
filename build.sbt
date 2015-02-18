name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.5"

resolvers ++= Seq()

libraryDependencies ++= Seq(
  "org.apache.spark"  %% "spark-streaming"           % "1.2.1" % "provided",
  "org.elasticsearch" %  "elasticsearch-spark_2.10"  % "2.1.0.Beta3" exclude("org.apache.spark", "spark-sql_2.10")
)

assemblySettings
