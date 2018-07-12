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

import bg.premiummobile.productimporter.asbis.model.AsbisCatalog;
import bg.premiummobile.productimporter.asbis.model.AsbisPriceAvail;
import bg.premiummobile.productimporter.asbis.model.AsbisProduct;
import bg.premiummobile.productimporter.asbis.model.AsbisProductPriceInfo;
import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;

@Component
public class AsbisClient {

private HttpClient client;
	
	private Serializer serializer;
	
	private ConfigurationReader reader;
	
	private HashMap<String, String> asbisProperties;
	
	@Autowired
	public AsbisClient(HttpClient client, ConfigurationReader reader){
		this.client = client;
		this.reader = reader;
		this.asbisProperties = reader.getAsbis();
		this.serializer = new Persister();
	}
	
	public List<AsbisProduct> downloadAsbisProducts() throws Exception{
		try(CloseableHttpResponse response = client.getClient().execute(generateGetRequest("products"))){
			AsbisCatalog catalog = serializer.read(AsbisCatalog.class, response.getEntity().getContent());
			return catalog.getProducts();
		}
	}
	
	public List<AsbisProductPriceInfo> downloadAsbisProductPrices() throws Exception{
		try(CloseableHttpResponse response = client.getClient().execute(generateGetRequest("prices"))){
			AsbisPriceAvail prices = serializer.read(AsbisPriceAvail.class, response.getEntity().getContent());
			return prices.getProducts();
		}
	}
	
	private HttpGet generateGetRequest(String type) throws Exception{
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(asbisProperties.get("domain"));
		builder.setPath(asbisProperties.get(type));
		builder.setParameter("USERNAME", asbisProperties.get("username")).setParameter("PASSWORD", asbisProperties.get("password"));

		HttpGet httpGet;
		httpGet = new HttpGet(builder.build());
		
		return httpGet;
	}
}
