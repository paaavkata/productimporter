package bg.premiummobile.productimporter.controller;

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
	
	private HashMap<String, List<Integer>> magentoCategories;
	
	private HashMap<String, StockInfoProduct> stockInfoProducts;
	
	public SolytronController(){
		this.magentoCategories = reader.getSolytronCategoriesToMagentoCategories();
		this.stockInfoProducts = reader.loadStockInfoProducts();
	}
	
	@GetMapping("/importCategoryToMagento/{categoryId}/")
	@ResponseBody
	public List<Result> downloadAndImportCategory(@PathVariable("categoryId") String category){
		List<SolytronProduct> solytronProducts = solytronService.getCategoryFullProducts(category);
		List<Integer> magentoCategories = magentoCategories.get(category);
		for(SolytronProduct solytronProduct : solytronProducts){
			MagentoProductRequest magentoProduct = mapper.mapSolytronProduct(solytronProduct, magentoCategories);
			magentoService.
		}
	}
}
