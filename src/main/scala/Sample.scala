import org.apache.spark.{SparkConf, SparkContext}

object Sample {
  def main(args: Array[String]) = {
    // TODO want to abstraction for unit test
    val sc = new SparkContext(new SparkConf().setAppName("sample app"))



  }

}
