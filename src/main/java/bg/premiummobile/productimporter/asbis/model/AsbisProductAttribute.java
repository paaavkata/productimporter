package bg.premiummobile.productimporter.asbis.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="element", strict=false)
public class AsbisProductAttribute {
	
	@Attribute(name="Name", required=false)
	private String name;
	
	@Attribute(name="Value", required=false)
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
