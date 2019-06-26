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
import registry.ServiceRegistry;
import vetting.LocalVettingService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ServiceRegistryTest {
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
        final Props props = ServiceRegistry.createProps();
        subject = system.actorOf(props);
    }

    @Test()
    public void initTest() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.ServiceRegistryBind("AF",system.actorOf(Props.create(GPQService.class)),0), getRef());
                            expectNoMessage();
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
    public void ServiceRegistryLookupTest() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.ServiceRegistryRemove("qs-GirlPowerService"), getRef());
                            expectNoMessage();
                            return null;
                        });
            }
        };

    }
    @Test
    public void RespondVetFalse() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.RespondVetService(false), getRef());
                            expectNoMessage();
                            return null;
                        });
            }
        };

    }
    @Test()
    public void RespondVetTrue() {
        new TestKit(system) {
            {
                within(
                        Duration.ofSeconds(2),
                        () -> {
                            subject.tell(new Messages.RespondVetService(true), getRef());
                            expectNoMessage();
                            return null;
                        });
            }
        };

    }

}
