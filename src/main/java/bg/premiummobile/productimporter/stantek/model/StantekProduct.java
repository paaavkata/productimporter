package bg.premiummobile.productimporter.stantek.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Product", strict=false)
public class StantekProduct {
	
	@Element(name="ID", required = false)
	private int id;
	@Element(name="Group", required = false)
	private String group;
	@Element(name="Title", required = false)
	private String name;
	@Element(name="Available", required = false)
	private String availability;
	@Element(name="RetailPriceWithVAT", required = false)
	private String retailPrice;
	@Element(name="YourPriceWithoutVAT", required = false)
	private String myPrice;
	@Element(name="Image", required = false)
	private String image;
	@Element(name="Description", required = false)
	private String description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getMyPrice() {
		return myPrice;
	}
	public void setMyPrice(String myPrice) {
		this.myPrice = myPrice;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
