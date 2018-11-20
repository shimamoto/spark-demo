name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.7"
crossScalaVersions := Seq(scalaVersion.value, "2.11.12")

val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.scalatest"     %% "scalatest"                   % "3.0.5" % "test",
  "org.apache.spark"  %% "spark-core"                  % sparkVersion % "test" classifier "tests",
  "org.apache.spark"  %% "spark-sql"                   % sparkVersion % "test" classifier "tests",
  "org.apache.spark"  %% "spark-catalyst"              % sparkVersion % "test" classifier "tests",
  "org.apache.spark"  %% "spark-sql"                   % sparkVersion % "provided",
  "org.apache.spark"  %% "spark-streaming"             % sparkVersion % "provided",
  "org.apache.spark"  %% "spark-streaming-kinesis-asl" % sparkVersion excludeAll(
    ExclusionRule("org.apache.spark", "spark-streaming_2.11"),
    ExclusionRule("org.apache.spark", "spark-streaming_2.12"),
    ExclusionRule("org.apache.spark", "spark-tags_2.11"),
    ExclusionRule("org.apache.spark", "spark-tags_2.12"),
    ExclusionRule("org.spark-project.spark")
  )
//  "org.elasticsearch" %% "elasticsearch-spark-20"      % "5.6.9" exclude("org.apache.spark", "*")
)

scalacOptions ++= Seq("-feature", "-deprecation")
