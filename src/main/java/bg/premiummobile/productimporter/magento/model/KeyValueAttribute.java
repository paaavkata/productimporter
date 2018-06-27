package bg.premiummobile.productimporter.magento.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyValueAttribute extends Attribute{

	@JsonProperty("attribute_code")
	private String attributeCode;
	@JsonProperty("value")
	private String value;
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public String getValue() {
		if(value == null){
			return "";
		}
		return value;
	}
	public void setValue(Object value) {
		this.value = String.valueOf(value);
	}
}
