package com.lightbend.akka.sample;

import akka.actor.AbstractActor;

import java.util.List;
import java.util.Random;

public class Worker extends AbstractActor {

    static public class Data {
        private List<Long> data;

        public Data(List<Long> data) {
            this.data = data;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    System.out.println(Thread.currentThread().getName());
                })
                .match(Data.class, data -> {
                    Thread.sleep(new Random().nextInt(100));
                    System.out.println(Thread.currentThread().getName() + ": length " + data.data.size() + ", " + data.data);
                    getSender().tell(new Puller.Query(), getSelf());
                })
                .build();
    }
}
