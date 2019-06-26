package quotation;

import Models.ClientInfo;
import Models.Quotation;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */
public interface QuotationService extends Service {
	public Quotation generateQuotation(ClientInfo info);
}
