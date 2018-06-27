package bg.premiummobile.productimporter.magento.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurableProductOption {

	private int id;
	@JsonProperty("attribute_id")
	private int attributeId;
	private String label;
	private int position;
	private List<Value> values;
	@JsonProperty("product_id")
	private int productId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
}
