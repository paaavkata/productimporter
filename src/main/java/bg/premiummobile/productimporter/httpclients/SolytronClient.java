package bg.premiummobile.productimporter.httpclients;

import java.util.HashMap;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.solytron.model.ProductCatalog;
import bg.premiummobile.productimporter.solytron.model.ProductSet;
import bg.premiummobile.productimporter.solytron.model.SolytronProduct;

@Component
public class SolytronClient {

	private HttpClient client;
	
	private Serializer serializer;
	
	private ConfigurationReader reader;
	
	private HashMap<String, String> solytronProperties;
	
	@Autowired
	public SolytronClient(HttpClient client, ConfigurationReader reader){
		this.client = client;
		this.reader = reader;
		this.solytronProperties = reader.getSolytron();
		this.serializer = new Persister();
	}
	public SolytronProduct downloadProduct(String id) throws Exception{
		try(CloseableHttpResponse response = client.getClient().execute(generateGetRequest(id, "product"))){
			SolytronProduct productNew = serializer.read(SolytronProduct.class, response.getEntity().getContent());
			return productNew;
		}
	}
	
	public List<SolytronProduct> downloadCategory(String code) throws Exception{
		ProductSet productSet = null;
		try(CloseableHttpResponse response = client.getClient().execute(generateGetRequest(code, "category"))){
			productSet = serializer.read(ProductSet.class, response.getEntity().getContent());
		}
		if(productSet != null){
			return productSet.getProducts();
		}
		else{
			return null;
		}
	}
	
	public ProductCatalog downloadCategoriesList() throws Exception{
		try(CloseableHttpResponse response = client.getClient().execute(generateGetRequest(null, "categoryList"))){
			ProductCatalog productSet = serializer.read(ProductCatalog.class, response.getEntity().getContent());
			return productSet;
		}
	}
	
	private HttpGet generateGetRequest(String code, String type) throws Exception{
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(solytronProperties.get("domain"));
		
		if("product".equals(type)){
			builder.setPath(solytronProperties.get("url.product")).setParameter("productId", code);
		}
		
		else if("category".equals(type)){
			builder.setPath(solytronProperties.get("url.category")).setParameter("propertyId", code);
		}
		
		else if("categroyList".equals(type)){
			builder.setPath(solytronProperties.get("url.categoryList"));
		}
		
		HttpGet httpGet;
		builder.setParameter("j_u", solytronProperties.get("username")).setParameter("j_p", solytronProperties.get("password"));
		httpGet = new HttpGet(builder.build());
		return httpGet;
		
	}
	
}
