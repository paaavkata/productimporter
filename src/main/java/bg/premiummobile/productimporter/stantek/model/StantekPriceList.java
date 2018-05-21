package bg.premiummobile.productimporter.stantek.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Pricelist", strict=false)
public class StantekPriceList {
	
	@Element(name="ID", required = false)
	private StantekHeader header;
	@ElementList(name="Products", required = false)
	private List<StantekProduct> products;
	
	public StantekHeader getHeader() {
		return header;
	}
	public void setHeader(StantekHeader header) {
		this.header = header;
	}
	public List<StantekProduct> getProducts() {
		return products;
	}
	public void setProducts(List<StantekProduct> products) {
		this.products = products;
	}
	
	
}
