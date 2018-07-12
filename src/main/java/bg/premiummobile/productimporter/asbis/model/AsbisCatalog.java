package bg.premiummobile.productimporter.asbis.model;


import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ProductCatalog", strict = false)
public class AsbisCatalog {

	@ElementList(inline = true, required = false)
	private List<AsbisProduct> products;

	public List<AsbisProduct> getProducts() {
		return products;
	}

	public void setProducts(List<AsbisProduct> products) {
		this.products = products;
	}
	
}
