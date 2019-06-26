package Models;

import akka.actor.ActorRef;
import core.ClientInfo;
import core.Quotation;
import quotation.QuotationService;

import java.util.List;

public class Messages {
    class Init {
        public QuotationService service;

        public Init(QuotationService service) {
            this.service = service;
        }
    }

    public static class RequestQuotations {
        public int sequenceNumber;
        public ClientInfo info;

        public RequestQuotations(int sequenceNumber, ClientInfo info) {
            this.sequenceNumber = sequenceNumber;
            this.info = info;
        }
    }

    public static class RequestAQuotation {
        public ClientInfo info;
        public int sequenceNumber;

        public RequestAQuotation(ClientInfo info, int sequenceNumber) {
            this.info = info;
            this.sequenceNumber = sequenceNumber;

        }
    }

    public static class Offer {
        public int sequenceNumber;
        public List<Quotation> quotation;

        public Offer(int sequenceNumber, List<Quotation> quotation) {
            this.sequenceNumber = sequenceNumber;
            this.quotation = quotation;
        }

        @Override
        public boolean equals(Object obj) {
            Offer offer = (Offer) obj;
            return (offer.sequenceNumber == sequenceNumber)
                    && offer.quotation.equals(quotation);
        }
    }

    public static class NoOffer {
        public int sequenceNumber;

        public NoOffer(int sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }
    }

    public static class RequestVetService {
        public ClientInfo info;

        public RequestVetService(ClientInfo info) {
            this.info = info;
        }
    }

    public static class RespondVetService {
        public boolean equal;

        public RespondVetService(boolean equal) {
            this.equal = equal;
        }
    }

    public static class RequestReference {
        public String prefix;

        public RequestReference(String prefix) {
            this.prefix = prefix;
        }
    }

    public static class RespondReference {
        public String reference;

        public RespondReference(String reference) {
            this.reference = reference;
        }
    }

    public static class RequestPrice {
        public double min;
        public int range;

        public RequestPrice(double min, int range) {
            this.min = min;
            this.range = range;
        }
    }

    public static class RespondPrice {
        public double price;

        public RespondPrice(double price) {
            this.price = price;
        }
    }

    public static class ServiceRegistryBind {
        public String name;
        public ActorRef service;
        public int sequenceNumber;

        public ServiceRegistryBind(String name, ActorRef service, int sequenceNumber) {
            this.name = name;
            this.service = service;
            this.sequenceNumber = sequenceNumber;
        }
    }

    public class ServiceRegistryRemove {
        public String name;

        public ServiceRegistryRemove(String name) {
            this.name = name;
        }
    }

}
