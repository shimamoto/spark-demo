//package planner
//
//import java.time.LocalDateTime
//
//import org.optaplanner.core.api.solver.SolverFactory
//
//import scala.collection.JavaConverters._
//
///**
// * Created by takako.shimamoto on 15/11/05.
// */
//object Main {
//  def main(args: Array[String]): Unit = {
//    // Build the Solver
//    val solver = SolverFactory.createFromXmlResource("solverConfig.xml").buildSolver()
//
//    // Solve the problem
//    solver.solve(currentSchedule)
//    val solved = solver.getBestSolution.asInstanceOf[ScheduleAssignment]
//
//    // Display the result
//    println("Solved :")
//    solved.getJobs.asScala.foreach { x =>
//      println(s"${x.getKey}: ${Option(x.getSchedule).map(_.cron)}")
//    }
//  }
//
//  def currentSchedule: ScheduleAssignment = {
////    val r = new util.Random
//    val jobs = (1 to 100).map { id =>
//      val job = new JobProcess
//      job.setKey(id)
//      job.setDuration(if(id % 2 == 0) 60 else 120)
//      // Notice that we leave the PlanningVariable properties on null
////      job.setSchedule(CronSchedule(
////        minutes = if(id % 2 == 0) 0 else 30,
////        hours   = r.nextInt(10)
////      ))
//      job
//    }
//
//    val now = LocalDateTime.now()
//    val schedule = (0 until 1440).map { i =>
//      val x = now.minusMinutes(i)
//      CronSchedule(
//        minutes = x.getMinute,
//        hours   = x.getHour
//      )
//    }.reverse
//
//    val original = new ScheduleAssignment
//    original.setJobs(jobs.asJava)
//    original.setSchedules(schedule.asJava)
//    original
//  }
//
//}
