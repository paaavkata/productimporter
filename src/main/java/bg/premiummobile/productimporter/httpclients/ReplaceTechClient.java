package bg.premiummobile.productimporter.httpclients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.replacetech.model.Product;

@Component
public class ReplaceTechClient {
	
	private HttpClient client;
	
	private ConfigurationReader reader;
	
	private ObjectMapper om;
	
	private HashMap<String, String> replaceTech;
	
	@Autowired
	public ReplaceTechClient(ConfigurationReader reader, ObjectMapper om, HttpClient client){
		this.om = om;
		this.reader = reader;
		this.client = client;
		this.replaceTech = reader.getReplaceTech();
	}
	
	public List<Product> getCategory(String slug, int dimensionGroupId, int itemGroupId, boolean vat) throws Exception{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(replaceTech.get("host"));
		builder.setPath(replaceTech.get("productUri") + '/' + slug + '/' + replaceTech.get("pricelistUri"));
		builder.setParameter(replaceTech.get("dimensionGroup"), String.valueOf(dimensionGroupId));
		builder.setParameter(replaceTech.get("itemGroup"), String.valueOf(itemGroupId));
		builder.setParameter(replaceTech.get("vatMargin"), String.valueOf(vat));
		builder.setParameter(replaceTech.get("apiVersion"), "1.0");
		
		HttpGet getRequest = new HttpGet(builder.build());
		getRequest.addHeader("Accept", "application/json");
		getRequest.addHeader("X-ApiKey", replaceTech.get("apiKey"));
		System.out.println(getRequest.getRequestLine());
		CloseableHttpResponse response = client.getClient().execute(getRequest);
		
		List<Product> products = new ArrayList<>();
		
		if (response.getEntity() != null) {
			Product[] productsArray = om.readValue(response.getEntity().getContent(), Product[].class);
			for(Product product : productsArray){
				products.add(product);
			}
        }
		
		return products;
	}

}
