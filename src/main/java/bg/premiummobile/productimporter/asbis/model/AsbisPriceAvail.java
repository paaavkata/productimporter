package bg.premiummobile.productimporter.asbis.model;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="CONTENT", strict = false)
public class AsbisPriceAvail {
	
	@ElementList(name="PRICES", required = false)
	private List<AsbisProductPriceInfo> products;

	@Element(name="COMP_CODE", required = false)
	private String code;
	
	@Element(name="LANG", required = false)
	private String lang;
	
	@Element(name="COMP_CODE_BUYER", required = false)
	private String codeBuyer;
	
	@Element(name="SEARCH_CODE", required = false)
	private String codeSearch;
	
	@Element(name="MANUFACTURER_NAME", required = false)
	private String manufacturer;
	
	@Element(name="TYPE_NAME", required = false)
	private String type;
	
	@Element(name="SUPPLY_TYPE", required = false)
	private String supply;
	
	public List<AsbisProductPriceInfo> getProducts() {
		return products;
	}

	public void setProducts(List<AsbisProductPriceInfo> products) {
		this.products = products;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCodeBuyer() {
		return codeBuyer;
	}

	public void setCodeBuyer(String codeBuyer) {
		this.codeBuyer = codeBuyer;
	}

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSupply() {
		return supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}
}
