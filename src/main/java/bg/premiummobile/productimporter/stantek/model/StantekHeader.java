package bg.premiummobile.productimporter.stantek.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Header", strict=false)
public class StantekHeader {

	@Element(name = "Company", required = false)
	private String company;
	@Element(name = "Count", required = false)
	private int count;
	@Element(name = "Created", required = false)
	private String dateCreated;
	@Element(name = "Currency", required = false)
	private String currency;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
