import org.apache.spark._
import org.apache.spark.streaming._
import org.elasticsearch.spark._

object StreamingSample {
  def main(args: Array[String]): Unit = {
    args.headOption.map { directory =>
      val conf = new SparkConf().setAppName("sample streaming")
      val ssc = new StreamingContext(conf, Seconds(5))

      import StreamingContext._
      val dstream = ssc.textFileStream(directory)
        .flatMap(_ split " ")
        .map(_ -> 1)
        .reduceByKey(_ + _)
        .map { case (key, value) =>
          Map(key -> value)
        }

      dstream.foreachRDD { rdd =>
        rdd.cache
        // saving data only if DStream is not empty
        if(rdd.count > 0) rdd.saveToEs("spark/big")
      }

      ssc.start
      ssc.awaitTermination
//      ssc.stop()

    } getOrElse {
      System.err.println("Usage: StreamingSample <directory>")
    }
  }
}
