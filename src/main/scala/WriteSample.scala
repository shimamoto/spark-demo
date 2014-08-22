import org.apache.spark.{SparkConf, SparkContext}

import org.elasticsearch.spark._
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object WriteSample extends App {
  val conf = new SparkConf().setAll(Seq(
    ES_NODES           -> "localhost",
    ES_RESOURCE_READ   -> "spark/docs",
    ES_RESOURCE_WRITE  -> "docker/docs",
    ES_QUERY           -> """{"query" : { "ids" : { "values" : [ "100", "101" ] } } }""",
    ES_WRITE_OPERATION -> "upsert",
    ES_MAPPING_ID      -> "one"
  ))

  new SparkContext(conf).esRDD.flatMap {
    _.headOption.map { case (_id, body) =>
      body.asInstanceOf[collection.mutable.Map[String, Any]].toMap + ("two" -> (Math.random * 10).toInt)
    }
  }.saveToEs(Map(ES_NODES -> "172.17.0.2"))


//  new SparkContext(conf).makeRDD(Seq(
//    Map("one" -> 101, "two" -> 200, "three" -> 300)
//  )).saveToEs(Map.empty[String, String])

}
