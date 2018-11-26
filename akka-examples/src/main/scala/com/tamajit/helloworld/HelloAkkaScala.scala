package com.tamajit.helloworld

import akka.actor.{ActorSystem, Props}

/**
  * Created by ibm on 6/16/2018.
  */
object HelloAkkaScala extends App {

  // Create the 'hello akka' actor system
  val system = ActorSystem("Hello-Akka")

  // Create the 'greeter' actor
  val greeter = system.actorOf(Props[Greeter], "greeter")

  // Send WhoToGreet Message to actor
  greeter ! WhoToGreet("Akka")

  //shutdown actorsystem
  system.terminate()

}
