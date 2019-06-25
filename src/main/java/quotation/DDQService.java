package quotation;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import core.AbstractQuotationService;
import core.ClientInfo;
import core.Quotation;
import akka.actor.Props;


/**
 * Implementation of Quotation Service for Dodgy Drivers Insurance Company
 *
 * @author Rem
 */
public class DDQService extends AbstractActor {
    // All references are to be prefixed with an AF (e.g. AF001000)
    public static final String PREFIX = "DD";
    ActorSystem system;
    final ActorRef AbstractActor;
    double price;
    ClientInfo clientInfo;
    double discount;
    ActorRef client;
    int sequenceNumber;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.RequestAQuotation.class, msg -> {
                    clientInfo = msg.info;
                    client = getSender();
                    sequenceNumber=msg.sequenceNumber;
                    AbstractActor.tell(new Messages.RequestPrice(800, 800), getSelf());
                })
                .match(Messages.RespondPrice.class, msg -> {
                    price = msg.price;

                    // 5% discount per penalty point (3 points required for qualification)
                    discount = (clientInfo.points > 3) ? 5 * clientInfo.points : -50;

                    // Add a no claims discount
                    discount += getNoClaimsDiscount(clientInfo);

                    AbstractActor.tell(new Messages.RequestReference(PREFIX), getSelf());
                })
                .match(Messages.RespondReference.class, msg -> {
                    client.tell(new Quotation(msg.reference, clientInfo, (price * (100 - discount)) / 100,sequenceNumber), getSelf());
                })
                .build();
    }

    public DDQService() {
        system =
                ActorSystem.create("ContentSystem");

        AbstractActor =
                system.actorOf(
                        Props.create(AbstractQuotationService.class),
                        "AbstractActor");
    }

    private int getNoClaimsDiscount(ClientInfo info) {
        return 10 * info.noClaims;
    }


}