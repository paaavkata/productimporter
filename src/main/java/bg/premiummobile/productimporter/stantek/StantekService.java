package bg.premiummobile.productimporter.stantek;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicStatusLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.domain.Result;
import bg.premiummobile.productimporter.domain.StockInfoProduct;
import bg.premiummobile.productimporter.httpclients.StantekClient;
import bg.premiummobile.productimporter.magento.MagentoService;
import bg.premiummobile.productimporter.magento.model.MagentoProductRequest;
import bg.premiummobile.productimporter.magento.model.MagentoProductResponse;
import bg.premiummobile.productimporter.magento.model.MagentoStockItemRequest;
import bg.premiummobile.productimporter.stantek.model.StantekProduct;
import bg.premiummobile.productimporter.util.FieldManipulationHelper;

@Service
public class StantekService {

	private StantekClient client;
	
	private FieldManipulationHelper helper;
	
	private MagentoService magentoService;
	
	private StantekToMagentoMapper mapper;
	
	private HashMap<String, String> magentoCategories;
	
	ConfigurationReader reader;
	
	@Autowired
	public StantekService(StantekToMagentoMapper mapper, ConfigurationReader reader, StantekClient client, MagentoService magentoService){
		this.reader = reader;
		this.mapper = mapper;
		this.magentoService = magentoService;
		this.client = client;
		this.magentoCategories = reader.getMagentoCategories();
	}

	public List<Result> downloadAndImportCategory(String category) {
		List<StantekProduct> stantekProducts;
		try{
			stantekProducts = client.getStantekFile(category);
		} catch(Exception e){
			System.err.println(e.getMessage());
			stantekProducts = null;
		}
//		printCatAttr(stantekProducts, category);
//		printAvailabilities(stantekProducts);
		List<Result> results = new ArrayList<>();
		List<Integer> magentoCategoriesInner = new ArrayList<>();
		magentoCategoriesInner.add(Integer.parseInt(magentoCategories.get("main")));
		int attributeSetId = 4;
		if("Notebook".equals(category)){
			magentoCategoriesInner.add(Integer.parseInt(magentoCategories.get("laptops")));
			attributeSetId = 10;
		}
		else if("Tablet".equals(category)){
			magentoCategoriesInner.add(Integer.parseInt(magentoCategories.get("tablets")));
			attributeSetId = 11;
		}
		else if(category.contains("acc")){
			magentoCategoriesInner.add(Integer.parseInt(magentoCategories.get("accessory")));
			attributeSetId = 12;
		}
		
		int counter = 0;
		System.out.println("Total product size: " + stantekProducts.size());
		for(StantekProduct stantekProduct : stantekProducts){
			counter++;
			System.out.println(counter);
			if(counter < 5){
				continue;
			}
			Map<String, String> values = mapper.getValues(stantekProduct.getDescription());
			String sku = mapper.generateSku(stantekProduct, values);
//			System.out.println(sku);
//			for(Entry<String, String> e : values.entrySet()){
//				System.out.println(e.getKey() + ": " + e.getValue());
//			}
			MagentoProductResponse magentoProductResponse = null;
//			try {
//				magentoProductResponse = magentoService.getProductBySku(sku);
//			} catch (Exception e1) {
//				System.err.println(e1.getMessage());
//				magentoProductResponse = null;
//			}
			if(magentoProductResponse != null) {
				MagentoStockItemRequest magentoStockItem = mapper.generateStockInfo(stantekProduct.getAvailability());
				StockInfoProduct stockInfo = new StockInfoProduct(sku, Double.valueOf(stantekProduct.getRetailPrice()), 1, 4, magentoStockItem.getQty(), magentoStockItem.isStock());
				magentoService.updateMagentoProductStockInfo(stockInfo, magentoProductResponse);
			}
			else {
				long startTime = System.currentTimeMillis();
				Result result = new Result();
				MagentoProductRequest magentoProductRequest = mapper.mapStantekProduct(stantekProduct, category, magentoCategoriesInner);
				magentoProductRequest.setSku(sku);
				magentoProductRequest.setAttributeSetId(attributeSetId);
				result.setId(magentoProductRequest.getSku());
				result.setSequenceNumber(counter);
				result.setName(magentoProductRequest.getName());
				String imageUrl = stantekProduct.getImage();
				
				HashMap<BufferedImage, String> stantekImages = new HashMap<>();
				if(imageUrl != null){
					result.setPhotos(1);
					String extension = "jpeg";
					
					if(imageUrl.contains("png")){
						extension = "png";
					}
					
					BufferedImage bufferedImage;
					try{
						bufferedImage = client.getImage(imageUrl);
					}
					catch(ClientProtocolException e){
						System.err.println(e.getMessage());
						bufferedImage = null;
					}
					catch(IOException e){
						System.err.println(e.getMessage());
						bufferedImage = null;
					}
					
					if(bufferedImage != null){
						stantekImages.put(bufferedImage, extension);
					}
				}
				else{
					result.setPhotos(0);
				}
				
				result.setSuccessfullUploadedPhotos(0);

				if(stantekImages.isEmpty()){
					result.setMagentoUploadStatus(500);
					result.setTotalTime(System.currentTimeMillis() - startTime);
					System.err.println("Stantek image list is empty. Aborting upload for product with SKU: " + magentoProductRequest.getSku());
					System.out.println();
					System.out.println("==================================================================");
					continue;
				}
				
				StatusLine status;
				try{
					status = magentoService.uploadMagentoProduct(magentoProductRequest);
				}
				catch(ClientProtocolException e){
					System.err.println(e.getMessage());
					status = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, e.getMessage());
				}
				catch(Exception e){
					System.err.println(e.getMessage());
					status = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, e.getMessage());
				}
				
//				if(status.getStatusCode() == 200) {
//					int imageCounter = 1;
//					for(Entry<BufferedImage, String> entry : stantekImages.entrySet()){
//						StatusLine imageStatusLine;
//						try {
//							imageStatusLine = magentoService.uploadMagentoProductImage(entry.getKey(), entry.getValue(), magentoProductRequest, imageCounter, "stantek");
//						} catch (Exception e) {
//							System.err.println(e.getMessage());
//							imageStatusLine = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, e.getMessage());
//						}
//						
//						if(imageStatusLine.getStatusCode() == 200) {
//							imageCounter++;
//						}
//					}
//					result.setSuccessfullUploadedPhotos(imageCounter);
//				}
				
				results.add(result);
			}
			System.out.println();
			System.out.println("==================================================================");
//			if(counter >= 10){
//				break;
//			}
		}
		
		return results;
		
//		printCatAttr(products, category);
//		generateNameSku(products, category);
		
	}
	
	private void generateNameSku(List<StantekProduct> products, String category) {
		for (StantekProduct product : products) {
			if(product.getGroup().equals(category)){
//				HashMap<String, String> values;
//				if (product.getDescription() != null) {
//					values = getValues(product.getDescription());
//				} else {
//					values = new HashMap<String, String>();
//				}
//				if(!values.isEmpty()){
//					System.out.println("SKU: " + generateSku(product, values));
//				}
//				System.out.println("Old Name: " + product.getName());
				System.out.println(helper.trimName(product.getName(), 5));
			}
		}
	}
	private void printAvailabilities(List<StantekProduct> products){
		Set<String> availabilities = new TreeSet<>();
		for(StantekProduct product : products){
			availabilities.add(product.getAvailability());
		}
		for(String s : availabilities){
			System.out.println(s);
		}
	}
	private void printCatAttr(List<StantekProduct> products, String category) {
		
		if(category == null){
			
			Set<String> categories = new TreeSet<>();
			for (StantekProduct product : products) {
				categories.add(product.getGroup());
			}

			System.err.println("CATEGORIES:");
			for(String s : categories){
				System.out.println(s);
			}
			
		} else if(!category.equals("categories") && !category.equals("NotebeookNames")){
			
			Set<String> attributes = new TreeSet<>();
			Set<String> uniqueAttributes = new TreeSet<>();
			for (StantekProduct product : products) {
				if(product.getGroup().equals(category)){
					Map<String, String> values;
					if (product.getDescription() != null) {
						values = mapper.getValues(product.getDescription());
					} else {
						values = new HashMap<String, String>();
					}
					if(!values.isEmpty()){
						attributes.addAll(values.keySet());
						uniqueAttributes.addAll(values.values());
					}
				}
			}
			System.err.println("Unique attributes");
			for(String s : attributes){
				System.out.println(s);
			}
			System.out.println();
			System.out.println();
			System.err.println("Unique values");
			for(String s : uniqueAttributes){
				System.out.println(s);
			}
		}
		else {
			for (StantekProduct product : products) {
				if(product.getGroup().equals("Notebook")){
					System.out.println(product.getName());
				}
			}
		}
	}


}
