import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark._
import org.apache.spark.streaming._
import org.elasticsearch.spark._
import _root_.dstream.S3InputDStream

object StreamingSample {
  def main(args: Array[String]): Unit = {
    args.headOption.map { directory =>
      val conf = new SparkConf().setAppName("sample streaming")
      val ssc = new StreamingContext(conf, Seconds(5))

      import StreamingContext._
//      val dstream = ssc.textFileStream(directory)
      val dstream = new S3InputDStream[LongWritable, Text, TextInputFormat](ssc, directory).map(_._2.toString)
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

      // more better of StreamingListener ?
      sys.ShutdownHookThread {
        println("Gracefully stopping Spark Streaming Application")
        ssc.stop(true, true)
        println("Application stopped")
      }

      ssc.start
      ssc.awaitTermination

    } getOrElse {
      System.err.println("Usage: StreamingSample <directory>")
    }
  }
}
