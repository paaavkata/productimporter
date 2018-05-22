package bg.premiummobile.productimporter.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.httpclients.SolytronClient;
import bg.premiummobile.productimporter.solytron.model.Property;
import bg.premiummobile.productimporter.solytron.model.PropertyGroup;
import bg.premiummobile.productimporter.solytron.model.SolytronProduct;
import bg.premiummobile.productimporter.solytron.model.Value;

@Service
public class SolytronService {

	@Autowired
	private ConfigurationReader reader;
	
	@Autowired
	private SolytronClient client;
	
	private HashMap<String, String> categories;
	
	public SolytronService(){
		this.categories = reader.getSolytronCategories();
	}
	
	public List<SolytronProduct> getCategoryFullProducts(String category){
		List<SolytronProduct> solytronProducts = getCategoryStockProducts(categories.get(category));
		System.out.println(solytronProducts.size());
		List<SolytronProduct> productsNew = new ArrayList<SolytronProduct>();
		HashMap<Integer, String> tabletProperties = new HashMap<Integer, String>();
		HashMap<Integer, String> tabletPropertiesValues = new HashMap<Integer, String>();
		HashMap<Integer, String> tabletValues = new HashMap<Integer, String>();

		int productCounter = 0;
		
		for(SolytronProduct productSimple : solytronProducts){
			productCounter++;
//			if(productCounter < 133){
//				continue;
//			}
			
			System.out.println(productCounter);
			
			SolytronProduct product;
			
			try{
				product = client.downloadProduct(productSimple);
			}
			catch(Exception e){
				System.out.println("Error while downloading product " + productSimple.getCodeId());
				product = null;
				continue; 
			}
			product.setPrice(productSimple.getPrice());
			product.setGroupId(productSimple.getProductId());
			product.setCodeId(productSimple.getCodeId());
			product.setEan(productSimple.getEan());
			product.setVendor(productSimple.getVendor());
			product.setWarrantyQty(productSimple.getWarrantyQty());
			product.setWarrantyUnit(productSimple.getWarrantyUnit());
			product.setPriceEndUser(productSimple.getPriceEndUser());
			product.setStockInfoData(productSimple.getStockInfoData());
			product.setStockInfoValue(productSimple.getStockInfoValue());
			productsNew.add(product);
			
			//tablet properties for statistics and import logic;
			for(PropertyGroup propertyGroup : product.getProperties()){
				if(propertyGroup.getList() != null){
					for(Property property : propertyGroup.getList()){
						tabletProperties.put(property.getPropertyId(), property.getName());
						for(Value value : property.getValue()){
							tabletPropertiesValues.put(property.getPropertyId(), property.getValue().get(0).getText());
							tabletValues.put(Integer.valueOf(value.getValueId()), value.getText());
						}
					}
				}
			}
//			if(productCounter >= 154){
//				break;
//			}
		}
		return productsNew;
	}
	
	public List<SolytronProduct> getCategoryStockProducts(String id){
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
	
	public List<> getProductPhotos(SolytronProduct solytronProduct){
		
	}
}
