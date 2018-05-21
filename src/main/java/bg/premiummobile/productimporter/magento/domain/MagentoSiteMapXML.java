package bg.premiummobile.productimporter.magento.domain;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class MagentoSiteMapXML {

	@ElementList(inline=true, required = false)
	private List<MagentoSiteMapUrlXML> list;

	public List<MagentoSiteMapUrlXML> getList() {
		return list;
	}

	public void setList(List<MagentoSiteMapUrlXML> list) {
		this.list = list;
	}

}
