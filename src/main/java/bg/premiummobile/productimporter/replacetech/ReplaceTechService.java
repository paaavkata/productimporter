package bg.premiummobile.productimporter.replacetech;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.stereotype.Service;

import bg.premiummobile.productimporter.replacetech.model.Product;
import bg.premiummobile.productimporter.replacetech.model.ReplaceTechAttribute;

@Service
public class ReplaceTechService {

	public static final char DELIMITER = ',';
	
	public static final int MARGIN_PERCENTAGE = 15;
	
	public static final int EMAG_COMMISSION_PERCENTAGE = 8;
	
	public static final int DELIVERY_COST = 10;
	
	public void saveStockInfoProducts(List<Product> products, String name){
		
		StringBuilder st = new StringBuilder();
		
		st.append("name");
		st.append(DELIMITER);
		st.append("priceInWarehouseBGN");
		st.append(DELIMITER);
		st.append("Revenue");
		st.append(DELIMITER);
		st.append("customerPrice");
		st.append(DELIMITER);
		st.append("emagPrice");
		st.append(DELIMITER);
		st.append("qty");
		st.append(DELIMITER);
//		st.append("currency");
//		st.append(DELIMITER);
		st.append("color");
		st.append(DELIMITER);
//		st.append("lock");
//		st.append(DELIMITER);
//		st.append("visual");
//		st.append(DELIMITER);
		st.append("functional");
		st.append(DELIMITER);
		st.append("box");
		st.append(System.getProperty("line.separator"));
		
		for(Product product : products){
			st.append(product.getName());
			st.append(DELIMITER);
			st.append(product.getPrice()*1.96);
			st.append(DELIMITER);
			st.append((generatePrice(product.getPrice())/1.2)-(product.getPrice()*1.96)-10);
			st.append(DELIMITER);
			st.append(generatePrice(product.getPrice()));
			st.append(DELIMITER);
			st.append(generateEmagPriceWithCommission(product.getPrice()));
			st.append(DELIMITER);
			st.append(product.getQty());
			st.append(DELIMITER);
//			st.append(product.getCurrency());
//			st.append(DELIMITER);
			String color = "";
//			String lock = "";
//			String visual = "";
			String functional = "";
			String box = "";
			for(ReplaceTechAttribute attr : product.getAttributes()){
				if("Color".equals(attr.getKey())){
					color = attr.getValue();
				}
//				if("Cloud Lock".equals(attr.getKey())){
//					lock = attr.getValue();
//				}
//				if("Visual Condition".equals(attr.getKey())){
//					visual = attr.getValue();
//				}
				if("Functional Condition".equals(attr.getKey())){
					functional = attr.getValue();
				}
				if("Boxed".equals(attr.getKey())){
					box = attr.getValue();
				}
			}
			st.append(color);
			st.append(DELIMITER);
//			st.append(lock);
//			st.append(DELIMITER);
//			st.append(visual);
//			st.append(DELIMITER);
			st.append(functional);
			st.append(DELIMITER);
			st.append(box);
			st.append(System.getProperty("line.separator"));
		}
		
		writeDataToFile(st.toString(), name);
		
	}
	
	private int generatePrice(int price){
		
		double newPrice = price + DELIVERY_COST;
		int marginPercentage = MARGIN_PERCENTAGE;
		newPrice *= 1.96;
		if(newPrice * (1 + 0.01*marginPercentage) > 100){
			newPrice += 100;
		} else {
			newPrice *= (1 + 0.01*marginPercentage);
		}
		newPrice *= 1.2;
		newPrice = prettyPrice(newPrice);
		return (int) newPrice;
	}
	
	private int prettyPrice(double oldPrice) {
		int tens = (int) oldPrice/10;
		int newPrice = tens*10 + 9;
		return newPrice;
	}

	private int generateEmagPriceWithCommission(int price){
		double priceDouble = generatePrice(price);
		int emagCommision = EMAG_COMMISSION_PERCENTAGE;
		priceDouble *= (1 + 0.01*emagCommision);
		return prettyPrice(priceDouble);
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
