name := "spark-demo"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "JBoss Public Repository" at "https://repository.jboss.org/nexus/content/groups/public/"
)

libraryDependencies ++= Seq(
  "org.apache.spark"  %% "spark-streaming"             % "1.5.1" % "provided",
  "org.apache.spark"  %% "spark-streaming-kinesis-asl" % "1.5.1" excludeAll(
    ExclusionRule("org.apache.spark", "spark-streaming_2.11"),
    ExclusionRule("org.spark-project.spark")
  ),
  "org.elasticsearch" %% "elasticsearch-spark"         % "2.1.0" exclude("org.apache.spark", "spark-sql_2.11")
//  "org.optaplanner"    % "optaplanner-core"            % "6.3.0.Final"
)

net.virtualvoid.sbt.graph.Plugin.graphSettings
