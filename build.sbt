name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "JBoss Public Repository" at "https://repository.jboss.org/nexus/content/groups/public/"
)

libraryDependencies ++= Seq(
  "org.apache.spark"  %% "spark-streaming"             % "2.0.0" % "provided",
  "org.apache.spark"  %% "spark-streaming-kinesis-asl" % "2.0.0" excludeAll(
    ExclusionRule("org.apache.spark", "spark-streaming_2.11"),
    ExclusionRule("org.apache.spark", "spark-tags_2.11"),
    ExclusionRule("org.spark-project.spark")
  ),
  "org.elasticsearch" %% "elasticsearch-spark-20"      % "5.0.0-alpha5" exclude("org.apache.spark", "spark-sql_2.11")
//  "org.optaplanner"    % "optaplanner-core"            % "6.3.0.Final"
)

net.virtualvoid.sbt.graph.Plugin.graphSettings
