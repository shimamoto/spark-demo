package dstream

import java.io.File

import org.apache.spark.rdd.{UnionRDD, RDD}
import org.apache.spark.streaming.{Time, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream

class LocalInputDStream(
  @transient ssc_ : StreamingContext,
  directory: String
) extends InputDStream[String](ssc_) {

  private var lastFileFindingTime: Long = 0

  def start(): Unit = {}
  def stop(): Unit = {}

  def compute(validTime: Time): Option[RDD[String]] = {
    val rdds = new File(directory).listFiles().collect {
      case f if f.lastModified > lastFileFindingTime =>
        context.sparkContext.makeRDD(Seq(scala.io.Source.fromFile(f).mkString))
    }.toSeq

    lastFileFindingTime = validTime.milliseconds

    Some(new UnionRDD(context.sparkContext, rdds))
  }

}
