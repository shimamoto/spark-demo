import org.apache.spark.{SparkConf, SparkContext}

import org.elasticsearch.spark._

object Sample {
  def main(args: Array[String]) = {
    // TODO want to abstraction for unit test
    val sc = new SparkContext(new SparkConf().setAppName("sample app"))

    /** writing */
    // creates RDD based on the collection specified
    val data = sc.makeRDD(Seq(Map("one" -> 100, "two" -> 200, "three" -> 300)))
    data.saveToEs("spark/docs")

    // For cases where the data in the RDD is already in JSON
    val data2 = sc.makeRDD(Seq("""{ "one":11, "two":12, "three":13 }"""))
    data2.saveJsonToEs("spark/docs")

    /** reading */
    val rdd = sc.esRDD("spark/docs" ,"?pretty=true")
    val data3 = rdd.map(x => x._1 + ":" + x._2)

    println(s"----------- ${data3.collect.mkString(", ")} -----------")

    // For cases where the data in the RDD is already in JSON
    val data4 = sc.makeRDD(Seq("""{ "one":{"foo":1, "bar":2 } }"""))
    data4.saveJsonToEs("spark/nested")
    val rdd2 = sc.esRDD("spark/nested", "?pretty=true")
    val data5 = rdd2.map { x =>
      x._1 + ":" + x._2
    }

    println(s"*********** ${data5.collect.mkString(", ")} ***********")

  }

}
