package dstream

import org.apache.hadoop.fs.{PathFilter, FileSystem, Path}
import org.apache.hadoop.io.{Text, LongWritable}
import org.apache.hadoop.mapreduce.InputFormat
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.spark.rdd.{UnionRDD, RDD}
import org.apache.spark.streaming.{Time, StreamingContext}h
import org.apache.spark.streaming.dstream.{DStream, InputDStream}

import scala.io.Source
import scala.reflect.ClassTag
import scala.sys.process._

/**
 * one warning found: https://issues.scala-lang.org/browse/SI-8813
 */
// TODO checkpoints
// TODO read-through cache of file mod times
class S3InputDStream[K: ClassTag, V: ClassTag, F <: InputFormat[K,V] : ClassTag](
  @transient ssc_ : StreamingContext,
  directory: String
) extends InputDStream[(K, V)](ssc_) {

  /**
   * The filter condition to detect new files selected for that batch.
   * By default, it has to pass the following criteria.
   * - file mod time is greater than the last time
   * - file mod time is less than or equal to the current time
   */
  protected val filter = (path: Path, current: Long) => {
    val modtime = fs.getFileStatus(path).getModificationTime
    lastFileFindingTime < modtime && modtime <= current
  }

  // Timestamp of the last round of finding files
  private var lastFileFindingTime: Long = 0

  @transient private var path_ : Path = _
  @transient private var fs_ : FileSystem = _

  def start(): Unit = {
    logInfo(s"Starting S3 Stream. monitoring directory: $directory")

    // time treated in the last time batches
    // TODO file path (like a pid) and format (need to per monitoring directory)
    // TODO If the file does not exist
    lastFileFindingTime = try
      Source.fromFile("output.log").getLines.next.toLong catch {
        case e: Exception =>
          logInfo("Not found the time treated in the last time batches. Therefore settings the current time.")
          System.currentTimeMillis
      }
    logInfo(s"Initial mod time threshold: $lastFileFindingTime ms")
  }

  def stop(): Unit = {
    logInfo(s"Stopping S3 Stream. monitoring directory: $directory")

    s"echo $lastFileFindingTime" #> new java.io.File("output.log")!

    logInfo(s"Saved successfully the next mod time threshold: $lastFileFindingTime ms")
  }

  def compute(validTime: Time): Option[RDD[(K, V)]] = {
//    println("compute start: " + validTime)
    findFiles(validTime.milliseconds) map context.sparkContext
      .newAPIHadoopFile[K, V, F] partition (_.partitions.size == 0) match {
      case (errors, rdds) =>
        errors.foreach(x => logError(s"Files $x have no data in it."))
        Some(new UnionRDD(context.sparkContext, rdds))
    }
  }

  private def findFiles(current: Long): Seq[String] = {
    try {
      val files = fs.listStatus(directoryPath, new PathFilter {
        def accept(path: Path): Boolean = filter(path, current)
      }).map(_.getPath.toString)

      lastFileFindingTime = current
      files
    } catch {
      case e: Exception =>
        logDebug("Error finding new files", e)
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

object S3InputDStream {
  def apply(ssc: StreamingContext, directory: String): DStream[String] =
    new S3InputDStream[LongWritable, Text, TextInputFormat](ssc, directory).map(_._2.toString)

}
