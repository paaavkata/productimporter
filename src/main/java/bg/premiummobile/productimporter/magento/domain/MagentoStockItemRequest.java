package bg.premiummobile.productimporter.magento.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagentoStockItemRequest {
	
	@JsonProperty("qty")
	private Integer qty;
	@JsonProperty("is_in_stock")
	private boolean stock;
	
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public boolean isStock() {
		return stock;
	}
	
	public void setStock(boolean stock) {
		this.stock = stock;
	}
	
	
}
