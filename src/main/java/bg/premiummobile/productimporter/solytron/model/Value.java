package bg.premiummobile.productimporter.solytron.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root		
public class Value {
	
	@Attribute(required = false)
	private String valueId;
	
	private String text;

	public String getValueId() {
		return valueId;
	}

	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	
	@Text(required = false)
	public String getText() {
		return text;
	}
	@Text(required = false)
	public void setText(String text) {
		this.text = text;
	}
	
}
