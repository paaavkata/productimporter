package bg.premiummobile.productimporter.httpclients;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.stantek.model.StantekPriceList;
import bg.premiummobile.productimporter.stantek.model.StantekProduct;

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
	
	public List<StantekProduct> getStantekFile(String category) throws IOException{
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
		if(!category.equals("ALL")){
			List<StantekProduct> categoryProducts = new ArrayList<>();
			for(StantekProduct product : priceList.getProducts()){
				if(product.getGroup().equals(category)){
					categoryProducts.add(product);
				}
			}
			response.close();
			return categoryProducts;
		} else {
			response.close();
			return priceList.getProducts();
		}
	}
	
	public BufferedImage getImage(String urlString) throws ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(urlString);
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		if(response.getStatusLine().getStatusCode() == 200){
			BufferedImage img = ImageIO.read(response.getEntity().getContent());
			response.close();
			return img;
		} else {
			return null;
		}
	}
	
}
