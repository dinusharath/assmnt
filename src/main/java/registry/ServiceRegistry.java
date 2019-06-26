package registry;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import core.ClientInfo;
import core.Quotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a basic service registry implementation that is based on the registry used in
 * RMI systems.
 *
 * @author Rem
 */
public class ServiceRegistry extends AbstractActor {
    ActorRef quationSender;
    ActorSystem system;
    ClientInfo clientInfo;
    List names;

    public ServiceRegistry() {
        system = ActorSystem.create("ContentSystem");
        names = new ArrayList();
    }

    private static Map<String, ActorRef> services = new HashMap<String, ActorRef>();

    private static void bind(String name, ActorRef service) {
        services.put(name, service);
    }

    private static void unbind(String name) {
        services.remove(name);
    }

    private static ActorRef lookup(String name) {
        return services.get(name);
    }

    private static String[] list() {
        return services.keySet().toArray(new String[services.size()]);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.ServiceRegistryBind.class, msg -> {
                    bind(msg.name, msg.service);
                    getSender().tell(new Messages.NoOffer(msg.sequenceNumber + 1), getSelf());
                })
                .match(Messages.ServiceRegistryRemove.class, msg -> {
                    unbind(msg.name);
                    getSender().tell(new Messages.NoOffer(0), getSelf());
                })
                .match(Messages.RequestQuotations.class, msg -> {
                    clientInfo = msg.info;
                    quationSender = getSender();
                    int i = 0;
                    boolean shouldSend = true;
                    for (String name : list()) {
                        names.add(name);
                        if (name.startsWith("qs-") && shouldSend) {
                            lookup(name).tell(new Messages.RequestAQuotation(clientInfo, i), getSelf());
                            ;
                            shouldSend = false;
                        }
                        i++;

                    }
                })
                .match(Quotation.class, msg -> {
                    quationSender.tell(msg, getSelf());
                    if (names.size() > msg.sequenceNumber + 1) {
                        lookup((String) names.get(msg.sequenceNumber + 1)).tell(new Messages.RequestAQuotation(clientInfo, msg.sequenceNumber + 1), getSelf());
                    } else {
                        quationSender.tell(new Messages.NoOffer(0), getSelf());
                    }
                })
                .build();
    }
}
