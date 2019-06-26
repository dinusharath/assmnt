//import Models.Messages;
//import Models.Quotation;
//import akka.QuotationActor;
//import akka.actor.ActorRef;
//import akka.actor.ActorSystem;
//import akka.actor.Props;
//import akka.testkit.javadsl.TestKit;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.time.Duration;
//
//public class QuotationActorTest {
//    private static ActorSystem system;
//    private ActorRef subject;
//
//    @BeforeClass
//    public static void setup() {
//        system = ActorSystem.create();
//    }
//
//    @AfterClass
//    public static void teardown() {
//        TestKit.shutdownActorSystem(system);
//        system = null;
//    }
//
//    @Before
//    public void setupSubject() {
//        final Props props = QuotationActor.createProps();
//        subject = system.actorOf(props);
//    }
//
//    @Test
//    public void initTest() {
//        new TestKit(system) {
//            {
//                within(
//                        Duration.ofSeconds(2),
//                        () -> {
//                            subject.tell(new Messages.Init(), getRef());
//                            expectNoMessage();
//                            return null;
//                        });
//            }
//        };
////        probe.expectMsg(Duration.ofSeconds(1), new Quotation("TEST", info, 100));
//    }
//
//    @Test
//    public void getOffer() {
//        new TestKit(system) {
//            {
//                within(
//                        Duration.ofSeconds(2),
//                        () -> {
//                            subject.tell(new Messages.Init(new TestQService()), getRef());
//                            subject.tell(new Messages.RequestQuotation(1, Main.clients[0]), getRef());
//                            expectMsg(Duration.ofSeconds(1), new Messages.Offer(1, new Quotation("TEST", Main.clients[0], 100)));
//                            return null;
//                        });
//            }
//        };
//    }
//
//
//
//}
