package com.tamajit.communication

import akka.actor.Actor
import scala.util.Random._

/**
  * Created by ibm on 6/16/2018.
  */
class RandomNumberGeneratorActor extends Actor {

  import Messages._

  override def receive: Receive = {
    case GiveMeRandomNumber =>
      println("received a message to generate a random integer")
      val randomNumber = nextInt
      sender ! Done(randomNumber)
  }
}
