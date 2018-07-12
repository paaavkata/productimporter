package bg.premiummobile.productimporter.asbis.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Product", strict=false)
public class AsbisProduct {
	
	@Element(name="ProductCode", required = false)
	private String asbisSku;
	
	@Element(name="Vendor", required = false)
	private String vendor;
	
	@Element(name="ProductType", required = false)
	private String type;
	
	@Element(name="ProductCategory", required = false)
	private String category;
	
	@Element(name="ProductDescription", required = false)
	private String name;
	
	@Element(name="ProductCard", required = false)
	private String card;
	
	@Element(name="Image", required = false)
	private String image;
	
	private String available;
	
	private String myPrice;
	
	private String retailPrice;
	
	private String ean;
	
	@ElementList(name="AttrList", required = false)
	private List<AsbisProductAttribute> attributes;
	
	@ElementList(name="Images", required = false)
	private List<String> images;

	public String getAsbisSku() {
		return asbisSku;
	}

	public void setAsbisSku(String asbisSku) {
		this.asbisSku = asbisSku;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<AsbisProductAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AsbisProductAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public String getMyPrice() {
		return myPrice;
	}

	public void setMyPrice(String myPrice) {
		this.myPrice = myPrice;
	}

	public String getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}
	
}
