name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.scalatest"     %% "scalatest"                   % "3.0.5" % "test",
  "org.apache.spark"  %% "spark-core"                  % "2.3.1" % "test" classifier "tests",
  "org.apache.spark"  %% "spark-sql"                   % "2.3.1" % "test" classifier "tests",
  "org.apache.spark"  %% "spark-catalyst"              % "2.3.1" % "test" classifier "tests",
  "org.apache.spark"  %% "spark-sql"                   % "2.3.1" % "provided",
  "org.apache.spark"  %% "spark-streaming"             % "2.3.1" % "provided",
  "org.apache.spark"  %% "spark-streaming-kinesis-asl" % "2.3.1" excludeAll(
    ExclusionRule("org.apache.spark", "spark-streaming_2.11"),
    ExclusionRule("org.apache.spark", "spark-tags_2.11"),
    ExclusionRule("org.spark-project.spark")
  ),
  "org.elasticsearch" %% "elasticsearch-spark-20"      % "5.6.9" exclude("org.apache.spark", "*")
)

scalacOptions ++= Seq("-feature", "-deprecation")
