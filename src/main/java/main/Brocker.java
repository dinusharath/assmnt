package main;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.ClientInfo;
import core.Quotation;
import impl.LocalBrokerService;
import registry.ServiceRegistry;

public class Brocker extends AbstractActor {
    public ActorRef target;
    ActorSystem system;

    public Brocker() {
        system = ActorSystem.create("ContentSystem");
        this.target = system.actorOf(Props.create(ServiceRegistry.class), "serviceRegister");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.NoOffer.class,
                        msg -> {
                        })
                .match(Messages.Offer.class, msg -> {
                    for (Quotation quotation : msg.quotation) {
                        System.out.println("Reference: " + quotation.reference + " / Price: " + quotation.price);
                    }
                })
                .match(ClientInfo.class, msg -> {
                    this.target = system.actorOf(Props.create(LocalBrokerService.class), "localBroker");
                    System.out.println("Name: " + msg.name);
                    target.tell(msg, getSelf());
                })
                .match(Messages.ServiceRegistryBind.class, msg -> {
                    target.tell(msg, getSelf());
                })
                .build();
    }
//    public static final ClientInfo[] clients = {
//            new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1",0),
//            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4",1),
//            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9",2)
//    };
}
