package bg.premiummobile.productimporter.httpclients;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.domain.StockInfoProduct;
import bg.premiummobile.productimporter.magento.domain.Attribute;
import bg.premiummobile.productimporter.magento.domain.ExtensionAttributeRequest;
import bg.premiummobile.productimporter.magento.domain.ItemResponse;
import bg.premiummobile.productimporter.magento.domain.MagentoAttribute;
import bg.premiummobile.productimporter.magento.domain.MagentoPostProduct;
import bg.premiummobile.productimporter.magento.domain.MagentoProduct;
import bg.premiummobile.productimporter.magento.domain.MagentoProductRequest;
import bg.premiummobile.productimporter.magento.domain.MagentoProductResponse;
import bg.premiummobile.productimporter.magento.domain.MagentoSiteMapXML;
import bg.premiummobile.productimporter.magento.domain.Option;
import bg.premiummobile.productimporter.magento.domain.ProductLink;
import bg.premiummobile.productimporter.magento.domain.TierPrice;
import bg.premiummobile.productimporter.solytron.model.Category;

@Component
public class Magento2Client {

	@Autowired
	private HttpClient client;
	
	private ConfigurationReader reader;
	
	private ObjectMapper om;
	
	private String magentoToken;
	
	private Serializer serializer;
	
	private HashMap<String, String> magentoProperties;
	
	@Autowired
	public Magento2Client(ConfigurationReader reader, ObjectMapper om, HttpClient client){
		this.om = om;
		this.reader = reader;
		this.client = client;
		this.magentoProperties = reader.getMagento();
	}
	
	private void magentoAuthenticate() throws Exception{

		HttpPost httpPost = new HttpPost(this.urlBuilder(magentoProperties.get("authorization")));
		Map<String, String> data = new HashMap<>();
		data.put("username", magentoProperties.get("username"));
		data.put("password", magentoProperties.get("password"));
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(om.writeValueAsString(data)));
		CloseableHttpResponse response = client.getClient().execute(httpPost);
		
		if (response.getEntity() != null) {
			this.magentoToken = EntityUtils.toString(response.getEntity(), "UTF-8").replaceAll("\"", "");
			if(this.magentoToken.length() != 32){
				this.magentoToken = null;
				System.out.println("There was an error while authenticating to Magento2.");
			}
			else{
				System.out.println(this.magentoToken + " <======== MAGENTO TOKEN");
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						magentoToken = null;
					}
				}, 4 * 60 * 60 * 1000);
			}
        }
	}
	
	private String magentoToken() throws Exception{
		if(this.magentoToken != null){
			return this.magentoToken;
		}
		else{
			magentoAuthenticate();
			if(this.magentoToken != null){
				return this.magentoToken;
			}
			else{
				throw new Exception("There was an error while trying to authenticate to magento!");
			}
		}
	}
	
	private Serializer getSerializer(){
		if(serializer != null){
			return serializer;
		}
		else{
			serializer = new Persister();
			return serializer;
		}
	}
	
	public StatusLine newMagentoProduct(MagentoProductRequest product) throws Exception{
		HttpPut httpPut = new HttpPut(this.urlBuilder(magentoProperties.get("product") + "/" + product.getSku()));
		httpPut.addHeader("Authorization", "Bearer " + magentoToken());
		httpPut.addHeader("Content-Type", "application/json");
		MagentoPostProduct postProduct = new MagentoPostProduct();
		postProduct.setMagentoProduct(generateInitialProduct(product));
		StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
		httpPut.setEntity(params);
		CloseableHttpResponse response = client.getClient().execute(httpPut);
		StatusLine statusLine;
		if(response.getStatusLine().getStatusCode() == 200){
			statusLine = updateMagentoProduct(product);
			return statusLine;
		}
		else{
			System.out.println(om.writeValueAsString(postProduct));
			System.out.println("Product upload error: " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		}
		response.close();
		return response.getStatusLine();
	}
	
	public StatusLine updateMagentoProduct(MagentoProduct product) throws Exception{
		MagentoPostProduct postProduct = new MagentoPostProduct();
		postProduct.setMagentoProduct(product);
		StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
		System.out.println("Product Json: ");
		System.out.println(om.writeValueAsString(postProduct));
//		System.out.println("Product: " + product.getName() + " " + product.getSku());
		HttpPost httpPost = new HttpPost(this.urlBuilder(magentoProperties.get("product")));
		httpPost.addHeader("Authorization", "Bearer " + magentoToken());
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		CloseableHttpResponse response = client.getClient().execute(httpPost);
		System.out.println("Product response: " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		System.err.println("Product status: " + response.getStatusLine().getStatusCode());
		response.close();
		return response.getStatusLine();
	}
	
	private MagentoProductRequest generateInitialProduct(MagentoProductRequest product) {
		MagentoProductRequest initial = new MagentoProductRequest();
		initial.setName(product.getName());
		initial.setSku(product.getSku());
		initial.setPrice(product.getPrice());
		initial.setAttributeSetId(product.getAttributeSetId());
		initial.setStatus(1);
		initial.setVisibility(4);
		initial.setOptions(new ArrayList<Option>());
		initial.setTypeId("simple");
		initial.setExtensionAttributes(new ExtensionAttributeRequest());
		initial.setProductLinks(new ArrayList<ProductLink>());
//		initial.setMediaGalleryEntries(new ArrayList<MediaGalleryEntry>());
		initial.setTierPrices(new ArrayList<TierPrice>());
		initial.setCustomAttributes(new ArrayList<Attribute>());
		return initial;
	}
	
	public List<String> downloadMagentoCategories() throws Exception{
		HttpGet httpGet = new HttpGet(urlBuilder(magentoProperties.get("categories")));
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		httpGet.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		Category category = new Category();
		if (response.getEntity() != null) {
			category = om.readValue(EntityUtils.toString(response.getEntity()), Category.class);
        }
		
		return walkCategories(category);
	}
	
	private List<String> walkCategories(Category category) {
		List<String> result = new ArrayList<String>();
		System.out.println(category.getName() + "=" + category.getId());
		result.add(category.getName() + "=" + category.getId());
		String categoryName = "";
		for(Category categoryInner : category.getChildrenData()){
			if(categoryInner.getName().equals("Смартфони")){
				categoryName = "Smartphone";
			}
			if(categoryInner.getName().equals("Лаптопи")){
				categoryName = "Laptops";
			}
			if(categoryInner.getName().equals("Таблети")){
				categoryName = "Tablets";
			}
			if(categoryInner.getName().equals("Аксесоари")){
				categoryName = "Accessory";
			}
			if(categoryInner.getName().equals("Дронове")){
				categoryName = "Drones";
			}
			System.out.println(categoryName + "=" + categoryInner.getId());
			result.add(categoryName + "=" + categoryInner.getId());
			for(Category categoryInnerInner : categoryInner.getChildrenData()){
				System.out.println(categoryName + categoryInnerInner.getName() + "=" + categoryInnerInner.getId());
				result.add(categoryName + categoryInnerInner.getName() + "=" + categoryInnerInner.getId());
			}
		}
		return result;
	}
	
	public void updateMagentoStockInfo(StockInfoProduct stockInfoProduct) throws Exception {
		this.updateMagentoProduct(stockInfoProduct);
	}
	
	public MagentoProductResponse getMagentoProduct(String sku) throws Exception {
		HttpGet httpGet = new HttpGet(urlBuilder(magentoProperties.get("product") + "/" + sku));
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		MagentoProductResponse product;
		if(response.getStatusLine().getStatusCode() == 200){
			product = om.readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), MagentoProductResponse.class);
			return product;
		}
		response.close();
		return null;
	}
	
	public MagentoSiteMapXML getMagentoSitemap() throws Exception{
		HttpGet httpGet = new HttpGet(urlBuilder(magentoProperties.get("host") + "/" + magentoProperties.get("siteMapUri")));
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		MagentoSiteMapXML siteMap = getSerializer().read(MagentoSiteMapXML.class, response.getEntity().getContent());
		return siteMap;
	}
	
	private URI urlBuilder(String path) throws URISyntaxException{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(path);
		return builder.build();
	}
	
	public List<MagentoAttribute> downloadMagentoAttributes() throws Exception {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(magentoProperties.get("host"));
		builder.setPath(magentoProperties.get("attributes"));
		builder.setParameter("searchCriteria", "0");
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		httpGet.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		ItemResponse itemResponse = new ItemResponse();
		if (response.getEntity() != null) {
			itemResponse = om.readValue(EntityUtils.toString(response.getEntity()), ItemResponse.class);
//			System.out.println(EntityUtils.toString(response.getEntity()));
			HashMap<Integer, String> attributes = new HashMap<Integer, String>();
			TreeMap<String, String> options = new TreeMap<String, String>(); 
			for(MagentoAttribute attribute : itemResponse.getAttributes()){
				attributes.put(attribute.getId(), attribute.getName());
				if(attribute.getOptions().size() > 0){
//					System.out.println(attribute.getName());
					for(Option option : attribute.getOptions()){
						options.put(option.getValue(), option.getLabel());
//						System.out.println(option.getValue() + "=" + option.getLabel());
					}
				}
			}
//			for(Entry<Integer,String> entry : attributes.entrySet()){
//				System.out.println(entry.getKey() + "=" + entry.getValue());
//			}
//			for(Entry<String, String> entry : options.entrySet()){
//				System.out.println(entry.getKey() + "=" + entry.getValue());
//			}
        }
		return itemResponse.getAttributes();
	}
	
}
