package quotation;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.AbstractQuotationService;
import core.ClientInfo;
import core.Quotation;

/**
 * Implementation of the Girl Power insurance quotation service.
 *
 * @author Rem
 */
public class GPQService extends AbstractActor {
    // All references are to be prefixed with an AF (e.g. AF001000)
    ActorRef senderRef;
    ActorSystem system;
    ClientInfo clientInfo;
    double discount,price;
    final ActorRef abstractActor;
    int sequenceNumber;
    public static final String PREFIX = "GP";

    public GPQService() {
        system = ActorSystem.create("ContentSystem");
        abstractActor = system.actorOf(Props.create(AbstractQuotationService.class), "AbstractActor");
    }

    /**
     * Quote generation:
     * 50% discount for being female
     * 20% discount for no penalty points
     * 15% discount for < 3 penalty points
     * no discount for 3-5 penalty points
     * 100% penalty for > 5 penalty points
     * 5% discount per year no claims
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.RequestAQuotation.class, msg -> {
                    clientInfo = msg.info;
                    senderRef = getSender();
                    abstractActor.tell(new Messages.RequestPrice(600, 400), getSelf());
                    sequenceNumber=msg.sequenceNumber;
                })
                .match(Messages.RespondPrice.class, msg -> {
                    price = msg.price;
                    // Automatic 50% discount for being female
                    discount = (clientInfo.sex == ClientInfo.FEMALE) ? 50 : 0;

                    // Add a points discount
                    discount += getPointsDiscount(clientInfo);

                    // Add a no claims discount
                    discount += getNoClaimsDiscount(clientInfo);
                    abstractActor.tell(new Messages.RequestReference(PREFIX), getSelf());
                })
                .match(Messages.RespondReference.class, msg -> {
                    senderRef.tell(new Quotation(msg.reference, clientInfo, (price * (100 - discount)) / 100,sequenceNumber), getSelf());
                })
                .build();
    }

    private int getNoClaimsDiscount(ClientInfo info) {
        return 5 * info.noClaims;
    }

    private int getPointsDiscount(ClientInfo info) {
        if (info.points == 0) return 20;
        if (info.points < 3) return 15;
        if (info.points < 6) return 0;
        return -100;

    }

}


