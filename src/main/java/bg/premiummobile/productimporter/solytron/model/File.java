package bg.premiummobile.productimporter.solytron.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class File {
	
	@Attribute
	private String type;
	
	@Attribute
	private String description;
	
	private String text;

	@Text
	public String getType() {
		return type;
	}
	
	@Text
	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
