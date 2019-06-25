package registry;

import Models.Messages;
import Models.ServiceRegistryBind;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import core.ClientInfo;
import core.Quotation;

import java.util.HashMap;
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
int count=0;
    public ServiceRegistry() {
        system = ActorSystem.create("ContentSystem");
    }

    private static Map<String, ActorRef> services = new HashMap<String, ActorRef>();

    private static void bind(String name, ActorRef service) {
        services.put(name, service);
    }

    private static void unbind(String name) {
        services.remove(name);
    }

    private static void lookup(String name, ClientInfo clientInfo, ActorRef self,int sequenceNumber) {
        services.get(name).tell(new Messages.RequestAQuotation(clientInfo,sequenceNumber), self);
    }

    private static String[] list() {
        return services.keySet().toArray(new String[services.size()]);
    }

String[] names =new  String[list().length];
    ClientInfo clientInfo;
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ServiceRegistryBind.class, msg -> {
                    bind(msg.name, msg.service);
                    getSender().tell(new Messages.NoOffer(msg.sequenceNumber + 1), getSelf());
                })
                .match(Messages.RequestQuotations.class, msg -> {
                    clientInfo = msg.info;
                    quationSender = getSender();
                    int i = 0;
                    boolean shouldSend= true;
                    for (String name : list()) {
                        names[i]= name;
                        if (name.startsWith("qs-")&& shouldSend) {
                            lookup(name, msg.info, getSelf(),i);
                            shouldSend=false;
                        }
                        i++;

                    }
                })
                .match(Quotation.class, msg -> {
                    quationSender.tell(msg, getSelf());
                    if(names.length > msg.sequenceNumber+1) {
                        lookup(names[msg.sequenceNumber + 1], clientInfo, getSelf(), msg.sequenceNumber + 1);
                    }
                    else {
                        quationSender.tell(new Messages.NoOffer(0), getSelf());
                    }
                })
                .build();
    }
}
