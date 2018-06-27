package bg.premiummobile.productimporter.replacetech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReplaceTechAttribute {

	@JsonProperty("Key")
	private String key;
	@JsonProperty("Value")
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
