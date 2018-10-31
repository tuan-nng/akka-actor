package com.lightbend.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

public class AkkaQuickstart {
    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create("helloakka");

        //#create-actors
        final ActorRef printerActor =
                system.actorOf(Printer.props(), "printerActor");
        final ActorRef howdyGreeter =
                system.actorOf(Greeter.props("Howdy", printerActor), "howdyGreeter");
        final ActorRef helloGreeter =
                system.actorOf(Greeter.props("Hello", printerActor), "helloGreeter");
        final ActorRef goodDayGreeter =
                system.actorOf(Greeter.props("Good day", printerActor), "goodDayGreeter");
        //#create-actors


        ActorRef router = system.actorOf(new RoundRobinPool(4).props(Props.create(Worker.class)), "router");
        ActorRef puller = system.actorOf(Props.create(Puller.class, new Source(), router));
        long start = System.currentTimeMillis();
        puller.tell(new Puller.Start(), ActorRef.noSender());
        System.out.println(System.currentTimeMillis() - start);

        Thread.sleep(1000);
        puller.tell(new Puller.Stop(), ActorRef.noSender());
        System.out.println(router.isTerminated());
        System.out.println(puller.isTerminated());
        system.terminate();
    }
}
