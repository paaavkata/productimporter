package bg.premiummobile.productimporter.magento.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtensionAttributeRequest {
	
	@JsonProperty("stock_item")
	private MagentoStockItemRequest item;

	public MagentoStockItemRequest getItem() {
		return item;
	}

	public void setItem(MagentoStockItemRequest item) {
		this.item = item;
	}
}
