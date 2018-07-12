package bg.premiummobile.productimporter.asbis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.asbis.model.AsbisProduct;
import bg.premiummobile.productimporter.asbis.model.AsbisProductAttribute;
import bg.premiummobile.productimporter.asbis.model.AsbisProductPriceInfo;
import bg.premiummobile.productimporter.domain.Result;
import bg.premiummobile.productimporter.httpclients.AsbisClient;

@Service
public class AsbisService {

	@Autowired
	private AsbisClient client;
	
	public List<Result> downloadAndImportCategory(String category){
		List<AsbisProduct> products;
		Map<String, AsbisProduct> categoryProductsMap = new HashMap<>();
		
		List<AsbisProductPriceInfo> productPrices;
		Map<String, AsbisProductPriceInfo> categoryProductPricesMap = new HashMap<>();
		Map<String, Integer> categories = new TreeMap<>();
		Map<String, Integer> attributes = new TreeMap<>();
		try{
			products = client.downloadAsbisProducts();
		} catch (Exception e){
			System.out.println(e.getMessage());
			products = new ArrayList<>();
		}
		for(AsbisProduct product : products){
			if(!categories.containsKey(product.getCategory())){
				categories.put(product.getCategory(), 1);
			} else {
				int counter = categories.get(product.getCategory());
				categories.put(product.getCategory(), ++counter);
			}
			
			if(product.getCategory().equals(category)){
				if(product.getAttributes() != null){
					for(AsbisProductAttribute attr : product.getAttributes()){
						if(!attributes.containsKey(attr.getName())){
							attributes.put(attr.getName(), 1);
						} else {
							int counter = attributes.get(attr.getName());
							attributes.put(attr.getName(), ++counter);
						}
					}
				}
			}
		}
		for(Entry<String, Integer> e : categories.entrySet()){
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		
		for(Entry<String, Integer> e : attributes.entrySet()){
			System.out.println(e.getKey() + ": " + e.getValue());
		}
		
//		try{
//			productPrices = client.downloadAsbisProductPrices();
//		} catch (Exception e){
//			productPrices = new ArrayList<>();
//		}
//		
//		for(AsbisProductPriceInfo product : productPrices){
//			categoryProductPricesMap.put(product.getAsbisSku(), product);
//		}
//		
//		for(AsbisProduct product : products){
//			if(product.getCategory().equals(category)){
//				
//				AsbisProductPriceInfo info = categoryProductPricesMap.get(product.getAsbisSku());
//				
//				product.setEan(info.getEan());
//				product.setAvailable(info.getAvailability());
//				product.setMyPrice(info.getMyPrice());
//				product.setRetailPrice(info.getRetailPrice());
//
//				categoryProductsMap.put(product.getAsbisSku(), product);
//			}
//		}
		
		return new ArrayList<>();
	}
	
}
