package bg.premiummobile.productimporter.magento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaGalleryContent {

	@JsonProperty("base64_encoded_data")
	private String data;
	@JsonProperty("type")
	private String type;
	@JsonProperty("name")
	private String name;
	public String getData() {
		return data;
	}
	public void setData(String string) {
		this.data = string;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
