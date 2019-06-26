package main;

import Models.Messages;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.ClientInfo;
import impl.Broker;
import quotation.AFQService;
import quotation.DDQService;
import quotation.GPQService;
import vetting.LocalVettingService;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("ContentSystem");
        final ActorRef broker = system.actorOf(Props.create(Broker.class));
//        serviceRegistry.tell(new Messages.NoOffer(0),null);
        Messages.ServiceRegistryBind[] services = new Messages.ServiceRegistryBind[4];

        services[0] = new Messages.ServiceRegistryBind("qs-GirlPowerService",system.actorOf(Props.create(GPQService.class), "GPQService"), 0);
        services[1] = new Messages.ServiceRegistryBind("qs-AuldFellasService", system.actorOf(Props.create( AFQService.class), "AFQService"), 1);
        services[2] = new Messages.ServiceRegistryBind("qs-DodgyDriversService", system.actorOf(Props.create( DDQService.class), "DDQService"), 2);
        services[3] = new Messages.ServiceRegistryBind("vs-VettingService", system.actorOf(Props.create( LocalVettingService.class), "LVQService"), 3);
        broker.tell(services[0],null);
        broker.tell(services[1],null);
        broker.tell(services[2],null);
        broker.tell(services[3],null);
        System.out.print("Wait.");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.println(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Result");
        broker.tell(new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"),null);
    }

    /**
     * Test client data
     */
}