package bg.premiummobile.productimporter.controller;

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
		service.saveStockInfoProducts(products);
	}
	
	
}
