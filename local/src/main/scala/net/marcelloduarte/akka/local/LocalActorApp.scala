package net.marcelloduarte.akka.local

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object LocalActorApp extends App {
  sealed trait Msg
  object SendMessageToRemote extends Msg

  class LocalActor extends Actor{
    val log = Logging(context.system, this)
    val path = "akka.tcp://RemoteDemoSystem@127.0.0.1:2222/user/RemoteActor"

    override def receive: Receive = {
      case SendMessageToRemote =>
        implicit val resolveTimeout: Timeout = Timeout(5.seconds)
        for (ref : ActorRef <- context.actorSelection(path).resolveOne()) {
          println("Resolved remote actor ref using Selection")
          context.watch(ref)
          context.setReceiveTimeout(Duration.Undefined)
          ref ! "Hello from local actor"
        }
    }
  }

  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("LocalDemoSystem")
    val localActor = system.actorOf(Props[LocalActor], "LocalActor")
    localActor ! SendMessageToRemote
  }
}


