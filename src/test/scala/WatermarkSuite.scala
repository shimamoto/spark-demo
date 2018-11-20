/*
import java.sql.Timestamp

import org.apache.spark.sql.execution.streaming.MemoryStream
import org.apache.spark.sql.streaming.{GroupState, GroupStateTimeout, OutputMode, StreamTest}

case class Document(eventTime: Timestamp, uid: String, data: String)

class WatermarkSuite extends StreamTest {
  import testImplicits._

  val stateFunc = (key: String, values: Iterator[Document], oldState: GroupState[Timestamp]) => {
    val latest = values.maxBy(_.eventTime.getTime)

    // the timeout would occur when the watermark advances beyond the set timestamp
    println(oldState.getOption)
    println(oldState.hasTimedOut)

    oldState.getOption.filter(_.after(latest.eventTime))
      .map { x =>
        // out of order data
        Iterator.empty
      }
      .getOrElse {
        oldState.update(latest.eventTime)
        Iterator(latest)
      }
  }

  test("streaming - test watermark") {
    val input = MemoryStream[(String, String, String)]
    val ds = input.toDF().toDF("eventTime", "uid", "data")
      .withColumn("eventTime", $"eventTime".cast("timestamp"))
      .withWatermark("eventTime", "10 seconds")
      .as[Document]
      .groupByKey(_.uid)
      .flatMapGroupsWithState(OutputMode.Update, GroupStateTimeout.EventTimeTimeout)(stateFunc)

    val query = ds.writeStream
      .outputMode("update")
      .format("console")
      .start()

    new Thread(new Runnable() {
      override def run(): Unit = {
        /* currentWatermarkMs = 0
         * -------------------------------------------
         * Batch: 0
         * -------------------------------------------
         * +-------------------+---+-------+
         * |          eventTime|uid|   data|
         * +-------------------+---+-------+
         * |2017-12-11 09:30:01| a2|data2-2|
         * |2017-12-11 09:30:00| a1|  data1|
         * +-------------------+---+-------+
         */
        input.addData(
          ("2017-12-11 09:30:00", "a1", "data1"),   // ok
          ("2017-12-11 09:30:00", "a2", "data2-1"), // before data2-2
          ("2017-12-11 09:30:01", "a2", "data2-2")) // ok
        while (!query.isActive) {
          // wait the query to activate
        }
        Thread.sleep(20000)

        /* currentWatermarkMs = 2017-12-11 09:29:51
         * -------------------------------------------
         * Batch: 1
         * -------------------------------------------
         * +-------------------+---+-------+
         * |          eventTime|uid|   data|
         * +-------------------+---+-------+
         * |2017-12-11 09:30:15| a3|data3-1|
         * +-------------------+---+-------+
         */
        input.addData(
          ("2017-12-11 09:29:59", "a2", "data2-0"), // before data2-2
          ("2017-12-11 09:30:15", "a3", "data3-1")) // ok

        Thread.sleep(20000)

        /* currentWatermarkMs = 2017-12-11 09:30:05
         * -------------------------------------------
         * Batch: 2
         * -------------------------------------------
         * +-------------------+---+-------+
         * |          eventTime|uid|   data|
         * +-------------------+---+-------+
         * |2017-12-11 09:30:16| a3|data3-2|
         * |2017-12-11 09:30:30| a4|  data4|
         * +-------------------+---+-------+
         */
        input.addData(
          ("2017-12-11 09:30:04", "a1", "data10"),  // time out of watermark
          ("2017-12-11 09:30:16", "a3", "data3-2"), // ok
          ("2017-12-11 09:30:30", "a4", "data4"))   // ok

        // Batch: 3, currentWatermarkMs = 2017-12-11 09:30:20

      }
    }).start()

    query.awaitTermination(60000)

  }

}
*/
