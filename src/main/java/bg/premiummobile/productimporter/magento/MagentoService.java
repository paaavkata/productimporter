package bg.premiummobile.productimporter.magento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicStatusLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.domain.StockInfoProduct;
import bg.premiummobile.productimporter.httpclients.Magento2Client;
import bg.premiummobile.productimporter.magento.model.MagentoProductRequest;

@Service
public class MagentoService {

	private Magento2Client magentoClient;
	
	private ConfigurationReader reader;
	
	private HashMap<String, StockInfoProduct> stockInfo;
	
	private String CSV_DELIMITER = ";";
	
	private boolean ADD_VAT = false;
	
	private boolean REMOVE_VAT = false;
	
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
			status = new BasicStatusLine(new ProtocolVersion("http", 1, 1), 500, e.getMessage());
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
	
	@Async
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
		if(null == oldStockInfo){
			return true;
		}
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
	
	public List<String> mapFile(MultipartFile file) throws Exception {
//		CSVFile csvFile = new CSVFile(file, delimiter, true, String.valueOf('"'), "sku", true);
		List<String> statuses = new ArrayList<>();
        String completeData = new String(file.getBytes(),"UTF-8");
        String[] rows = completeData.split("\n");
        List<String> rowsList = new ArrayList<>();
        Collections.addAll(rowsList, rows);
        String[] fields = rowsList.get(0).split(CSV_DELIMITER);
        rowsList.remove(0);
        System.out.println(rows.length);
        int counter = 0;
        for(String row : rowsList){
        	counter++;
//        	if(counter < 171){continue;}
        	String[] columns = row.split(CSV_DELIMITER);
        	//sku, name, price, inStock, qty 
        	int stockInt = Integer.valueOf(columns[3]);
        	int qty = Integer.valueOf(columns[4].substring(0, 1));
        	Double price = "".equals(columns[2]) ? null : Double.valueOf(columns[2]);
        	if(null != price){
	        	if(ADD_VAT){
	        		price *= 1.2;
	        	}
	        	if(REMOVE_VAT){
	        		price /= 1.2;
	        	}
        	}
        	boolean isInStock = stockInt == 1 ? true : false;
        	System.out.println(counter);
        	StockInfoProduct stockInfo = new StockInfoProduct(columns[0], price, 1, 1, qty, isInStock);
        	System.out.println(stockInfo.getSku() + " " + columns[1] + " " + stockInfo.getPrice() + " " + stockInfo.getQty() + " " + stockInfo.getInStock());
        	statuses.add(this.updateMagentoProductStockInfo(stockInfo).toString());
//        	if(counter > 50){break;}
        }
        return statuses;
	}
}
