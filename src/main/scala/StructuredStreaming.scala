import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession

object StructuredStreaming {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    import spark.implicits._

    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()

    val ds = lines.as[String]
      .flatMap(_ split " ")
      .groupBy("value")
      .count()

    ds.createOrReplaceTempView("words")
    spark.sql("select count(*) from words").show()

    val query = ds.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()

  }
}
