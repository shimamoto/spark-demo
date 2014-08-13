import org.apache.spark.{SparkConf, SparkContext}
//import org.apache.spark.SparkContext._

import org.elasticsearch.spark._

object Sample {
  def main(args: Array[String]) = {
    // TODO want to abstraction for unit test
    val sc = new SparkContext(new SparkConf().setAppName("sample app"))

    /** writing */
    // creates RDD based on the collection specified
    val data = sc.makeRDD(Seq(Map("one" -> 1, "two" -> 2, "three" -> 3)))
    data.saveToEs("spark/docs")

    // For cases where the data in the RDD is already in JSON
    val data2 = sc.makeRDD(Seq("""{ "one":11, "two":12, "three":13 }"""))
    data2.saveJsonToEs("spark/docs")

    /** reading */
    val rdd = sc.esRDD("spark/docs" ,"?pretty=true")
    val data3 = rdd.map(_.mkString(":"))

    println(s"----------- ${data3.collect.mkString(", ")} -----------")
  }

}
