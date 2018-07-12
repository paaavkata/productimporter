package bg.premiummobile.productimporter.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bg.premiummobile.productimporter.httpclients.ReplaceTechClient;
import bg.premiummobile.productimporter.replacetech.ReplaceTechService;
import bg.premiummobile.productimporter.replacetech.model.Product;
import bg.premiummobile.productimporter.replacetech.model.ReplaceTechAttribute;

@RestController
@RequestMapping("/replaceTech")
public class ReplaceTechController {

	@Autowired
	private ReplaceTechClient client;
	
	@Autowired
	private ReplaceTechService service;
	
	//slug = working
	//dimensionGroupId = 1
	//itemGroupId = 1
	//vat-margin = false
	@GetMapping("/downloadCategory")
	@ResponseBody
	public void downloadAndImportCategory(@RequestParam("slug") String slug, @RequestParam("dimensionGroupId") int dimensionGroupId, 
			@RequestParam("itemGroupId") int itemGroupId, @RequestParam("vat") boolean vat) throws Exception{


		List<Product> products = client.getCategory(slug, dimensionGroupId, itemGroupId, vat);
		List<Product> classAPlus = new ArrayList<>();
		List<Product> classA = new ArrayList<>();
		List<Product> classB = new ArrayList<>();
		List<Product> classCPlus = new ArrayList<>();
		List<Product> classC = new ArrayList<>();
		List<Product> brandNew = new ArrayList<>();
		List<Product> swap = new ArrayList<>();
		for(Product product : products){
			for(ReplaceTechAttribute attr : product.getAttributes()){
				if(attr.getKey().equals("Appearance")){
					if(attr.getValue().equals("Grade A+")){
						classAPlus.add(product);
					}
					if(attr.getValue().equals("Grade A")){
						classA.add(product);
					}
					if(attr.getValue().equals("Grade B")){
						classB.add(product);
					}
					if(attr.getValue().equals("Grade C+")){
						classCPlus.add(product);
					}
					if(attr.getValue().equals("Grade C")){
						classC.add(product);
					}
					if(attr.getValue().equals("Brand New")){
						brandNew.add(product);
					}
					if(attr.getValue().equals("Swap")){
						swap.add(product);
					}
					break;
				}
			}
		}
		service.saveStockInfoProducts(classAPlus, "Class-A-Plus");
		service.saveStockInfoProducts(classA, "Class-A");
		service.saveStockInfoProducts(classB, "Class-B");
		service.saveStockInfoProducts(classCPlus, "Class-C-Plus");
		service.saveStockInfoProducts(classC, "Class-C");
		service.saveStockInfoProducts(swap, "Swap");
		service.saveStockInfoProducts(brandNew, "Brand-New");
	}
	
	
}
