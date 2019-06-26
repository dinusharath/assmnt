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
    public ClientInfo client = new ClientInfo();

    public void setData(String name, char gender, int age, int points, int noClaims, String licenseNumber) {
        this.client.name = name;
        this.client.sex = gender;
        this.client.age = age;
        this.client.points = points;
        this.client.noClaims = noClaims;
        this.client.licenseNumber = licenseNumber;

        System.out.println(client);
    }

    public ClientInfo getData(){
        return client;
    }
    public static void main(String[] args) throws InterruptedException {
        Main mainObject = new Main();
        ClientInfo clientInfo = mainObject.getData();
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
//        ClientInfo clientInfo =new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1");
//        broker.tell(new Messages.Init(new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"),0),null);
        broker.tell(new ClientInfo(clientInfo.name, clientInfo.sex, clientInfo.age, clientInfo.points, clientInfo.noClaims, clientInfo.licenseNumber),null);

    }

    /**
     * Test client data
     */
}
