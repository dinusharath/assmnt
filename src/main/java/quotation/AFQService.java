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
 * Implementation of the AuldFellas insurance quotation service.
 *
 * @author Rem
 */
public class AFQService extends AbstractActor {
    // All references are to be prefixed with an AF (e.g. AF001000)

    ActorRef senderRef;
    ActorSystem system;
    ClientInfo clientInfo;
    double discount,price;
    final ActorRef abstractActor;
    int sequenceNumber;
    public static final String PREFIX = "AF";

    public AFQService() {
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
                    abstractActor.tell(new Messages.RequestPrice(600, 600), getSelf());
                })
                .match(Messages.RespondPrice.class, msg -> {
                    price = msg.price;
                    // Automatic 30% discount for being male
                    discount = (clientInfo.sex == ClientInfo.MALE) ? 30 : 0;

                    // Automatic 2% discount per year over 60...
                    discount += (clientInfo.age > 60) ? (2 * (clientInfo.age - 60)) : 0;

                    // Add a points discount
                    discount += getPointsDiscount(clientInfo);
                    abstractActor.tell(new Messages.RequestReference(PREFIX), getSelf());
                })
                .match(Messages.RespondReference.class, msg -> {
                    senderRef.tell(new Quotation(msg.reference, clientInfo, (price * (100 - discount)) / 100, sequenceNumber), getSelf());
                })
                .build();
    }


    /**
     * Quote generation:
     * 30% discount for being male
     * 2% discount per year over 60
     * 20% discount for less than 3 penalty points
     * 50% penalty (i.e. reduction in discount) for more than 60 penalty points
     */

    private int getPointsDiscount(ClientInfo info) {
        if (info.points < 3) return 20;
        if (info.points <= 6) return 0;
        return -50;

    }

}
