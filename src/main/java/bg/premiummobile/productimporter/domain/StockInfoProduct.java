package bg.premiummobile.productimporter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import bg.premiummobile.productimporter.magento.domain.MagentoProduct;

public class StockInfoProduct implements MagentoProduct {
	
	@JsonProperty("sku")
	private String sku;
	@JsonProperty("price")
	private double price;
	@JsonProperty("status")
	private int status;
	@JsonProperty("visibility")
	private int visibility;
	@JsonProperty("extension_attributes")
	private ExtensionAttribute attribute;
	
	public StockInfoProduct(){
		
	}
	//VISIBILITY CONSTANTS
	//1 - Not Visible
	//2 - Only catalog
	//3 - Only searchable
	//4 - Both 2 & 3
	public StockInfoProduct(String sku, double price, int status, int visibility, int qty, boolean inStock){
		
		this.attribute = new ExtensionAttribute(qty, inStock);
		this.sku = sku;
		this.price = price;
		this.status = status;
		this.visibility = visibility;
		
	}
	
	private class ExtensionAttribute{
		
		@JsonProperty("stock_item")
		private StockItem stockItem;
		
		public ExtensionAttribute(){
			
		}
		
		private ExtensionAttribute(int qty, boolean inStock){
			this.stockItem = new StockItem(qty, inStock);
		}
		
		private class StockItem{
			
			@JsonProperty("qty")
			private int qty;
			
			@JsonProperty("is_in_stock")
			private boolean inStock;
			
			public StockItem(){
				
			}
			
			private StockItem(int qty, boolean inStock){
				this.qty = qty;
				this.inStock = inStock;
			}
		}
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getVisibility() {
		return visibility;
	}
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	public ExtensionAttribute getAttribute() {
		return attribute;
	}
	public void setAttribute(ExtensionAttribute attribute) {
		this.attribute = attribute;
	}
	@JsonIgnore
	public int getQty() {
		return this.attribute.stockItem.qty;
	}
	@JsonIgnore
	public void setQty(int qty) {
		this.attribute.stockItem.qty = qty;
	}
	@JsonIgnore
	public boolean getInStock() {
		return this.attribute.stockItem.inStock;
	}
	@JsonIgnore
	public void setInStock(boolean inStock) {
		this.attribute.stockItem.inStock = inStock;
	}
	
}
