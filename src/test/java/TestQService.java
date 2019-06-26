import Models.ClientInfo;
import Models.Quotation;
import core.AbstractQuotationService;
import quotation.QuotationService;

/**
 * Implementation of the AuldFellas insurance quotation service.
 * 
 * @author Rem
 *
 */
public class TestQService extends AbstractQuotationService implements QuotationService {
	@Override
	public Quotation generateQuotation(ClientInfo info) {
		return new Quotation("TEST", info, 100,0);
	}

}
