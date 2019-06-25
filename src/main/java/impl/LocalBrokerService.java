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
import vetting.VettingService;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
public class LocalBrokerService extends AbstractActor {
//    VettingService vettingService = (VettingService) ServiceRegistry.lookups("vs-VettingService");
    List<Quotation> quotations;
    ActorSystem system;
    ActorRef client;
    int clientSequence;
    final ActorRef serviceReg;
    final ActorRef localvetservice;
    ClientInfo info;

    public LocalBrokerService() {
         system = ActorSystem.create("ContentSystem");
        quotations = new LinkedList<Quotation>();
        serviceReg =
                system.actorOf(
                        Props.create(ServiceRegistry.class),
                        "serviceReg");
        localvetservice =
                system.actorOf(
                        Props.create(LocalVettingService.class),
                        "localvetservice");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClientInfo.class, msg -> {
                    info=msg;
                    quotations.clear();
                    client = getSender();
                    clientSequence = info.sequenceNumber;
                    localvetservice.tell(new Messages.RequestVetService(msg),getSelf());
                })
                .match(Quotation.class, msg -> {
                    quotations.add(msg);
                })
                .match(Messages.NoOffer.class, msg -> {
                    client.tell(new Messages.Offer(clientSequence + 1, quotations), getSelf());
                })
                .match(Messages.RespondVetService.class,msg->{
                    if (msg.equal) {
                        serviceReg.tell(new Messages.RequestQuotations(0,info), getSelf());
                    }else {
                        client.tell(new Messages.Offer(info.sequenceNumber + 1, quotations), getSelf());
                    }
                })
                .build();

    }
}
