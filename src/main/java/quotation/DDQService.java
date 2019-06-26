package quotation;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import core.AbstractQuotationService;
import Models.ClientInfo;
import Models.Quotation;
import akka.actor.Props;


/**
 * Implementation of Quotation Service for Dodgy Drivers Insurance Company
 *
 * @author Rem
 */
public class DDQService extends AbstractActor {
    // All references are to be prefixed with an AF (e.g. AF001000)
    ActorRef senderRef;
    ActorSystem system;
    ClientInfo clientInfo;
    double discount,price;
    final ActorRef abstractActor;
    int sequenceNumber;
    public static final String PREFIX = "DD";

    public DDQService() {
        system = ActorSystem.create("ContentSystem");
        abstractActor = system.actorOf(Props.create(AbstractQuotationService.class), "AbstractActor");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.RequestAQuotation.class, msg -> {
                    clientInfo = msg.info;
                    senderRef = getSender();
                    sequenceNumber = msg.sequenceNumber;
                    abstractActor.tell(new Messages.RequestPrice(800, 800), getSelf());
                })
                .match(Messages.RespondPrice.class, msg -> {
                    price = msg.price;

                    // 5% discount per penalty point (3 points required for qualification)
                    discount = (clientInfo.points > 3) ? 5 * clientInfo.points : -50;

                    // Add a no claims discount
                    discount += getNoClaimsDiscount(clientInfo);

                    abstractActor.tell(new Messages.RequestReference(PREFIX), getSelf());
                })
                .match(Messages.RespondReference.class, msg -> {
                    senderRef.tell(new Quotation(msg.reference, clientInfo, (price * (100 - discount)) / 100, sequenceNumber), getSelf());
                })
                .build();
    }

    private int getNoClaimsDiscount(ClientInfo info) {
        return 10 * info.noClaims;
    }


}