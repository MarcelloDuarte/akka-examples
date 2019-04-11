package net.marcelloduarte.akka.actor

import akka.actor.{Actor, ActorSystem, Props}

object HelloWorld extends App {
  import HelloWorldActor._

  object HelloWorldActor {
    trait Message
    case object Greet extends Message

    def props: Props =
      Props[HelloWorldActor]
  }

  class HelloWorldActor extends Actor {
    override def receive: Receive = {
      case Greet => println("Hello, World")
    }
  }

  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("hello-world")
    val actor = system.actorOf(HelloWorldActor.props, "hello")

    actor ! Greet
    system.terminate()
  }
}
