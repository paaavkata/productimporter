package bg.premiummobile.productimporter.asbis.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="PRICE", strict=false)
public class AsbisProductPriceInfo {

	@Element(name="WIC", required = false)
	private String asbisSku;
	
	@Element(name="DESCRIPTION", required = false)
	private String description;
	
	@Element(name="VENDOR_NAME", required = false)
	private String vendorName;
	
	@Element(name="GROUP_NAME", required = false)
	private String groupName;
	
	@Element(name="VPF_NAME", required = false)
	private String vpfName;
	
	@Element(name="CURRENCY_CODE", required = false)
	private String currency;
	
	@Element(name="AVAIL", required = false)
	private String availability;
	
	@Element(name="RETAIL_PRICE", required = false)
	private String retailPrice;
	
	@Element(name="MY_PRICE", required = false)
	private String myPrice;
	
	@Element(name="WARRANTYTERM", required = false)
	private String warranty;
	
	@Element(name="GROUP_ID", required = false)
	private String groupId;
	
	@Element(name="VENDOR_ID", required = false)
	private String vendorId;
	
	@Element(name="SMALL_IMAGE", required = false)
	private String smallImage;
	
	@Element(name="PRODUCT_CARD", required = false)
	private String productCard;
	
	@Element(name="EAN", required = false)
	private String ean;

	public String getAsbisSku() {
		return asbisSku;
	}

	public void setAsbisSku(String asbisSku) {
		this.asbisSku = asbisSku;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getVpfName() {
		return vpfName;
	}

	public void setVpfName(String vpfName) {
		this.vpfName = vpfName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getProductCard() {
		return productCard;
	}

	public void setProductCard(String productCard) {
		this.productCard = productCard;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}
	
}
