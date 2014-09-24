package actors

import akka.actor.Actor
import org.apache.spark.SparkConf
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

class SparkActor extends Actor {

  def receive = {
    case message =>
      val conf = new SparkConf().setAll(Seq(
        ES_NODES      -> "localhost",
        ES_RESOURCE   -> "spark/docs",
        ES_QUERY      -> """{"query" : { "ids" : { "values" : [ "100", "101" ] } } }"""
      ))
      utils.ESWriter.execute(conf)
  }

}
