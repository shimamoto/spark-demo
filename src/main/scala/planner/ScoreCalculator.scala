//package planner
//
//import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
//import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
//
//import scala.collection.JavaConverters._
//import scala.annotation.tailrec
//
///**
// * Created by takako.shimamoto on 15/11/05.
// */
//class ScoreCalculator extends EasyScoreCalculator[ScheduleAssignment] {
//
//  def calculateScore(solution: ScheduleAssignment): HardSoftScore = {
//    // TODO durationから起動中のscheduleを+1して積み上げ
//    // schedulesは0時からソート済み前提、1分未満はduration=1とする
//    val x = solution.getSchedules.asScala.map(_ -> 0)
//    solution.getJobs.asScala
//      .filterNot(_.getSchedule == null)
//      .foreach { job =>
//        val start = x.indexWhere(_._1 == job.getSchedule)
//        for(i <- start until start + job.getDuration){
//          val i2 = if(i >= x.size) i - x.size else i
//          x(i2) = (x(i2)._1, x(i2)._2 + 1)
//        }
//    }
//
//    println("*************")
//    x.foreach { x =>
//      if(x._2 != 0) {
//        println(x)
//      }
//    }
//    println("*************")
//
//    val working = x.toMap
//
//
//    // トータル時間 / スケジュール数 = 1スケジュールあたりの許容数
//    val available = solution.getJobs.asScala.map(_.getDuration).sum / 1440
//
//    // TODO 1分単位（スケジュールは1440個）で動いているジョブ数を計算、1440の動いているジョブ数をできるだけ平坦になるように
//    @tailrec
//    def test0(schedules: Seq[CronSchedule], score: (Int, Int)): (Int, Int) = schedules match {
//      case Nil => score
//      case x =>
//        val (hard, soft) = score
//        val duplicated = solution.getJobs.asScala.count(_.getSchedule == x.head)
//
//        test0(x.tail,(
//          // Hard constraints
//          // TODO 起動数が多すぎる場合はハードスコアをマイナス
////        if(solution.getJobs.asScala.count(_.getSchedule == x.head) > 1) hard - 10 else hard,
//          if(working(x.head) > available) hard - (working(x.head) - available) else hard,
//
//          // Soft constraints
//          // TODO 重複していたらソフトスコアをマイナス
//          if (duplicated > 1) soft - duplicated else soft
//        ))
//    }
//
//    val (hardScore, softScore) = test0(solution.getSchedules.asScala, (0, 0))
//    HardSoftScore.valueOf(hardScore, softScore)
//  }
//
//}
