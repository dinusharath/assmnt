package vetting;

import Models.ClientInfo;
import quotation.Service;

/**
 * Interface defining the expected behavour of a vetting service.
 * 
 * @author Rem
 *
 */
public interface VettingService extends Service {
	public boolean vetClient(ClientInfo info);
}
