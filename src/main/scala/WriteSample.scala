import org.apache.spark.{SparkConf, SparkContext}

import org.elasticsearch.spark._
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._

object WriteSample extends App {
  val sc = new SparkContext(new SparkConf().setAppName("write sample")
    .set(ES_RESOURCE_WRITE, "spark/docs")
    .set(ES_WRITE_OPERATION, "upsert")
    .set(ES_MAPPING_ID, "one")
  )

  print("------ ")
  sc.esRDD("spark/docs", """{"query" : { "ids" : { "values" : [ "100" ] } } }""") foreach println

  sc.makeRDD(Seq(
    Map("one" -> 100, "two" -> 222, "three" -> 333)
  )).saveToEs(Map.empty[String, String])

  print("------ ")
  sc.esRDD("spark/docs", """{"query" : { "ids" : { "values" : [ "100" ] } } }""") foreach println

}
