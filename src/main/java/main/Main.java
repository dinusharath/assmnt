package main;

import Models.Messages;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import Models.ClientInfo;
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
        ClientInfo clientInfo =new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1");
        broker.tell(new Messages.Init(new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"),0),null);
    }

    /**
     * Test client data
     */
}
