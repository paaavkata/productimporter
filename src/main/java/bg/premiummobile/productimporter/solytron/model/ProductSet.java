package bg.premiummobile.productimporter.solytron.model;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ProductSet {
	
	@Attribute
	private String version;
	
	@ElementList(inline = true, required = false)
	private List<SolytronProduct> products;

	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public List<SolytronProduct> getProducts() {
		return products;
	}

	public void setProducts(List<SolytronProduct> products) {
		this.products = products;
	}

}
