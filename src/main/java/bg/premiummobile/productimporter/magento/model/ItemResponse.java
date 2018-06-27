package bg.premiummobile.productimporter.magento.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemResponse {
	
	@JsonProperty("items")
	private ArrayList<MagentoAttribute> attributes;

	public ArrayList<MagentoAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<MagentoAttribute> attributes) {
		this.attributes = attributes;
	}
	
}
