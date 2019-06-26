package impl;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import Models.ClientInfo;
import Models.Quotation;
import registry.ServiceRegistry;

import java.util.LinkedList;
import java.util.List;

public class Broker extends AbstractActor {
    ActorSystem system;
    ClientInfo clientInfo;
    List<Quotation> quotations;
    public ActorRef serviceReg;

    public Broker() {
        system = ActorSystem.create("ContentSystem");
        quotations = new LinkedList<Quotation>();
        serviceReg = system.actorOf(Props.create(ServiceRegistry.class), "serviceReg");


    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Init.class, msg -> {
                    clientInfo = msg.clientInfo;
                    quotations.clear();
                    serviceReg.tell(new Messages.RequestQuotations(msg.sequenceNumber, clientInfo), getSelf());
                    System.out.println("Name: " + msg.clientInfo.name);
                })
                .match(Messages.ServiceRegistryBind.class, msg -> {
                    serviceReg.tell(msg, getSelf());
                })
                .match(Quotation.class, msg -> quotations.add(msg))
                .match(Messages.Offer.class, msg -> {
                    for (Quotation quotation : msg.quotation) {
                        System.out.println("Reference: " + quotation.reference + " / Price: " + quotation.price);
                    }
                })
                .match(Messages.NoOffer.class, msg -> {
                    System.out.println("Sorry no quotations qualified for you");
                })
                .build();
    }
//    public static final ClientInfo[] clients = {
//            new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"),
//            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
//            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
//    };
}
