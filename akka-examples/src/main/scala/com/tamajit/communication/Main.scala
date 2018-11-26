package com.tamajit.communication

import akka.actor.{ActorSystem, Props}

/**
  * Created by ibm on 6/16/2018.
  */
object Main extends App {
  import Messages._
  val actorSystem = ActorSystem("HelloAkka")
  val randomNumberGenerator =
    actorSystem.actorOf(Props[RandomNumberGeneratorActor],
      "randomNumberGeneratorActor")
  val queryActor = actorSystem.actorOf(Props[QueryActor],
    "queryActor")
  queryActor ! Start(randomNumberGenerator)
}
