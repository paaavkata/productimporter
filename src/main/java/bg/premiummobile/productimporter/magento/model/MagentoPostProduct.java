package bg.premiummobile.productimporter.magento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagentoPostProduct {
	
	@JsonProperty("product")
	private MagentoProduct magentoProduct;

	public MagentoProduct getMagentoProduct() {
		return magentoProduct;
	}

	public void setMagentoProduct(MagentoProduct magentoProduct) {
		this.magentoProduct = magentoProduct;
	}
	
}
