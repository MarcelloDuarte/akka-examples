package net.marcelloduarte.akka.actor

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global

object ActorSelectionAndCreateActorWithinActor extends App {
  case class Divide(a:Int, b:Int)
  case object CreateChild

  class ParentActor extends Actor {

    var number: Int = 0

    override def receive: Receive = {
      case CreateChild =>
        context.actorOf(Props[ChildActor], "child" + number)
        number = number + 1
    }
  }

  class ChildActor extends Actor {
    override def receive: Receive = {
      case Divide(a,b) => sender ! a / b
    }
  }

  val system = ActorSystem("parent-child")
  val parentActor = system.actorOf(Props[ParentActor], "parent")
  parentActor ! CreateChild
  val childActor = system.actorSelection("/user/parent/child0")

  implicit val timeout: Timeout = Timeout(2.seconds)

  for {
    r <- childActor ? Divide(4,2)
  } yield println(r)

  system.terminate()
}
