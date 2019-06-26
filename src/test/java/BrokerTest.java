import Models.ClientInfo;
import Models.Messages;
import Models.Quotation;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import impl.Broker;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import quotation.AFQService;
import quotation.DDQService;
import quotation.GPQService;
import vetting.LocalVettingService;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BrokerTest {
    private static ActorSystem system;
    private ActorRef subject;
    ClientInfo clientInfo = new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1");

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Before
    public void setupSubject() {
        final Props props = Broker.createProps();
        subject = system.actorOf(props);
    }

    @Test(expected = java.lang.AssertionError.class)
    public void initTest() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.Init(new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"),0), getRef());
                            expectMsg(Duration.ofSeconds(1), new Messages.Offer(0, new ArrayList<Quotation>()));
                            return null;
                        });
            }
        };

    }

    @Test
    public void ServiceRegistryBindTest() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.ServiceRegistryBind("qs-GirlPowerService", system.actorOf(Props.create(GPQService.class)), 0), getRef());
                            expectNoMessage();
                            return null;
                        });
            }
        };

    }

    @Test
    public void QuotationTest() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Quotation("AF0100", new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"), 100, 0), getRef());
                            expectMsg(Duration.ofSeconds(10), new Quotation("AF0100", new ClientInfo("Niki Collier", ClientInfo.MALE, 41, 0, 7, "PQR254/1"), 100, 0));
                            return null;
                        });
            }
        };

    }

    @Test
    public void OfferTest() {
        Messages.ServiceRegistryBind[] services = new Messages.ServiceRegistryBind[4];
        services[0] = new Messages.ServiceRegistryBind("qs-GirlPowerService",system.actorOf(Props.create(GPQService.class), "GPQService"), 0);
        services[1] = new Messages.ServiceRegistryBind("qs-AuldFellasService", system.actorOf(Props.create( AFQService.class), "AFQService"), 1);
        services[2] = new Messages.ServiceRegistryBind("qs-DodgyDriversService", system.actorOf(Props.create( DDQService.class), "DDQService"), 2);
        services[3] = new Messages.ServiceRegistryBind("vs-VettingService", system.actorOf(Props.create( LocalVettingService.class), "LVQService"), 3);
        subject.tell(services[0],null);
        subject.tell(services[1],null);
        subject.tell(services[2],null);
        subject.tell(services[3],null);
        List<Quotation> offer = new ArrayList<Quotation>();
        offer.add(new Quotation("AF0100", clientInfo, 100, 0));
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.Offer(0, offer), getRef());
                            expectMsg(Duration.ofSeconds(3), offer);
                            return null;
                        });
            }
        };

    }

}
