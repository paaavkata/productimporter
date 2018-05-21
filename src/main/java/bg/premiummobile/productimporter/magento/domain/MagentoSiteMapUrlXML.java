package bg.premiummobile.productimporter.magento.domain;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false, name = "url")
public class MagentoSiteMapUrlXML {

	@Element(name = "loc", required = false)
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
