package net.marcelloduarte.akka.actor

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object SendingMessageFromAnActor extends App {
  trait Message
  case class Marco(from: ActorRef) extends Message
  case class Polo(from: ActorRef) extends Message

  class Person(name: String) extends Actor {
    var count = 0
    override def receive: Receive = {
      case Marco(other) => increaseCountAnPrint("Polo", other, Polo(self))
      case Polo(other) => increaseCountAnPrint("Marco", other, Marco(self))
    }

    private def increaseCountAnPrint(message: String, other: ActorRef, newMessage: Message): Unit =
      if (count < 5) {
        count = count + 1
        println(s"$name: $message")
        other ! newMessage
      }
  }

  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("people")
    val john = system.actorOf(Props(new Person("John")), "john")
    val susie = system.actorOf(Props(new Person("Susie")), "susie")

    john ! Marco(susie)

    system.terminate()
  }
}
