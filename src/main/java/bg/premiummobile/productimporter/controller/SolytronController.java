package bg.premiummobile.productimporter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.StatusLine;
import org.apache.tomcat.jni.Time;
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
import bg.premiummobile.productimporter.magento.domain.MagentoStockItemRequest;
import bg.premiummobile.productimporter.solytron.SolytronService;
import bg.premiummobile.productimporter.solytron.SolytronToMagentoMapper;
import bg.premiummobile.productimporter.solytron.model.Image;
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
	
	public SolytronController(){
		this.magentoCategories = reader.getMagentoCategories();
		this.solytronCategories = reader.getSolytronCategories();
	}
	
	@GetMapping("/importCategoryToMagento/{categoryName}/")
	@ResponseBody
	public List<Result> downloadAndImportCategory(@PathVariable("categoryName") String category){
		List<Result> results = new ArrayList<>();
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
		int counter = 1;
		for(SolytronProduct solytronProduct : solytronProducts){
			String sku = solytronProduct.getCodeId();
			sku = sku.replaceAll("/", "");
			sku = sku.replace((char) 92, (char) 0);
			sku = sku.replace((char) 34, (char) 0);
			if(magentoService.isProductUploaded(sku)) {
				MagentoStockItemRequest magentoStockItem = mapper.generateStockInfo(solytronProduct.getStockInfoValue());
				StockInfoProduct stockInfo = new StockInfoProduct(sku, mapper.generatePrice(solytronProduct), 1, 4, magentoStockItem.getQty(), magentoStockItem.isStock());
				magentoService.updateMagentoProductStockInfo(stockInfo);
			}
			else {
				long startTime = Time.now();
				Result result = new Result();
				MagentoProductRequest magentoProduct = mapper.mapSolytronProduct(solytronProduct, category, magentoCategoriesInner);
				result.setId(magentoProduct.getSku());
				result.setSequenceNumber(counter++);
				result.setName(magentoProduct.getName());
				result.setPhotos(solytronProduct.getImages().size());
				StatusLine status = magentoService.uploadMagentoProduct(magentoProduct);
				result.setMagentoUploadStatus(status.getStatusCode());
				if(status.getStatusCode() == 200) {
					if(!solytronProduct.getImages().isEmpty()) {
						List<String> images = new ArrayList<>();
						for(Image image : solytronProduct.getImages()){
							images.add(image.getText());
						}
						List<StatusLine> imageStatusLines = magentoService.uploadMagentoProductImages(images, magentoProduct, "solytron");
						int success = 0;
						for(StatusLine line : imageStatusLines) {
							if(line.getStatusCode() == 200) {
								success++;
							}
						}
						result.setSuccessfullUploadedPhotos(success);
					}
				}
				result.setTotalTime(Time.now() - startTime);
				results.add(result);
			}
		}
		magentoService.saveStockInfo();
		return results;
	}
}
