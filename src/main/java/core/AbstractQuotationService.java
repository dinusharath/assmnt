package core;

import Models.Messages;
import akka.actor.AbstractActor;

import java.util.Random;

public class AbstractQuotationService extends AbstractActor {
    private int counter = 1000;
    private Random random = new Random();

    protected String generateReference(String prefix) {
        String ref = prefix;
        int length = 100000;
        while (length > 1000) {
            if (counter / length == 0) ref += "0";
            length = length / 10;
        }
        return ref + counter++;
    }

    protected double generatePrice(double min, int range) {
        return min + (double) random.nextInt(range);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.RequestReference.class, msg -> getSender().tell(new Messages.RespondReference(generateReference(msg.prefix)), getSelf()))
                .match(Messages.RequestPrice.class, msg -> getSender().tell(new Messages.RespondPrice(generatePrice(msg.min, msg.range)), getSelf()))
                .build();
    }
}
