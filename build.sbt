name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers ++= Seq()

libraryDependencies ++= Seq(
  "org.apache.spark"  %% "spark-streaming"             % "1.4.0" % "provided",
  "org.apache.spark"  %% "spark-streaming-kinesis-asl" % "1.4.0" exclude("org.apache.spark", "spark-streaming_2.11"),
  "org.elasticsearch" %% "elasticsearch-spark"         % "2.1.0" exclude("org.apache.spark", "spark-sql_2.11")
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) =>
    (xs map {_.toLowerCase}) match {
      case ("manifest.mf" :: Nil) => MergeStrategy.discard
      case _ => MergeStrategy.discard
    }
  case x => MergeStrategy.first
}
