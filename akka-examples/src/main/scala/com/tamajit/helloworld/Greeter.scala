package com.tamajit.helloworld

import akka.actor.Actor

/**
  * Created by ibm on 6/16/2018.
  */
// Define Greeter Actor
class Greeter extends Actor {
  def receive = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}
