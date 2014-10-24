import org.apache.spark.{SparkConf, SparkContext}

import org.elasticsearch.spark._
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object WriteMain extends App {
  val conf = new SparkConf().setAll(Seq(
    ES_NODES           -> "localhost",
    ES_RESOURCE_READ   -> "spark/docs",
    ES_RESOURCE_WRITE  -> "docker/docs",
    ES_QUERY           -> """{"query" : { "ids" : { "values" : [ "100", "101" ] } } }""",
    ES_WRITE_OPERATION -> ES_OPERATION_UPSERT,
    ES_MAPPING_ID      -> "one"
  ))

  utils.ESWriter.execute(conf)

}

package utils {
  object ESWriter {
    def execute(conf: SparkConf) = {
      val sc = new SparkContext(conf)
      try {
        sc.esRDD.flatMap { case (_id, body) =>
          println("---------------" + _id)
          body + ("two" -> (Math.random * 10).toInt)
        }.saveToEs(Map(ES_NODES -> "172.17.0.2"))
      } finally {
        sc.stop
      }
    }
  }
}
