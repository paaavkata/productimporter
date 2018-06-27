package bg.premiummobile.productimporter.replacetech.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

	@JsonProperty("ProductName")
	private String name;
	@JsonProperty("ItemVariantId")
	private int itemVariantId;
	@JsonProperty("ItemGroupId")
	private int itemGroupId;
	@JsonProperty("ItemGroupName")
	private String itemGroupName;
	@JsonProperty("DimensionGroupId")
	private int dimensionGroupId;
	@JsonProperty("DimensionGroupName")
	private String dimensionGroupName;
	@JsonProperty("Quantity")
	private int qty;
	@JsonProperty("Price")
	private int price;
	@JsonProperty("CurrencyIsoCode")
	private String currency;
	@JsonProperty("Dimension")
	private List<ReplaceTechAttribute> attributes;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getItemVariantId() {
		return itemVariantId;
	}
	public void setItemVariantId(int itemVariantId) {
		this.itemVariantId = itemVariantId;
	}
	public int getItemGroupId() {
		return itemGroupId;
	}
	public void setItemGroupId(int itemGroupId) {
		this.itemGroupId = itemGroupId;
	}
	public String getItemGroupName() {
		return itemGroupName;
	}
	public void setItemGroupName(String itemGroupName) {
		this.itemGroupName = itemGroupName;
	}
	public int getDimensionGroupId() {
		return dimensionGroupId;
	}
	public void setDimensionGroupId(int dimensionGroupId) {
		this.dimensionGroupId = dimensionGroupId;
	}
	public String getDimensionGroupName() {
		return dimensionGroupName;
	}
	public void setDimensionGroupName(String dimensionGroupName) {
		this.dimensionGroupName = dimensionGroupName;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public List<ReplaceTechAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<ReplaceTechAttribute> attributes) {
		this.attributes = attributes;
	}
	
	
	
}
