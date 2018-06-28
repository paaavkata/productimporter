package bg.premiummobile.productimporter.httpclients;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.imgscalr.Scalr;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.configuration.HttpClient;
import bg.premiummobile.productimporter.domain.StockInfoProduct;
import bg.premiummobile.productimporter.magento.model.Attribute;
import bg.premiummobile.productimporter.magento.model.Category;
import bg.premiummobile.productimporter.magento.model.ExtensionAttributeRequest;
import bg.premiummobile.productimporter.magento.model.ItemResponse;
import bg.premiummobile.productimporter.magento.model.MagentoAttribute;
import bg.premiummobile.productimporter.magento.model.MagentoPostProduct;
import bg.premiummobile.productimporter.magento.model.MagentoProduct;
import bg.premiummobile.productimporter.magento.model.MagentoProductRequest;
import bg.premiummobile.productimporter.magento.model.MagentoProductResponse;
import bg.premiummobile.productimporter.magento.model.MagentoSiteMapXML;
import bg.premiummobile.productimporter.magento.model.MagentoStockItemRequest;
import bg.premiummobile.productimporter.magento.model.MediaGalleryContent;
import bg.premiummobile.productimporter.magento.model.MediaGalleryEntry;
import bg.premiummobile.productimporter.magento.model.MediaGalleryEntryWrapper;
import bg.premiummobile.productimporter.magento.model.Option;
import bg.premiummobile.productimporter.magento.model.ProductLink;
import bg.premiummobile.productimporter.magento.model.TierPrice;

@Component
public class Magento2Client {

	private HttpClient client;
	
	private ConfigurationReader reader;
	
	private ObjectMapper om;
	
	private String magentoToken;
	
	private static final long TOKEN_LIFETIME_MINS = 25;
	
	private long tokenLastUsed;
	
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
			}
        }
		response.close();
	}
	
	private String magentoToken() throws Exception{
		if(this.magentoToken != null){
			if(tokenLastUsed + 1000 * 60 * TOKEN_LIFETIME_MINS < System.currentTimeMillis()) {
				magentoAuthenticate();
			}
			tokenLastUsed = System.currentTimeMillis();
			return this.magentoToken;
		}
		else{
			magentoAuthenticate();
			if(this.magentoToken != null){
				tokenLastUsed = System.currentTimeMillis();
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
			response.close();
			return statusLine;
		}
		else{
			System.out.println(om.writeValueAsString(postProduct));
		    System.out.println("Product upload error: " + EntityUtils.toString(response.getEntity(), "UTF-8"));
		    response.close();
		}
		return response.getStatusLine();
	}
	
	public StatusLine updateMagentoProduct(MagentoProduct product) throws Exception{
		MagentoPostProduct postProduct = new MagentoPostProduct();
		postProduct.setMagentoProduct(product);
		StringEntity params = new StringEntity(om.writeValueAsString(postProduct), "UTF-8");
		System.out.println("Request product JSON: ");
		System.out.println(om.writeValueAsString(postProduct));
//		System.out.println("Product: " + product.getName() + " " + product.getSku());
		HttpPost httpPost = new HttpPost(this.urlBuilder(magentoProperties.get("product")));
		httpPost.addHeader("Authorization", "Bearer " + magentoToken());
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		CloseableHttpResponse response = client.getClient().execute(httpPost);
		if(response.getStatusLine().getStatusCode() != 200){
			System.err.println("Product status: " + response.getStatusLine().getStatusCode());
		}
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
		MagentoStockItemRequest stockItemRequest = new MagentoStockItemRequest();
		stockItemRequest.setQty(0);
		stockItemRequest.setStock(false);
		initial.getExtensionAttributes().setItem(stockItemRequest);
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
		response.close();
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
	
	public StatusLine updateProductStockInfo(StockInfoProduct stockInfoProduct) throws Exception {
		return this.updateMagentoProduct(stockInfoProduct);
	}
	
	public MagentoProductResponse getProduct(String sku) throws Exception {
		HttpGet httpGet = new HttpGet(urlBuilder(magentoProperties.get("product") + "/" + sku));
		httpGet.addHeader("Authorization", "Bearer " + magentoToken());
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		MagentoProductResponse product;
		if(response.getStatusLine().getStatusCode() == 200){
			product = om.readValue(EntityUtils.toString(response.getEntity(), "UTF-8"), MagentoProductResponse.class);
			response.close();
			return product;
		}
		response.close();
		return null;
	}
	
	public MagentoSiteMapXML getSitemap() throws Exception{
		HttpGet httpGet = new HttpGet(urlBuilder(magentoProperties.get("siteMapUri")));
		CloseableHttpResponse response = client.getClient().execute(httpGet);
		MagentoSiteMapXML siteMap = getSerializer().read(MagentoSiteMapXML.class, response.getEntity().getContent());
		response.close();
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
		response.close();
		return itemResponse.getAttributes();
	}
	
	public StatusLine uploadMagentoImage(MagentoProductRequest product, String imageUrl, int counter, String provider) throws Exception{
		URL url = new URL(imageUrl);
		BufferedImage img = ImageIO.read(url);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		String extension;
		if(imageUrl.contains("png")){
			extension = "png";
		}
		else{
			extension = "jpeg";
		}
		String imageName = product.getSku() + counter + "." + extension;
		
		MediaGalleryEntry entry = new MediaGalleryEntry();
        entry.setMediaType("image");
        entry.setDisabled(false);
        if(product.getPrice() > 150){
        	entry.setLabel(product.getName() + " на топ цена и на изплащане от Примиъм Мобайл ЕООД");
        } else {
        	entry.setLabel(product.getName() + " на топ цена от Примиъм Мобайл ЕООД");
        }
        entry.setPosition(counter);
        entry.setFileName(provider + "/" + imageName);

        ArrayList<String> types = new ArrayList<String>();
        if( counter == 1 ){
        	types.add("base");
        	types.add("small_image");
        	types.add("thumbnail");
        }
        entry.setTypes(types);
        
        MediaGalleryContent content = new MediaGalleryContent();
        if(img.getHeight() > 1200 || img.getWidth() > 1200){
        	BufferedImage scaledImage = Scalr.resize(img, 1200, 1200);
        	ImageIO.write(scaledImage, extension, baos);
        }
        else{
        	ImageIO.write(img, extension, baos);
        }
		content.setName(imageName);
		content.setType("image/" + extension);
		content.setData(new String(Base64.encodeBase64(baos.toByteArray())));
		
		entry.setContent(content);
		
		MediaGalleryEntryWrapper wrapper = new MediaGalleryEntryWrapper();
		wrapper.setEntry(entry);
		
		StringEntity params = new StringEntity(om.writeValueAsString(wrapper), "UTF-8");
		
		HttpPost httpPost = new HttpPost(urlBuilder(magentoProperties.get("product") + "/" + product.getSku() + "/media"));
		httpPost.addHeader("Authorization", "Bearer " + magentoToken());
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		
		CloseableHttpResponse response = client.getClient().execute(httpPost);
		
		StatusLine statusLine = response.getStatusLine();
		System.out.println("---->Image Upload Status: " + statusLine.getStatusCode());
		
		baos.close();
		response.close();
		
		return statusLine;
	}
}
