package bg.premiummobile.productimporter.solytron.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root
public class PriceEndUser {
	@Attribute(required = false)
	public String currency;    

	public String text;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Text
	public String getText() {
		return text;
	}
	@Text
	public void setText(String text) {
		this.text = text;
	}
	
	
}
