package registry;

import Models.Messages;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import Models.ClientInfo;
import Models.Quotation;
import akka.actor.Props;
import impl.Broker;
import vetting.LocalVettingService;

import java.util.*;

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
    final ActorRef localVetService;
    int sequenceNumber;
    List names;
    List<Quotation> quotations;

    public ServiceRegistry() {
        system = ActorSystem.create("ContentSystem");
        names = new ArrayList();
        quotations = new LinkedList<Quotation>();
        localVetService = system.actorOf(Props.create(LocalVettingService.class), "localVetService");
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

    public static Props createProps() {
        return Props.create(ServiceRegistry.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.ServiceRegistryBind.class, msg -> {
                    bind(msg.name, msg.service);
                })
                .match(Messages.ServiceRegistryRemove.class, msg -> {
                    unbind(msg.name);
                })
                .match(Messages.RequestQuotations.class, msg -> {
                    clientInfo = msg.info;
                    quationSender = getSender();
                    sequenceNumber = msg.sequenceNumber;
                    quotations.clear();
                    localVetService.tell(new Messages.RequestVetService(msg.info), getSelf());
                })
                .match(Quotation.class, msg -> {
                    quotations.add(msg);
                    if (names.size() > msg.sequenceNumber + 1) {
                        lookup((String) names.get(msg.sequenceNumber + 1)).tell(new Messages.RequestAQuotation(clientInfo, msg.sequenceNumber + 1), getSelf());
                    } else {
                        quationSender.tell(new Messages.Offer(sequenceNumber, quotations), getSelf());
                    }
                })
                .match(Messages.RespondVetService.class, msg -> {
                    if (msg.equal) {
                        int i = 0;
                        boolean shouldSend = true;
                        for (String name : list()) {
                            names.add(name);
                            if (name.startsWith("qs-") && shouldSend) {
                                lookup(name).tell(new Messages.RequestAQuotation(clientInfo, i), getSelf());
                                shouldSend = false;
                            }
                            i++;
                        }
                    } else {
                        quationSender.tell(new Messages.NoOffer(sequenceNumber), getSelf());
                    }
                })
                .build();
    }
}
