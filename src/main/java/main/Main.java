package main;

import Models.Messages;
import Models.ServiceRegistryBind;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.ClientInfo;
import core.Quotation;
import core.Service;
import impl.LocalBrokerService;
import quotation.AFQService;
import quotation.DDQService;
import quotation.GPQService;
import registry.ServiceRegistry;
import scala.sys.Prop;
import vetting.LocalVettingService;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("ContentSystem");
        final ActorRef serviceRegistry = system.actorOf(Props.create(Brocker.class));
        serviceRegistry.tell(new Messages.NoOffer(0),null);

    }

    /**
     * Test client data
     */
}
