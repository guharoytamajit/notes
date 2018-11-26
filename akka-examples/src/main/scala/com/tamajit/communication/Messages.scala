package com.tamajit.communication

import akka.actor.ActorRef

/**
  * Created by ibm on 6/16/2018.
  */
object Messages {

  case class Done(randomNumber: Int)

  case object GiveMeRandomNumber

  case class Start(actorRef: ActorRef)

}
