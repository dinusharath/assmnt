package Models;

/**
 * Interface to define the data to be stored in Quotation objects.
 * 
 * @author Rem
 *
 */
public class Quotation {
	public Quotation(String reference, ClientInfo clientInfo, double price,int sequenceNumber) {
		this.reference = reference;
		this.clientInfo = clientInfo;
		this.price = price;
		this.sequenceNumber =sequenceNumber;
		
	}
	public String reference;
	public ClientInfo clientInfo;
	public double price;
	public int sequenceNumber;
}
