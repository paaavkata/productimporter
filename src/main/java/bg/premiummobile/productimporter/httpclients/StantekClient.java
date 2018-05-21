package bg.premiummobile.productimporter.httpclients;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.stantek.model.StantekPriceList;

@Component
public class StantekClient {

	private HttpClient client;
	
	private Serializer serializer;
	
	private ConfigurationReader reader;
	
	private HashMap<String, String> stantekProperties;
	
	@Autowired
	public StantekClient(HttpClient client, ConfigurationReader reader){
		this.client = client;
		this.reader = reader;
		this.stantekProperties = reader.getStantek();
		this.serializer = new Persister();
	}
	
	public StantekPriceList getStantekFile(){
		HttpGet httpGet = new HttpGet(stantekProperties.get("url"));
		CloseableHttpResponse response;
		try {
			response = client.getClient().execute(httpGet);
		} catch (IOException e) {
			response = null;
			e.printStackTrace();
		}
		StantekPriceList priceList = null;
		if(response.getEntity() != null){
			try {
				priceList = serializer.read(StantekPriceList.class, response.getEntity().getContent());
			} catch (Exception e) {
				priceList = new StantekPriceList();
				e.printStackTrace();
			}
		}
		return priceList;
	}
	
}
