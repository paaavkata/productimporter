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
	
	public static final int DELIVERY_COST = 10;
	
	public void saveStockInfoProducts(List<Product> products){
		
		StringBuilder st = new StringBuilder();
		
		st.append("name");
		st.append(DELIMITER);
//		st.append("qty");
//		st.append(DELIMITER);
		st.append("price");
		st.append(DELIMITER);
//		st.append("currency");
//		st.append(DELIMITER);
		st.append("color");
		st.append(DELIMITER);
//		st.append("lock");
//		st.append(DELIMITER);
		st.append("visual");
		st.append(DELIMITER);
		st.append("functional");
		st.append(DELIMITER);
		st.append("box");
		st.append(System.getProperty("line.separator"));
		
		for(Product product : products){
			st.append(product.getName());
			st.append(DELIMITER);
//			st.append(product.getQty());
//			st.append(DELIMITER);
			st.append(generatePrice(product.getPrice()));
//			st.append(DELIMITER);
//			st.append(product.getCurrency());
			st.append(DELIMITER);
			String color = "";
//			String lock = "";
			String visual = "";
			String functional = "";
			String box = "";
			for(ReplaceTechAttribute attr : product.getAttributes()){
				if("Color".equals(attr.getKey())){
					color = attr.getValue();
				}
//				if("Cloud Lock".equals(attr.getKey())){
//					lock = attr.getValue();
//				}
				if("Visual Condition".equals(attr.getKey())){
					visual = attr.getValue();
				}
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
			st.append(visual);
			st.append(DELIMITER);
			st.append(functional);
			st.append(DELIMITER);
			st.append(box);
			st.append(System.getProperty("line.separator"));
		}
		
		writeDataToFile(st.toString());
		
	}
	
	private double generatePrice(int price){
		
		double newPrice = price + DELIVERY_COST;
		
		newPrice *= 1.96;
		newPrice *= (1 + MARGIN_PERCENTAGE/100);
		newPrice *= 1.2;
		
		return newPrice;
	}
	
	private void writeDataToFile(String data){
		
		File file = new File("ReplaceTechStock.csv");
		
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
