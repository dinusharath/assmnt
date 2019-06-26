package main;

import Models.Messages;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import core.ClientInfo;
import quotation.AFQService;
import quotation.DDQService;
import quotation.GPQService;
import vetting.LocalVettingService;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("ContentSystem");
        final ActorRef serviceRegistry = system.actorOf(Props.create(Brocker.class));
//        serviceRegistry.tell(new Messages.NoOffer(0),null);
        Messages.ServiceRegistryBind[] services = new Messages.ServiceRegistryBind[4];

        services[0] = new Messages.ServiceRegistryBind("qs-GirlPowerService",system.actorOf(Props.create(GPQService.class), "GPQService"), 0);
        services[1] = new Messages.ServiceRegistryBind("qs-AuldFellasService", system.actorOf(Props.create( AFQService.class), "AFQService"), 1);
        services[2] = new Messages.ServiceRegistryBind("qs-DodgyDriversService", system.actorOf(Props.create( DDQService.class), "DDQService"), 2);
        services[3] = new Messages.ServiceRegistryBind("vs-VettingService", system.actorOf(Props.create( LocalVettingService.class), "LVQService"), 3);
        serviceRegistry.tell(services[0],null);
        serviceRegistry.tell(services[1],null);
        serviceRegistry.tell(services[2],null);
        serviceRegistry.tell(services[3],null);
        System.out.println("start");

        TimeUnit.SECONDS.sleep(5);
        System.out.println("end");
        serviceRegistry.tell(new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1",0),null);
    }

    /**
     * Test client data
     */
}
