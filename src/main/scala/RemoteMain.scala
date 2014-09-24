import akka.actor.{Props, ActorSystem}

object RemoteMain {
  def main(args: Array[String]): Unit = {
    // TODO Spark side should be ok because it made​ActorSystem in the config specified.
    val system = ActorSystem("spark-demo")
    system.actorOf(Props[actors.SparkActor], "extractor")

  }
}
