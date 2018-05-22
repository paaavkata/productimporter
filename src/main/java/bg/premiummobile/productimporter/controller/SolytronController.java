package bg.premiummobile.productimporter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.domain.Result;
import bg.premiummobile.productimporter.domain.StockInfoProduct;
import bg.premiummobile.productimporter.magento.MagentoService;
import bg.premiummobile.productimporter.magento.domain.MagentoProductRequest;
import bg.premiummobile.productimporter.solytron.SolytronService;
import bg.premiummobile.productimporter.solytron.SolytronToMagentoMapper;
import bg.premiummobile.productimporter.solytron.model.SolytronProduct;

@RestController
public class SolytronController {

	@Autowired
	private SolytronService solytronService;
	
	@Autowired
	private MagentoService magentoService;
	
	@Autowired
	private SolytronToMagentoMapper mapper;
	
	@Autowired
	private ConfigurationReader reader;
	
	private HashMap<String, String> magentoCategories;
	
	private HashMap<String, String> solytronCategories;
	
	private HashMap<String, StockInfoProduct> stockInfoProducts;
	
	public SolytronController(){
		this.magentoCategories = reader.getMagentoCategories();
		this.solytronCategories = reader.getSolytronCategories();
		this.stockInfoProducts = reader.loadStockInfoProducts();
	}
	
	@GetMapping("/importCategoryToMagento/{categoryId}/")
	@ResponseBody
	public List<Result> downloadAndImportCategory(@PathVariable("categoryId") String category){
		List<SolytronProduct> solytronProducts = solytronService.getCategoryFullProducts(solytronCategories.get(category));
		List<Integer> magentoCategoriesInner = new ArrayList<>();
		magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("main")));
		if("laptop".equals(category)){
			magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("laptops")));
		}
		else if("tablet".equals(category)){
			magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("tablets")));
		}
		else if("accessory".equals(category)){
			magentoCategoriesInner.add(Integer.valueOf(magentoCategories.get("accessory")));
		}
		for(SolytronProduct solytronProduct : solytronProducts){
			MagentoProductRequest magentoProduct = mapper.mapSolytronProduct(solytronProduct, category, magentoCategoriesInner);
		}
	}
}
