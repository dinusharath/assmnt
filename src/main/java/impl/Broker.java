package impl;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.ClientInfo;
import core.Quotation;
import registry.ServiceRegistry;
import vetting.LocalVettingService;

import java.util.LinkedList;
import java.util.List;

public class Broker extends AbstractActor {
    final ActorRef localVetService;
    ActorSystem system;
    ClientInfo clientInfo;
    List<Quotation> quotations;
    public ActorRef serviceReg;

    public Broker() {
        system = ActorSystem.create("ContentSystem");
        localVetService = system.actorOf(Props.create(LocalVettingService.class), "localVetService");
        quotations = new LinkedList<Quotation>();
        serviceReg = system.actorOf(Props.create(ServiceRegistry.class), "serviceReg");


    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClientInfo.class, msg -> {
                    clientInfo=msg;
                    localVetService.tell(new Messages.RequestVetService(msg), getSelf());
                    quotations.clear();
                    System.out.println("Name: " + msg.name);
                })
                .match(Messages.ServiceRegistryBind.class, msg -> {
                    serviceReg.tell(msg, getSelf());
                })
                .match(Quotation.class, msg -> quotations.add(msg))
                .match(Messages.NoOffer.class, msg ->{
                    for(Quotation quotation :quotations){
                        System.out.println("Reference: " + quotation.reference + " / Price: " + quotation.price);
                    }
                })
                .match(Messages.RespondVetService.class, msg -> {
                    if (msg.equal) {
                        serviceReg.tell(new Messages.RequestQuotations(0, clientInfo), getSelf());
                    } else {
                        System.out.println("Sorry no quotations qualified for you");
                    }
                })
                .build();
    }
//    public static final ClientInfo[] clients = {
//            new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"),
//            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
//            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
//    };
}
