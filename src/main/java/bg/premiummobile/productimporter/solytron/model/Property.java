package bg.premiummobile.productimporter.solytron.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false)			
public class Property {
	
	@Attribute(required = false)
    private int propertyId;
	
	@Attribute(required = false)
    private String type;
	
	@Attribute(required = false)
    private String name;

    @ElementList(inline=true, required = false)
    private List<Value> value;

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
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

	public List<Value> getValue() {
		return value;
	}

	public void setValue(List<Value> value) {
		this.value = value;
	}

}
