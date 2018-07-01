package bg.premiummobile.productimporter.solytron;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import bg.premiummobile.productimporter.httpclients.SolytronClient;
import bg.premiummobile.productimporter.magento.MagentoService;
import bg.premiummobile.productimporter.magento.model.MagentoProductRequest;
import bg.premiummobile.productimporter.magento.model.MagentoProductResponse;
import bg.premiummobile.productimporter.magento.model.MagentoStockItemRequest;
import bg.premiummobile.productimporter.solytron.model.Image;
import bg.premiummobile.productimporter.solytron.model.SolytronProduct;

@Service
public class SolytronService {

	private ConfigurationReader reader;
	
	private SolytronToMagentoMapper mapper;
	
	private SolytronClient client;
	
	private MagentoService magentoService;
	
	private HashMap<String, String> magentoCategories;
	
	private HashMap<String, String> solytronCategories;
	
	
	
	@Autowired
	public SolytronService(ConfigurationReader reader, SolytronToMagentoMapper mapper, SolytronClient client, MagentoService magentoService){
		this.mapper = mapper;
		this.magentoService = magentoService;
		this.client = client;
		this.reader = reader;
		this.magentoCategories = reader.getMagentoCategories();
		this.solytronCategories = reader.getSolytronCategories();
	}
	
	public List<Result> downloadAndImportCategory(String category) throws Exception{
		List<Result> results = new ArrayList<>();
		List<SolytronProduct> solytronProducts = getCategoryStockProducts(solytronCategories.get(category));
		List<Integer> magentoCategoriesInner = new ArrayList<>();
		magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("main")));
		int attributeSetId = 4;
		if("laptop".equals(category)){
			magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("laptops")));
			attributeSetId = 10;
		}
		else if("tablet".equals(category)){
			magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("tablets")));
			attributeSetId = 11;
		}
		else if(category.contains("acc")){
			magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("accessory")));
			attributeSetId = 12;
		}
		
		int counter = 0;
		System.out.println("Total product size: " + solytronProducts.size());
		for(SolytronProduct solytronProduct : solytronProducts){
			counter++;
			System.out.println(counter);
			if(counter < 134){
				continue;
			}
			String sku = solytronProduct.getCodeId();
			sku = sku.replaceAll("/", "");
			sku = sku.replace((char) 92, (char) 0);
			sku = sku.replace((char) 34, (char) 0);
			MagentoProductResponse magentoProductResponse = magentoService.getProductBySku(sku);
			if(magentoProductResponse != null) {
				MagentoStockItemRequest magentoStockItem = mapper.generateStockInfo(solytronProduct.getStockInfoValue());
				StockInfoProduct stockInfo = new StockInfoProduct(sku, mapper.generatePrice(solytronProduct), 1, 4, magentoStockItem.getQty(), magentoStockItem.isStock());
				magentoService.updateMagentoProductStockInfo(stockInfo, magentoProductResponse);
			}
			else {
				long startTime = System.currentTimeMillis();
				Result result = new Result();
				solytronProduct = getFullProduct(solytronProduct);
				if(solytronProduct == null){
					continue;
				}
				MagentoProductRequest magentoProductRequest = mapper.mapSolytronProduct(solytronProduct, category, magentoCategoriesInner);
				magentoProductRequest.setAttributeSetId(attributeSetId);
				result.setId(magentoProductRequest.getSku());
				result.setSequenceNumber(counter);
				result.setName(magentoProductRequest.getName());
				if(solytronProduct.getImages() != null){
					result.setPhotos(solytronProduct.getImages().size());
				} else{
					result.setPhotos(0);
				}
				
				HashMap<BufferedImage, String> solytronImages = new HashMap<>();
				if(solytronProduct.getImages() != null){
					for(Image image : solytronProduct.getImages()){
						
						String extension = "jpeg";
						
						if(image.getText().contains("png")){
							extension = "png";
						}
						BufferedImage bufferedImage;
						try{
							bufferedImage = client.getImage(image.getText());
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
							solytronImages.put(bufferedImage, extension);
						}
					}	
				}
				
				result.setSuccessfullUploadedPhotos(0);

				if(solytronImages.isEmpty()){
					result.setMagentoUploadStatus(500);
					result.setTotalTime(System.currentTimeMillis() - startTime);
					System.err.println("Solytron image list is empty. Aborting upload for product with SKU: " + magentoProductRequest.getSku());
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
				catch(IOException e){
					System.err.println(e.getMessage());
					status = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, e.getMessage());
				}
				
				if(status.getStatusCode() == 200) {
					int imageCounter = 1;
					for(Entry<BufferedImage, String> entry : solytronImages.entrySet()){
						StatusLine imageStatusLine = magentoService.uploadMagentoProductImage(entry.getKey(), entry.getValue(), magentoProductRequest, imageCounter, "solytron");
						if(imageStatusLine.getStatusCode() == 200) {
							imageCounter++;
						}
					}
					result.setSuccessfullUploadedPhotos(imageCounter);
				}
				
				results.add(result);
			}
//			magentoService.saveStockInfo();
			System.out.println();
			System.out.println("==================================================================");
//			if(counter >= 10){
//				break;
//			}
		}
		
		return results;
	}
	
	public SolytronProduct getFullProduct(SolytronProduct simpleProduct){
		SolytronProduct fullProduct;
		
		try{
			fullProduct = client.downloadProduct(simpleProduct.getProductId());
		}
		catch(Exception e){
			System.out.println("Error while downloading product " + simpleProduct.getProductId() + "Exception Message: " + e.getMessage());
			return null;
		}
		fullProduct = fromSimpleToFull(simpleProduct, fullProduct);
		return fullProduct;
	}
	
	public SolytronProduct fromSimpleToFull(SolytronProduct stockInfo, SolytronProduct fullProduct){
		fullProduct.setPrice(stockInfo.getPrice());
		fullProduct.setGroupId(stockInfo.getProductId());
		fullProduct.setCodeId(stockInfo.getCodeId());
		fullProduct.setEan(stockInfo.getEan());
		fullProduct.setVendor(stockInfo.getVendor());
		fullProduct.setWarrantyQty(stockInfo.getWarrantyQty());
		fullProduct.setWarrantyUnit(stockInfo.getWarrantyUnit());
		fullProduct.setPriceEndUser(stockInfo.getPriceEndUser());
		fullProduct.setStockInfoData(stockInfo.getStockInfoData());
		fullProduct.setStockInfoValue(stockInfo.getStockInfoValue());
		return fullProduct;
	}
	
	public List<SolytronProduct> getCategoryFullProducts(String category){
		List<SolytronProduct> solytronProducts = getCategoryStockProducts(category);
		System.out.println(solytronProducts.size());
		List<SolytronProduct> productsNew = new ArrayList<SolytronProduct>();
//		HashMap<Integer, String> tabletProperties = new HashMap<Integer, String>();
//		HashMap<Integer, String> tabletPropertiesValues = new HashMap<Integer, String>();
//		HashMap<Integer, String> tabletValues = new HashMap<Integer, String>();

		int productCounter = 0;
		
		for(SolytronProduct productSimple : solytronProducts){
			System.out.println(productCounter);
			SolytronProduct fullProduct = getFullProduct(productSimple);
			productsNew.add(fullProduct);			
			
			//tablet properties for statistics and import logic;
//			for(PropertyGroup propertyGroup : product.getProperties()){
//				if(propertyGroup.getList() != null){
//					for(Property property : propertyGroup.getList()){
//						tabletProperties.put(property.getPropertyId(), property.getName());
//						for(Value value : property.getValue()){
//							tabletPropertiesValues.put(property.getPropertyId(), property.getValue().get(0).getText());
//							tabletValues.put(Integer.valueOf(value.getValueId()), value.getText());
//						}
//					}
//				}
//			}
		}
		return productsNew;
	}
	
	private List<SolytronProduct> getCategoryStockProducts(String id){
		List<SolytronProduct> solytronProducts;
		try{
			solytronProducts =  client.downloadCategory(id);
		}
		catch(Exception e){
			System.out.println("Error while downloading category " + id);
			solytronProducts = new ArrayList<SolytronProduct>();
		}
		return solytronProducts;
	}
}
