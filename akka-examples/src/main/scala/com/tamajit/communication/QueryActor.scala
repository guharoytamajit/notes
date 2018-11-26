package com.tamajit.communication

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
  * Created by ibm on 6/16/2018.
  */
class QueryActor extends Actor {
  import Messages._
  override def receive: Receive = {
    case Start(actorRef) => println(s"send me the next random number")
      actorRef ! GiveMeRandomNumber
      case Done(randomNumber) =>
      println(s"received a random number $randomNumber")
  }
}
