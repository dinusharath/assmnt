package impl;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import Models.ClientInfo;
import Models.Quotation;
import quotation.AFQService;
import quotation.DDQService;
import quotation.GPQService;
import registry.ServiceRegistry;
import vetting.LocalVettingService;

import java.util.LinkedList;
import java.util.List;

public class Broker extends AbstractActor {
    ActorSystem system;
    ClientInfo clientInfo;
    List<Quotation> quotations;
    ActorRef serviceReg, senderRef;

    public Broker() {
        system = ActorSystem.create("ContentSystem");
        quotations = new LinkedList<Quotation>();
        serviceReg = system.actorOf(Props.create(ServiceRegistry.class), "serviceReg");
        Messages.ServiceRegistryBind[] services = new Messages.ServiceRegistryBind[4];

        services[0] = new Messages.ServiceRegistryBind("qs-GirlPowerService",system.actorOf(Props.create(GPQService.class), "GPQService"), 0);
        services[1] = new Messages.ServiceRegistryBind("qs-AuldFellasService", system.actorOf(Props.create( AFQService.class), "AFQService"), 1);
        services[2] = new Messages.ServiceRegistryBind("qs-DodgyDriversService", system.actorOf(Props.create( DDQService.class), "DDQService"), 2);
        services[3] = new Messages.ServiceRegistryBind("vs-VettingService", system.actorOf(Props.create( LocalVettingService.class), "LVQService"), 3);
        serviceReg.tell(services[0], getSelf());
        serviceReg.tell(services[1], getSelf());
        serviceReg.tell(services[2], getSelf());
        serviceReg.tell(services[3], getSelf());

    }

    public static Props createProps() {
        return Props.create(Broker.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.Init.class, msg -> {
                    clientInfo = msg.clientInfo;
                    quotations.clear();
                    senderRef = getSender();
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
                    senderRef.tell(msg, getSelf());
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
