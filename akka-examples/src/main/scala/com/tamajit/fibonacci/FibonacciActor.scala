package com.tamajit.fibonacci

import akka.actor.Actor

/**
  * Created by ibm on 6/16/2018.
  */
class FibonacciActor extends Actor {
  override def receive: Receive = {
    case num : Int =>
      val fibonacciNumber = fib(num)
      sender ! fibonacciNumber
  }
  def fib( n : Int) : Int = n match {
    case 0 | 1 => n
    case _ => fib( n-1 ) + fib( n-2 )
  }
}

