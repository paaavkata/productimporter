package bg.premiummobile.productimporter.magento.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyListAttribute extends Attribute{
	
	@JsonProperty("attribute_code")
	private String attributeCode;
	@JsonProperty("value")
	private List<String> value;
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public List<String> getValue() {
		if(value == null){
			return new ArrayList<String>();
		}
		return value;
	}
	public void setValue(Object value) {
		this.value = (List<String>) value;
	}
	
}
