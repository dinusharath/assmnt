package quotation;

import core.ClientInfo;
import core.Quotation;
import core.Service;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */
public interface QuotationService extends Service {
	public Quotation generateQuotation(ClientInfo info);
}
