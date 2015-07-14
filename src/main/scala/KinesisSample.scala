import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import kinesis.KinesisUtils

object KinesisSample {
  def main(args: Array[String]): Unit = {
    val interval = Seconds(30)

    val spark = new StreamingContext(
      new SparkConf().set("spark.streaming.stopGracefullyOnShutdown", "true"), interval)

    val dstream = spark.union(
      // TODO shards size
      (0 until 1).map { _ =>
        KinesisUtils.createStream(spark,
          kinesisAppName = "spark-demo-indexer",
          streamName     = "spark-test",
          endpointUrl    = "https://kinesis.us-east-1.amazonaws.com",
          regionName     = "us-east-1",
          storageLevel   = StorageLevel.MEMORY_AND_DISK,
          checkpointInterval      = interval,
          initialPositionInStream = InitialPositionInStream.LATEST)
      }
    )

    // print the first 10 wordCounts
    dstream
      .flatMap(new String(_) split " ")
      .map(_ -> 1)
      .reduceByKey(_ + _)
      .print()

    spark.start
    spark.awaitTermination

  }
}
