package net.marcelloduarte.akka.actor

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object AskPattern extends App {
  trait Message
  case class Sum(a: Int, b: Int)

  class Operation extends Actor {
    override def receive: Receive = {
      case Sum(a, b) => sender ! (a + b)
    }
  }

  val system = ActorSystem("adding-numbers")
  val operation = system.actorOf(Props[Operation], "adder")
  implicit val timeout: Timeout = Timeout(1.second)

  for {
    result <- operation ? Sum(1, 2)
  } yield println(result)

  system.terminate()
}
