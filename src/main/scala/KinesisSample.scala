import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import kinesis.{KinesisInitialPositions, KinesisInputDStream}

object KinesisSample {
  def main(args: Array[String]): Unit = {
    val interval = Seconds(30)

    val spark = new StreamingContext(
      new SparkConf().set("spark.streaming.stopGracefullyOnShutdown", "true"), interval)

    val dstream = spark.union(
      // TODO shards size
      (0 until 1).map { _ =>
        KinesisInputDStream.builder.streamingContext(spark)
          .checkpointAppName("spark-demo-indexer")
          .streamName("spark-test")
          .endpointUrl("https://kinesis.us-east-1.amazonaws.com")
          .regionName("us-east-1")
          .initialPosition(KinesisInitialPositions.fromKinesisInitialPosition(InitialPositionInStream.LATEST))
          .checkpointInterval(interval)
          .storageLevel(StorageLevel.MEMORY_AND_DISK)
          .build
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
