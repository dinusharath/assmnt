import core.AbstractQuotationService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class calculation {
    @Test
    public final void generateReference() {
        assertEquals("AF001000", AbstractQuotationService.generateReference("AF"));
    }

    @Test
    public final void generatePrice() {
        assertEquals(105, AbstractQuotationService.generatePrice(100, 10), 10);
    }
}
