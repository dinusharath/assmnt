package Models;

import akka.actor.ActorRef;
import core.Service;

public class ServiceRegistryBind {
    public String name;
    public ActorRef service;
    public int sequenceNumber;

    public ServiceRegistryBind(String name, ActorRef service, int sequenceNumber) {
        this.name = name;
        this.service = service;
        this.sequenceNumber = sequenceNumber;
    }
}
