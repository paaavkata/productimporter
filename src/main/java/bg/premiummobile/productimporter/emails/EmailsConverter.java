package bg.premiummobile.productimporter.emails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EmailsConverter {
	
	public List<Order> convertEmail() throws IOException{
//	public List<String> convertEmail() throws IOException{
		HashMap<String, String> descriptions = new HashMap<String, String>();
		File fileDir = new File("mail.txt");
		List<Order> orders = new ArrayList<Order>();
//		List<String> orders = new ArrayList<String>();
	    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));
		try {
		    StringBuilder sb1 = new StringBuilder();
		    String description = new String();
		    String line = br.readLine();
		    
		    while (line != null) {
		    	if(line.contains("From:	")){
		    		Order order = new Order();
		    		line = br.readLine();
		    		if(line != null && line.contains("Sent:")){
		    			order.setDate(line.substring(6));
		    			System.out.println("Date: " + order.getDate());
		    		}
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Име:")){
		    			order.setFname(line.substring(5));
		    			System.out.println("Fname: " + order.getFname());
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Фамилия:")){
		    			order.setLname(line.substring(9));
		    			System.out.println("Lname: " + order.getLname());
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("ЕГН:")){
		    			order.setEgn(line.substring(5));
		    			System.out.println("EGN: " + order.getEgn());
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Телефон:")){
		    			order.setPhone(line.substring(9));
		    			System.out.println("Phone: " + order.getPhone());
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Допълнителен телефон:")){
		    			order.setPhone2(line.substring(22));
		    			System.out.println("Phone2: " + order.getPhone2());
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("E-mail:")){
		    			order.setEmail(line.substring(8));
		    			System.out.println("Email: " + order.getEmail());
		    		}
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Избран артикул(и):")){
		    			order.setProduct(line.substring(19));
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Допълнителни опции:")){
		    			order.setProduct(order.getProduct() + " " + line.substring(20).replaceAll(";", ""));
		    			br.readLine();
			    		line = br.readLine();
		    		}
		    		System.out.println("Product: " + order.getProduct());
		    		if(line != null && line.contains("Финансова институция:")){
		    			order.setBank(line.substring(22));
		    			System.out.println("Bank: " + order.getBank());
		    		}
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Брутна сума:")){
		    			order.setPrice(line.substring(13));
		    			System.out.println("Price: " + order.getPrice());
		    		}
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		line = br.readLine();
		    		if(line != null && line.contains("Брой вноски:")){
		    			order.setPeriod(line.substring(13));
		    			System.out.println("Period: " + order.getPeriod());
		    		}
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		br.readLine();
		    		orders.add(order);
		    		System.out.println("===============================");
		    	}
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
		
		
		
		return orders;
	}
}
