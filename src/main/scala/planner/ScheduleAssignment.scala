package planner

import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.solution.{PlanningEntityCollectionProperty, Solution, PlanningSolution}
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.domain.variable.PlanningVariable
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore

import scala.beans.BeanProperty

/**
 * Created by takako.shimamoto on 15/11/05.
 */
//@PlanningSolution
//class ScheduleAssignment(
//  val schedule: Seq[CronSchedule],
//  val jobs: Seq[JobProcess],
//  @BeanProperty var score: HardSoftScore
//) extends Solution[HardSoftScore] {
//  import scala.collection.JavaConverters._
//
//  // Do not add the planning entity's (jobs) because that will be done automatically
//  def getProblemFacts: java.util.Collection[_ <: AnyRef] = {
//    Seq(schedule).asJava
//  }
//
//  @ValueRangeProvider(id = "scheduleRange") def getSchedule: java.util.Collection[CronSchedule] = schedule.asJava
//  @PlanningEntityCollectionProperty def getJobs: java.util.Collection[JobProcess] = jobs.asJava
//
//}

//@PlanningEntity(difficultyComparatorClass = CloudProcessDifficultyComparator.class)
//@PlanningEntity
//class JobProcess(
//  val key: Int,
//  val duration: Int,  // in minutes per day
//  val explicitSchedule: Option[CronSchedule] = None,
//  // Planning variables: changes during planning, between score calculations.
////@PlanningVariable(valueRangeProviderRefs = {"computerRange"},
////  strengthComparatorClass = CloudComputerStrengthComparator.class)
//  var schedule: CronSchedule
//){
//  @PlanningVariable(valueRangeProviderRefs = Array("scheduleRange"))
//  def getSchedule: CronSchedule = schedule
//  def setSchedule(s: CronSchedule): Unit = schedule = s
//}

case class CronSchedule(
  seconds: Int = 0,     // random?
  minutes: Int,
  hours: Int,
  day: String = "* * ?" // every day
){

  def cron: String = s"$seconds $minutes $hours $day"

}
