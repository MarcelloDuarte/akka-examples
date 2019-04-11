package net.marcelloduarte.akka.actor

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object OneForOne extends App {
  case object CreateChild
  case class Divide(a:Int, b:Int)

  class ParentActor extends Actor {
    var number: Int = 0

    override def receive: Receive = {
      case CreateChild =>
        context.actorOf(Props[ChildActor], "child" + number)
        number = number + 1
    }

    override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy(loggingEnabled = false) {
      case ae: ArithmeticException => Resume
      case ie: IllegalArgumentException => Escalate
      case _ => Restart
    }
  }

  class ChildActor extends Actor {
    override def receive: Receive = {
      case Divide(a,b) => sender ! a / b
    }
  }

  val system = ActorSystem("supervision")
  val parentActor = system.actorOf(Props[ParentActor], "parent")
  parentActor ! CreateChild
  val childActor = system.actorSelection("/user/parent/child0")

  implicit val timeout: Timeout = Timeout(2.seconds)

  for {
    r <- childActor ? Divide(4,0)
  } yield println(r)

  system.terminate()

}
