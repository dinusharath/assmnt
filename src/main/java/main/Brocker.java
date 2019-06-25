package main;

import Models.Messages;
import Models.ServiceRegistryBind;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.ClientInfo;
import core.Quotation;
import impl.LocalBrokerService;
import quotation.AFQService;
import quotation.DDQService;
import quotation.GPQService;
import registry.ServiceRegistry;
import vetting.LocalVettingService;

public class Brocker extends AbstractActor {
    static ServiceRegistryBind[] services = new ServiceRegistryBind[4];
    public ActorRef target;
    ActorSystem system;

public Brocker(){
    system =
            ActorSystem.create("ContentSystem");
    this.target = system.actorOf(
            Props.create(ServiceRegistry.class),
            "serviceRegister");
    services[0] = new ServiceRegistryBind("qs-GirlPowerService",system.actorOf(Props.create(GPQService.class), "GPQService"), 0);
    services[1] = new ServiceRegistryBind("qs-AuldFellasService", system.actorOf(Props.create( AFQService.class), "AFQService"), 1);
    services[2] = new ServiceRegistryBind("qs-DodgyDriversService", system.actorOf(Props.create( DDQService.class), "DDQService"), 2);
    services[3] = new ServiceRegistryBind("vs-VettingService", system.actorOf(Props.create( LocalVettingService.class), "LVQService"), 3);
}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Messages.NoOffer.class,
                        msg -> {
                            if (services.length >= msg.sequenceNumber+1) {
                                target.tell(services[msg.sequenceNumber], getSelf());
                            } else {
                                this.target = system.actorOf(
                                        Props.create(LocalBrokerService.class),
                                        "localBroker");
                                    System.out.println("Name: " + clients[0].name);
                                    target.tell(clients[0],getSelf());
                            }
                        })
                .match(Messages.Offer.class,msg->{
                    for (Quotation quotation : msg.quotation) {
                        System.out.println("Reference: " + quotation.reference + " / Price: " + quotation.price);
                    }
                    if(clients.length > msg.sequenceNumber){
                    System.out.println("Name: " + clients[msg.sequenceNumber].name);
                    target.tell(clients[msg.sequenceNumber],getSelf());
                    }
                })
                .build();
    }
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1",0),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4",1),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9",2)
    };
}
