package dstream

import org.apache.hadoop.fs.{PathFilter, FileSystem, Path}
import org.apache.hadoop.mapreduce.InputFormat
import org.apache.spark.rdd.{UnionRDD, RDD}
import org.apache.spark.streaming.{Time, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream

import scala.reflect.ClassTag

/**
 * one warning found: https://issues.scala-lang.org/browse/SI-8813
 */
class S3InputDStream[K: ClassTag, V: ClassTag, F <: InputFormat[K,V] : ClassTag](
  @transient ssc_ : StreamingContext,
  directory: String,
  filter: Path => Boolean
) extends InputDStream[(K, V)](ssc_) {

  // Timestamp of the last round of finding files
//  @transient private var lastFileFindingTime = 0L
  @transient private var path_ : Path = _
  @transient private var fs_ : FileSystem = _

  def start(): Unit = {
    println("***** Spark Streaming system to start *****")
  }

  def stop(): Unit = {
    println("***** Spark Streaming system to stop *****")
  }

  def compute(validTime: Time): Option[RDD[(K, V)]] = {
    findFiles map context.sparkContext.newAPIHadoopFile[K, V, F] partition (_.partitions.size == 0)
    match {
      case (error, rdds) =>
        Some(new UnionRDD(context.sparkContext, rdds))
    }
  }

  private def findFiles(): Seq[String] = {
    try {
      fs.listStatus(directoryPath, new PathFilter {
        def accept(path: Path): Boolean = filter(path)
      }).map(_.getPath.toString)
    } catch {
      case e: Exception =>
        fs_ = null  // reset
        Nil
    }
  }

  private def directoryPath: Path = {
    if (path_ == null) path_ = new Path(directory)
    path_
  }

  private def fs: FileSystem = {
    if (fs_ == null) fs_ = directoryPath.getFileSystem(context.sparkContext.hadoopConfiguration)
    fs_
  }

}
