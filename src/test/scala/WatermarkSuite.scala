import java.sql.Timestamp

import org.apache.spark.sql.execution.streaming.MemoryStream
import org.apache.spark.sql.functions.max
import org.apache.spark.sql.streaming.StreamTest

class WatermarkSuite extends StreamTest {
  import testImplicits._

  test("streaming - test watermark") {
    val input = MemoryStream[(String, String, String)]
    val ds = input.toDF().toDF("created", "uid", "data")
      .withColumn("created", $"created".cast("timestamp"))
      .withWatermark("created", "10 seconds")
      .groupBy("uid")
      .agg(max("created"))

    val query = ds.writeStream
      .outputMode("update")
      .format("console")
      .start()

    val now = "2017-12-11 09:30:00"
    new Thread(new Runnable() {
      override def run(): Unit = {
        /* -------------------------------------------
         * Batch: 0
         * -------------------------------------------
         * +---+-------------------+
         * |uid|       max(created)|
         * +---+-------------------+
         * | a2|2017-12-11 09:30:01|
         * | a1|2017-12-11 09:30:00|
         * +---+-------------------+
         */
        input.addData((now, "a1", "data1"), (now, "a2", "data2-1"),
          ("2017-12-11 09:30:01", "a2", "data2-2"))
        while (!query.isActive) {
          // wait the query to activate
        }
        Thread.sleep(20000)

        /* -------------------------------------------
         * Batch: 1
         * -------------------------------------------
         * +---+-------------------+
         * |uid|       max(created)|
         * +---+-------------------+
         * | a2|2017-12-11 09:30:01|
         * | a3|2017-12-11 09:30:10|
         * +---+-------------------+
         */
        input.addData(
          ("2017-12-11 09:29:59", "a2", "data2-0"), ("2017-12-11 09:30:10", "a3", "data3"))

        Thread.sleep(20000)

        /* -------------------------------------------
         * Batch: 2
         * -------------------------------------------
         * +---+-------------------+
         * |uid|       max(created)|
         * +---+-------------------+
         * | a4|2017-12-11 09:30:20|
         * | a1|2017-12-11 09:30:00|
         * +---+-------------------+
         */
        val timeOutOfWatermark = "2017-12-11 09:29:30"
        input.addData((timeOutOfWatermark, "a1", "data10"),
          ("2017-12-11 09:30:20", "a4", "data4"))
      }
    }).start()

    query.awaitTermination(60000)

  }

}
