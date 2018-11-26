package com.tamajit.fibonacci
import akka.actor.{Props, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

object FibonacciActorApp extends App {
  implicit val timeout = Timeout(10 seconds)
  val actorSystem = ActorSystem("HelloAkka")
  val actor = actorSystem.actorOf(Props[FibonacciActor])
  // asking for result from actor
  val future = (actor ? 10).mapTo[Int]
  val fiboacciNumber = Await.result(future, 10 seconds)
  println(fiboacciNumber)
}

