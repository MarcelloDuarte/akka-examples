package net.marcelloduarte.akka.local

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

import scala.io.StdIn

object RemoteActorApp extends App {

  class RemoteActor extends Actor{
    val log = Logging(context.system, this)

    override def receive: Receive = {
      case msg: String =>
        log.info(s"RemoteActor received message '$msg'")
        sender ! "Message received by the RemoteActor"
    }
  }

  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("RemoteDemoSystem")
    val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
    remoteActor ! "The RemoteActor is alive"
    StdIn.readLine()
    system.terminate()
    StdIn.readLine()
  }
}
