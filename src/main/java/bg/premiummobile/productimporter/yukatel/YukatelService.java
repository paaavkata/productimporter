package bg.premiummobile.productimporter.yukatel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.yukatel.model.YukatelStockInfo;

@Service
public class YukatelService {
	
	public static final char DELIMITER = ',';
	
	public static final int EMAG_COMMISSION_PERCENTAGE = 8;
	
	public static final int DELIVERY_COMISSION_PERCENTAGE = 1;
	
	public static final int DELIVERY_EUROPE_EUR = 5;
	
	public static final int DELIVERY_BG_BGN = 5;
	
	public static final double EUR_BGN = 1.9559;
	
	public List<YukatelStockInfo> getYukatelStockInfo(String sessionId, boolean onlyAvailable) throws IOException{
		
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("xajax", "artikel_suche");
		if(onlyAvailable) {
			requestBody.put("xajaxargs[]", "<xjxquery><q>artnr=&adrnr=&bezeichnung=&hauptwarengruppe=3&nurlager=on</q></xjxquery>");
		}
		else {
			requestBody.put("xajaxargs[]", "<xjxquery><q>artnr=&adrnr=&bezeichnung=&hauptwarengruppe=3&warengruppe=</q></xjxquery>");
		}
						         
		Document doc = Jsoup.connect("https://wawi.yukatel.de:85/nexterp/jonas2/faktura/auftrag_neu/auftrag_request.php")
				  .data(requestBody)
				  .header("Cookie", "PHPSESSID=" + sessionId)
				  .timeout(30000)
				  .post();
		Element xjx = doc.selectFirst("xjx");
		Element cmd = xjx.selectFirst("cmd");
		String cdata = cmd.childNode(0).toString();
		cdata = cdata.substring(9,cdata.length()-2);
		Element table = Jsoup.parse(cdata);
		Element body = table.selectFirst("tbody");
		Elements rows = body.select("tr");
		List<YukatelStockInfo> stockList = new ArrayList<>();
		for(Element row : rows) {
			Elements columns = row.select("td");
			if(columns.size() < 5){
				System.out.println(columns.get(0).data());
				continue;
			}
			String availability = "0";
			String img = columns.get(2).selectFirst("img").toString();
			if(img.contains("green")){
				availability = "1";
			}
			String price = columns.get(4).selectFirst("div").selectFirst("input").attr("value").replaceAll(",", ".");
			stockList.add(new YukatelStockInfo(columns.get(0).childNode(0).toString().trim(), columns.get(1).selectFirst("div").childNode(0).toString().trim(), availability, Double.valueOf(price)));
		}
		saveStockInfoProducts(stockList, "yukatel");
		return stockList;
	}
	
	public void saveStockInfoProducts(List<YukatelStockInfo> products, String name){
		
		StringBuilder st = new StringBuilder();
		st.append("id");
		st.append(DELIMITER);
		st.append("name");
		st.append(DELIMITER);
		st.append("availability");
		st.append(DELIMITER);
		st.append("priceDEwoDel");
		st.append(DELIMITER);
		st.append("priceBGwDel");
		st.append(DELIMITER);
		st.append("premium");
		st.append(DELIMITER);
		st.append("premiumProfit");
		st.append(DELIMITER);
		st.append("emag");
		st.append(DELIMITER);
		st.append("emagProfit");
		st.append(System.getProperty("line.separator"));
		
		for(YukatelStockInfo product : products){
			double price = generatePrice(product.getPrice());
			double magento = generateMagentoPrice(price);
			double emag = generateEmagPriceWithCommission(price);
			st.append(product.getId());
			st.append(DELIMITER);
			st.append(product.getName());
			st.append(DELIMITER);
			st.append(product.getAvailability());
			st.append(DELIMITER);
			st.append(product.getPrice());
			st.append(DELIMITER);
			st.append(price);
			st.append(DELIMITER);
			st.append(magento);
			st.append(DELIMITER);
			st.append(magento/1.2 - price);
			st.append(DELIMITER);
			st.append(emag);
			st.append(DELIMITER);
			st.append(emag/1.2/1.08 - price);
			st.append(System.getProperty("line.separator"));
		}
		
		writeDataToFile(st.toString(), name);
		
	}
	
	private int calculateProfit(double price){
		if(price < 300){
			return 30;
		} else if(price < 500){
			return 40;
		} else if(price < 750){
			return 60;
		} else if(price < 1000){
			return 70;
		} else if(price < 1500){
			return 80;
		} else if(price < 2000){
			return 100;
		} else if(price < 5000){
			return 150;
		} else {
			return 300;
		}
	}
	private double generateMagentoPrice(double price){
		price+=DELIVERY_BG_BGN;
		price+=calculateDelivery(price);
		price+=calculateProfit(price);
		price*=1.2;
		return prettyPrice(price);
	}
	
	private double calculateDelivery(double price){
		//Calculating circa cash on delivery commission.
		
		return (price + 10)*1.2*(DELIVERY_COMISSION_PERCENTAGE/100);
	}
	
	private double generatePrice(double price){
		//Generating price that has to be payed on delivery from EU.
		return (price + DELIVERY_EUROPE_EUR)*EUR_BGN;
	}
	
	private int prettyPrice(double oldPrice) {
		int tens = (int) oldPrice/10;
		int newPrice = tens*10 - 1;
		return newPrice;
	}

	private int generateEmagPriceWithCommission(double price){
		price+=DELIVERY_BG_BGN;
		price+=calculateDelivery(price);
		price+=calculateProfit(price);
		price*=1.08;//(1 + EMAG_COMMISSION_PERCENTAGE/100);
		price*=1.2;
		return prettyPrice(price);
	}
	
	private void writeDataToFile(String data, String name){
		
		File file = new File(name + ".csv");
		
		if(!file.exists() && !file.isDirectory()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (PrintWriter printer = new PrintWriter(file)){
			printer.println(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
}
