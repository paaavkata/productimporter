package bg.premiummobile.productimporter.magento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import bg.premiummobile.productimporter.magento.domain.Attribute;
import bg.premiummobile.productimporter.magento.domain.ConfigurableProductOption;
import bg.premiummobile.productimporter.magento.domain.MagentoAttribute;
import bg.premiummobile.productimporter.magento.domain.MagentoProductResponse;
import bg.premiummobile.productimporter.magento.domain.Option;
import bg.premiummobile.productimporter.magento.domain.Value;

@Component
public class MagentoPriceUpdater {

//	RequestsExecutor executor;
//	
//	List<MagentoAttribute> attributesList;
//	MagentoMappingHelper helper;
//	@Autowired
//	public MagentoPriceUpdater(RequestsExecutor executor, MagentoMappingHelper helper) throws Exception{
//		this.executor = executor;
//		this.helper = helper;
////		attributesList = executor.downloadMagentoAttributes();
//	}
//	
//	private String delimiter = ";";
//	
//	public List<String> mapFile(MultipartFile file) throws Exception {
//		CSVFile csvFile = new CSVFile(file, delimiter, true, String.valueOf('"'), "sku", true);
//		List<String> statuses = new ArrayList<>();
//        String completeData = new String(file.getBytes(),"UTF-8");
//        String[] rows = completeData.split("\n");
//        List<String> rowsList = new ArrayList<>();
//        Collections.addAll(rowsList, rows);
//        String[] fields = rowsList.get(0).split(";");
//        rowsList.remove(0);
//        System.out.println(rows.length);
//        int counter = 0;
//        for(String row : rowsList){
//        	counter++;
////        	if(counter < 171){
////        		continue;
////        	}
//        	String[] columns = row.split(delimiter);
//        	//sku, name, status, visibility, qty, inStock
//        	int stockInt = Integer.valueOf(columns[4]);
//        	int qty = Integer.valueOf(columns[5].substring(0, 1));
//        	boolean isInStock = stockInt == 1 ? true : false;
//        	System.out.println(counter);
////        	System.out.println(columns[0] + " " + columns[1] + " " + columns[2] + " " + columns[3] + " " + columns[4] + " " + qty);
//        	StockInfoProduct stockInfo = new StockInfoProduct(columns[0], Double.valueOf(columns[2]), 1, 1, qty, isInStock);
//        	System.out.println(stockInfo.getSku() + " " + columns[1] + " " + stockInfo.getPrice() + " " + stockInfo.getQty() + " " + stockInfo.getInStock());
//        	executor.updateMagentoStockInfo(stockInfo);
////        	if(counter > 50){
////        		break;
////        	}
//        }
//        StringBuilder st = new StringBuilder();
//        st.append("sku,url_key,short_decription" + "\n");
//        for(Entry<String, List<String>> entry : csvFile.getRows().entrySet()){
//        	MagentoProductResponse product = executor.getMagentoProduct(entry.getKey());
//        	if(product == null){
//        		continue;
//        	}
//        	List<Attribute> attributes = product.getCustomAttributes();
//        	String sku = product.getSku();
//        	String name = product.getName();
//        	String battery = null;
//        	String ram = null;
//        	String processor = null;
//        	String memory = null;
//        	String display = null;
//        	
//        	for(Attribute attribute : attributes){
//        		if("gsm_battery".equals(attribute.getAttributeCode())){
//        			battery = (String) attribute.getValue();
//        			battery = battery.replaceAll("Non-removable ", "");
//        			battery = battery.replaceAll("Removable ", "");
//        			continue;
//        		}
//				if("gsm_ram".equals(attribute.getAttributeCode())){
//					ram = (String) attribute.getValue();
//					continue;
//				}
//				if("procesor_filt_r".equals(attribute.getAttributeCode())){
//					processor = (String) attribute.getValue();
//					continue;
//				}
//				if("gsm_memory".equals(attribute.getAttributeCode())){
//					memory = (String) attribute.getValue();
//					continue;
//				}
//				if("gsm_display_size".equals(attribute.getAttributeCode())){
//					display = (String) attribute.getValue();
//					continue;
//				}
//        	}
//        	
//        	if(memory == null){
//        		for(ConfigurableProductOption option : product.getExtensionAttributes().getConfigurableProductOption()){
//        			if("Собствена памет GSM".equals(option.getLabel())){
//        				for(MagentoAttribute attribute : attributesList){
//        					if(option.getAttributeId() == attribute.getId()){
//        						List<Option> memoryOption = attribute.getOptions();
//        						HashMap<String, String> map = new HashMap<String, String>();
//        						for(Option option1 : memoryOption){
//        							map.put(option1.getLabel(), option1.getValue());
//        						}
//    							List<Value> values = option.getValues();
//    							int endIndex = 0;
//    							memory = "";
//    							for(Value value : values){
//    								memory.concat(map.get(value.getValueIndex()));
//    								if(endIndex < values.size() - 1){
//    									memory.concat("/");
//    								}
//    							}
//        					}
//        				}
//        			}
//        		}
//        	}
//        	String shortDesc = helper.generateShortDescription(display, processor, ram, memory, battery);
//        	st.append(sku + "," + name + "-cena-na-izplashtane,\"" + shortDesc + "\"\n");
//        }
//        System.out.println(st.toString());
//		return statuses;
//	}
}
