package bg.premiummobile.productimporter.magento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicStatusLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.domain.StockInfoProduct;
import bg.premiummobile.productimporter.httpclients.Magento2Client;
import bg.premiummobile.productimporter.magento.domain.MagentoProductRequest;

@Service
public class MagentoService {

	private Magento2Client magentoClient;
	
	private ConfigurationReader reader;
	
	private HashMap<String, StockInfoProduct> stockInfo;
	
	@Autowired
	public MagentoService(ConfigurationReader reader, Magento2Client magentoClient) {
		this.magentoClient = magentoClient;
		this.reader = reader;
		this.stockInfo = reader.loadStockInfoProducts();
	}
	
	
	public StatusLine uploadMagentoProduct(MagentoProductRequest magentoProduct){
		
		StatusLine status;
		try {
			status = magentoClient.newMagentoProduct(magentoProduct);
		} catch (Exception e) {
			status = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, "Error");
		}
		
		if(status.getStatusCode() == 200) {
			StockInfoProduct stockInfoProduct = new StockInfoProduct(
							magentoProduct.getSku(), 
							magentoProduct.getPrice(), 
							magentoProduct.getStatus(), 
							magentoProduct.getVisibility(),
							magentoProduct.getExtensionAttributes().getItem().getQty(),
							magentoProduct.getExtensionAttributes().getItem().isStock());
			this.stockInfo.put(stockInfoProduct.getSku(), stockInfoProduct);
		}
		return status;
	}

	public List<StatusLine> uploadMagentoProductImages(List<String> imageUrls,	MagentoProductRequest magentoProduct, String provider){
		int counter = 1;
		List<StatusLine> statuses = new ArrayList<>();
		for(String image : imageUrls){
			try {
				statuses.add(magentoClient.uploadMagentoImage(magentoProduct, image, counter++, provider));
			}
			catch(Exception e) {
				statuses.add(new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, "Error"));
			}
		}
		return statuses;
	}
	
	public boolean isProductUploaded(String sku) {
		return this.stockInfo.containsKey(sku);
	}
	
	public void saveStockInfo() {
		this.reader.saveStockInfoProducts(stockInfo);
	}
	
	public StatusLine updateMagentoProductStockInfo(StockInfoProduct newStockInfo){
		StockInfoProduct oldStockInfo = stockInfo.get(newStockInfo.getSku());
		StatusLine status = null;
		if(isStockChanged(newStockInfo, oldStockInfo)) {
			try {
				status = this.magentoClient.updateProductStockInfo(newStockInfo);
			}
			catch(Exception e) {
				status = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, "Error");
			}
			if(status.getStatusCode() == 200) {
				this.stockInfo.put(newStockInfo.getSku(), newStockInfo);
			}
		}
		return status;
		
	}
	
	private boolean isStockChanged(StockInfoProduct newStockInfo, StockInfoProduct oldStockInfo) {
		if(oldStockInfo.getInStock() != newStockInfo.getInStock()) {
			return true;
		}
		if(oldStockInfo.getQty() != newStockInfo.getQty()) {
			return true;
		}
		if(oldStockInfo.getPrice() != newStockInfo.getPrice()) {
			return true;
		}
		return false;
	}
}
