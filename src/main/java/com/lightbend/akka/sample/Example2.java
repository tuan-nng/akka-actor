package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class Example2 {
    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = akka.actor.ActorSystem.create("helloakka");

        ActorRef router = system.actorOf(new RoundRobinPool(4).props(Props.create(Worker.class)), "router");

        ActorRef puller = system.actorOf(Props.create(Puller.class, new Source(), router));


        puller.tell(new Puller.Start(), ActorRef.noSender());

        Thread.sleep(1000);
        puller.tell(new Puller.Stop(), ActorRef.noSender());

        Thread.sleep(1000);
        System.out.println(router.isTerminated());
        System.out.println(puller.isTerminated());
        system.terminate();
    }
}
