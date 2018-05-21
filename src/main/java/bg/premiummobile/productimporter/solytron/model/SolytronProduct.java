package bg.premiummobile.productimporter.solytron.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="product", strict=false)
public class SolytronProduct {
	
	@Attribute(name="productId", required = false)
	private String productId;
	
	@Attribute(name="groupId", required = false)
	private String groupId;
	
	@Attribute(name="codeId", required = false)
	private String codeId;
	
	@Attribute(name="ean", required = false)
	private String ean;

	@Element(name="name", required = false)
	private String name;
	
	@Element(name="vendor", required = false)
	private String vendor;
	
	@Element(name="warrantyQty", required = false)
	private String warrantyQty;
	
	@Element(name="warrantyUnit", required = false)
	private String warrantyUnit;
	
	@Element(name="price", required = false)
	private Price price;
	
	@Element(name="priceEndUser", required = false)
	private PriceEndUser priceEndUser;
	
	@Element(name="stockInfoValue", required = false)
	private String stockInfoValue;
	
	@Element(name="stockInfoData", required = false)
	private String stockInfoData;
	
	@ElementList(inline = true, required = false)
	private List<PropertyGroup> properties;

	@Element(name="file", required = false)
	private File file;
	
	@ElementList(inline = true, required = false)
	private List<Image> images;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCodeId() {
		return codeId;
	}

	public List<PropertyGroup> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyGroup> properties) {
		this.properties = properties;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getWarrantyQty() {
		return warrantyQty;
	}

	public void setWarrantyQty(String warrantyQty) {
		this.warrantyQty = warrantyQty;
	}

	public String getWarrantyUnit() {
		return warrantyUnit;
	}

	public void setWarrantyUnit(String warrantyUnit) {
		this.warrantyUnit = warrantyUnit;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public PriceEndUser getPriceEndUser() {
		return priceEndUser;
	}

	public void setPriceEndUser(PriceEndUser priceEndUser) {
		this.priceEndUser = priceEndUser;
	}

	public String getStockInfoValue() {
		return stockInfoValue;
	}

	public void setStockInfoValue(String stockInfoValue) {
		this.stockInfoValue = stockInfoValue;
	}

	public String getStockInfoData() {
		return stockInfoData;
	}

	public void setStockInfoData(String stockInfoData) {
		this.stockInfoData = stockInfoData;
	}
	
	
}
