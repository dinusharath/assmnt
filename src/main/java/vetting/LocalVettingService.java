package vetting;

import Models.Messages;
import akka.actor.AbstractActor;
import core.ClientInfo;

import java.util.HashMap;
import java.util.Map;

public class LocalVettingService extends AbstractActor implements VettingService {
	Map<String, Integer> pointsDB = new HashMap<String, Integer>();

	{
		pointsDB.put("PQR254/1", 0);
		pointsDB.put("ABC123/4", 2);
		pointsDB.put("XYZ567/9", 5);

	}

	@Override
	public boolean vetClient(ClientInfo info) {
		Integer value = pointsDB.get(info.licenseNumber);
		return value != null && (value == info.points);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(Messages.RequestVetService.class,msg->{
					Integer value = pointsDB.get(msg.info.licenseNumber);
					getSender().tell(new Messages.RespondVetService(value != null && (value == msg.info.points)),getSelf());
				})
				.build();
	}
}
