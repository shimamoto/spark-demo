import org.apache.spark._
import org.apache.spark.streaming._
import org.elasticsearch.spark._
import org.elasticsearch.hadoop.cfg.ConfigurationOptions._
import _root_.dstream.{LocalInputDStream, S3InputDStream}

object StreamingSample {
  def main(args: Array[String]): Unit = {
    args.headOption.map { directory =>
      val conf = new SparkConf().setAppName("sample streaming")
      val ssc = new StreamingContext(conf, Seconds(5))

      val r = new scala.util.Random

      import StreamingContext._
//      val dstream = ssc.textFileStream(directory)
//      val dstream = S3InputDStream(ssc, directory)
      val dstream = new LocalInputDStream(ssc, directory)
        .flatMap(x => (x split " ").map(_.replaceAll("\n", "")))
        .map(_ -> 1)
        .reduceByKey(_ + _)
        .map { case (key, value) =>
          val documentId = "43bc5b1181f41ca28ddb3aca505c4c196b38c2ba12c217885a2f9f1c47cd58c" + scala.math.max(r.nextInt(10), value)
          Map(key -> value, "documentId" -> documentId)
        }

      dstream.foreachRDD { rdd =>
        rdd.saveToEs(Map(
          ES_NODES    -> "localhost",
          ES_RESOURCE -> "demo/job",
          ES_WRITE_OPERATION -> ES_OPERATION_UPSERT,
          ES_MAPPING_ID -> "documentId"
        ))
      }

      ssc.start
      ssc.awaitTermination

    } getOrElse {
      System.err.println("Usage: StreamingSample <directory>")
    }
  }
}
