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
      .withColumn("eventTime", $"value".cast("timestamp"))
      .withWatermark("eventTime", "10 seconds")
//      .groupBy(window($"eventTime", "5 seconds") as 'window)
//      .agg(count("*") as 'count)
//      .select($"window".getField("start").cast("long").as[Long], $"count".as[Long])
      .dropDuplicates()
      .select($"eventTime".cast("long").as[Long])

    val query = ds.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()

  }
}
