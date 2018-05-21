package bg.premiummobile.productimporter.solytron.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class PropertyGroup {
	
	@Attribute(required = false)
	private String propertyGroupId;
	
	@Element(name="propertyGroupName", required = false)
	private String productGroupName;
	
	@ElementList(inline=true, required = false)
	private List<Property> list;

	public String getPropertyGroupId() {
		return propertyGroupId;
	}

	public void setPropertyGroupId(String propertyGroupId) {
		this.propertyGroupId = propertyGroupId;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public List<Property> getList() {
		return list;
	}

	public void setList(List<Property> list) {
		this.list = list;
	}
	
	
}
