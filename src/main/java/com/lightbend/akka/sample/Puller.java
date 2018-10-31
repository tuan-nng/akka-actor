package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.routing.Broadcast;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Puller extends AbstractActor {
    private Source source;
    private ActorRef worker;
    private int count = 0;

    public Puller(Source source, ActorRef worker) {
        this.source = source;
        this.worker = worker;
    }

    static public class Start {
        public Start() {
        }
    }

    static public class Query {
        public Query() {
        }
    }

    static public class Stop {
        public Stop() {
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Start.class, x -> {
                    List<Long> data = source.getData();
                    worker.tell(new Worker.Data(data), getSelf());
                })
                .match(Query.class, x -> {
                    if (count < 7) {
                        getData();
                        getData();
                        getData();
                        count++;
                    }
                })
                .match(Stop.class, x -> {
                    worker.tell(new Broadcast(PoisonPill.getInstance()), getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    void getData() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return source.getData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Collections.<Long>emptyList();
        })
                .thenAcceptAsync(result -> worker.tell(new Worker.Data(result), getSelf()));
    }
}
