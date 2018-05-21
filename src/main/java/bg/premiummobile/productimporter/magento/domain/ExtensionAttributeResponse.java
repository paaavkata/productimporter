package bg.premiummobile.productimporter.magento.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtensionAttributeResponse {

	@JsonProperty("website_ids")
	private List<Integer> websiteIds;
	@JsonProperty("category_links")
	private List<CategoryLink> categoryLinks;
	@JsonProperty("stock_item")
	private MagentoStockItemFull stockItem;
	@JsonProperty("configurable_product_options")
	private List<ConfigurableProductOption> configurableProductOption;
	@JsonProperty("configurable_product_links")
	private List<Integer> configurableProductLinks;
	
	public List<Integer> getWebsiteIds() {
		return websiteIds;
	}
	public void setWebsiteIds(List<Integer> websiteIds) {
		this.websiteIds = websiteIds;
	}
	public List<CategoryLink> getCategoryLinks() {
		return categoryLinks;
	}
	public void setCategoryLinks(List<CategoryLink> categoryLinks) {
		this.categoryLinks = categoryLinks;
	}
	public MagentoStockItemFull getStockItem() {
		return stockItem;
	}
	public void setStockItem(MagentoStockItemFull stockItem) {
		this.stockItem = stockItem;
	}
	public List<ConfigurableProductOption> getConfigurableProductOption() {
		return configurableProductOption;
	}
	public void setConfigurableProductOption(List<ConfigurableProductOption> configurableProductOption) {
		this.configurableProductOption = configurableProductOption;
	}
	public List<Integer> getConfigurableProductLinks() {
		return configurableProductLinks;
	}
	public void setConfigurableProductLinks(List<Integer> configurableProductLinks) {
		this.configurableProductLinks = configurableProductLinks;
	}
	
}
