package bg.premiummobile.productimporter.stantek;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.magento.model.Attribute;
import bg.premiummobile.productimporter.magento.model.ExtensionAttributeRequest;
import bg.premiummobile.productimporter.magento.model.MagentoProductRequest;
import bg.premiummobile.productimporter.magento.model.MagentoStockItemRequest;
import bg.premiummobile.productimporter.stantek.model.StantekProduct;
import bg.premiummobile.productimporter.util.FieldManipulationHelper;

@Component
public class StantekToMagentoMapper {
	
	@Autowired
	private FieldManipulationHelper helper;
	
	private Map<String, String> stantekProperties;
	
	private ConfigurationReader reader;
	
	public StantekToMagentoMapper(FieldManipulationHelper helper, ConfigurationReader reader){
		this.helper = helper;
		this.reader = reader;
		this.stantekProperties = reader.getStantek();
	}
	
	public static final boolean TAX_INCLUDED = true;
	
	public MagentoProductRequest mapStantekProduct(StantekProduct stantekProduct, String type, List<Integer> categories){
		MagentoProductRequest magentoProduct;
		
		if("Notebook".equals(type)){
			magentoProduct = generateLaptop(stantekProduct, categories);
		}
		else if("Tablet".equals(type)){
			magentoProduct = generateTablet(stantekProduct, categories);
		}
		else{
			magentoProduct = generateAccessory(stantekProduct, categories);
		}
		
		return magentoProduct;
	}
	
	private MagentoProductRequest generateLaptop(StantekProduct stantekProduct, List<Integer> categories){
		MagentoProductRequest magentoProduct = generateSkeleton(stantekProduct, categories);
		Map<String, String> properties = getValues(stantekProduct.getDescription());
		List<Attribute> customAttributes = magentoProduct.getCustomAttributes();
		
		magentoProduct.setName(helper.trimName(stantekProduct.getName(), 5));
		Double weight = helper.generateWeight(properties.remove(stantekProperties.get("weight")), properties.get(stantekProperties.get("weight2")));
		magentoProduct.setWeight(weight);
		
		String displaySize = helper.generateDisplaySize(helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("displaySize")), 
				properties.remove(stantekProperties.get("displaySize2"))), true));
		String hdd = helper.generateHddSize(helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("hddGB")), 
				properties.remove(stantekProperties.get("hddSize"))), true));
		String cpu = helper.generateCpuFilter(properties.remove(stantekProperties.get("cpuSeries")), properties.remove(stantekProperties.get("cpu")));
		String battery = helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("battery1")), 
				properties.remove(stantekProperties.get("battery2")), 
				properties.remove(stantekProperties.get("battery3")), 
				properties.remove(stantekProperties.get("battery4")), 
				properties.remove(stantekProperties.get("battery5"))), true);
		String ram = helper.generateRamFilter(helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("memory1")), 
				properties.remove(stantekProperties.get("memory2"))), true));
		String laptopDisplayInfo = helper.mergeStrings(Arrays.asList(properties.remove(stantekProperties.get("displayType")), properties.remove(stantekProperties.get("displayType2"))), true);
		
		customAttributes.add(helper.newKeyValueAttribute("url_key", helper.generateUrl(magentoProduct.getName(), true)));
		
		customAttributes.add(helper.newKeyValueAttribute("meta_description", helper.generateMetaDescription(magentoProduct.getName(), true)));
		
		customAttributes.add(helper.newKeyValueAttribute("meta_title", helper.generateMetaTitle(magentoProduct.getName(), true)));
		
		customAttributes.add(helper.newKeyListAttribute("hdd_razmer_filt_r_laptop", helper.generateHddFilter(hdd)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_battery", battery));
		
		customAttributes.add(helper.newKeyValueAttribute("color", helper.generateColorFilter(properties.remove(stantekProperties.get("color")))));

		customAttributes.add(helper.newKeyListAttribute("laptop_cpu_filter", cpu));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_dimensions", helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("dimensions1")), 
				properties.remove(stantekProperties.get("dimensions2"))), true)));

		customAttributes.add(helper.newKeyValueAttribute("laptop_display_info", laptopDisplayInfo));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_display_resolution", helper.generateDisplayResolution(properties.remove(stantekProperties.get("displayResolution")), properties.remove(stantekProperties.get("screenResolution")))));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_display_size", displaySize));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_gpu", helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("graphics1")), 
				properties.remove(stantekProperties.get("graphics2")), 
				properties.remove(stantekProperties.get("graphics3")), 
				properties.remove(stantekProperties.get("graphics4")),
				properties.remove(stantekProperties.get("graphics5"))), true)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_hdd_info", helper.mergeStrings(Arrays.asList(properties.get(stantekProperties.get("hdd")), properties.get(stantekProperties.get("hddType"))), true)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_hdd_size", hdd));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_optical", properties.get(stantekProperties.get("optical")) != null ? properties.remove(stantekProperties.get("optical")) : "No optical device"));
		
		String osFilter = helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("sofware1")), 
				properties.remove(stantekProperties.get("sofware2")), 
				properties.remove(stantekProperties.get("sofware3")),
				properties.remove(stantekProperties.get("sofware4")),
				properties.remove(stantekProperties.get("sofware5"))), true);
		
		customAttributes.add(helper.newKeyListAttribute("laptop_os_filter", helper.generateOsFilter(osFilter)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_processor", helper.mergeStrings(Arrays.asList(properties.remove(stantekProperties.get("cpu2")), properties.remove(stantekProperties.get("cpuName")), properties.remove(stantekProperties.get("cpuSeries"))), true)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_ram", ram));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_ram_info", helper.mergeStrings(Arrays.asList(properties.remove(stantekProperties.get("memoryType")), properties.remove(stantekProperties.get("memoryType2")), properties.remove(stantekProperties.get("memoryType3"))), true)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_weight", String.valueOf(weight)));
		
		String wifi = helper.mergeStrings(Arrays.asList(
				properties.remove(stantekProperties.get("wifi1")), 
				properties.remove(stantekProperties.get("wifi2")), 
				properties.remove(stantekProperties.get("wifi3"))), true);
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_wifi", !wifi.equals("") ? wifi : "No data");
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_warranty", helper.mergeStrings(Arrays.asList(properties.remove(stantekProperties.get("warranty")), properties.remove(stantekProperties.get("warranty2"))), true)));

//		Operation generates values for yes/no filtering. Usage of the method is as follows:
//		generateLaptopYesNo(bluetooth, camera, ssd, ssd2, reader, fingerprint, hdmi, hdmi2, onelink, usb3, rj45, sensorscreen, usbc)
		customAttributes.add(helper.newKeyListAttribute("laptop_yes_no", helper.generateLaptopYesNo(
				properties.remove(helper.mergeStrings(Arrays.asList(
						properties.remove(stantekProperties.get("bluetooth")), 
						properties.remove(stantekProperties.get("bluetooth2"))), true)),
				
				properties.remove(helper.mergeStrings(Arrays.asList(
						properties.remove(stantekProperties.get("camera1")), 
						properties.remove(stantekProperties.get("camera2")), 
						properties.remove(stantekProperties.get("camera3"))), true)),
				
				properties.remove(stantekProperties.get("hdd")), 
				
				properties.remove(stantekProperties.get("hddType")), 
				
				properties.remove(helper.mergeStrings(Arrays.asList(
						properties.remove(stantekProperties.get("cardReader")), 
						properties.remove(stantekProperties.get("cardReader2"))), true)),
				properties.remove(properties.remove(stantekProperties.get("fingerPrint"))),
				
				properties.remove(helper.mergeStrings(Arrays.asList(
						properties.remove(stantekProperties.get("hdmi1")), 
						properties.remove(stantekProperties.get("hdmi2")), 
						properties.remove(stantekProperties.get("hdmi3")), 
						properties.remove(stantekProperties.get("hdmi4")), 
						properties.remove(stantekProperties.get("hdmi5"))), true)),
				
				null,
				
				properties.remove(stantekProperties.get("oneLink")),
				
				properties.remove(stantekProperties.get("usb3")),
				
				properties.remove(stantekProperties.get("rj45")), 
				
				laptopDisplayInfo, 
				
				properties.remove(stantekProperties.get("usbc"))
				)));
		
		StringBuilder portsString = new StringBuilder();
		
		List<String> portPropertyIds = Arrays.asList("usb","hdmiPort","rj45","rj11","ieee1394FireWire","mic-spk","miniDisplayPort","miniVga","miniHdmi", "oneLink", "parallelPort", "displayPort", "ethernet", "serialPort", "dokingConnector");
		
		for(String s : portPropertyIds){
			if(properties.containsKey(stantekProperties.get(s))){
				portsString.append(stantekProperties.get(s) + ", ");
				properties.remove(s);
			}
		}
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_ports", portsString.toString().trim()));
		
		StringBuilder otherInfoString = new StringBuilder();
		for(Entry<String, String> entry : properties.entrySet()){
			otherInfoString.append(entry.getKey() + ": " + entry.getValue() + ", ");
		}
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_other_info", otherInfoString.toString()));
		
		customAttributes.add(helper.newKeyValueAttribute("short_description", helper.generateShortDescription(displaySize, cpu, ram, hdd, battery)));
		
		magentoProduct.setCustomAttributes(customAttributes);
		
		return magentoProduct;
	}
	
	private MagentoProductRequest generateTablet(StantekProduct stantekProduct, List<Integer> categories){
		MagentoProductRequest magentoProduct = generateSkeleton(stantekProduct, categories);

		return magentoProduct;
	}
	
	private MagentoProductRequest generateAccessory(StantekProduct stantekProduct, List<Integer> categories){
		MagentoProductRequest magentoProduct = generateSkeleton(stantekProduct, categories);
		magentoProduct.setName(helper.trimName(stantekProduct.getName(), 6));
		Map<String, String> properties = getValues(stantekProduct.getDescription());
		
		return magentoProduct;
	}
	
	private MagentoProductRequest generateSkeleton(StantekProduct product, List<Integer> categories){
		MagentoProductRequest magentoProduct = new MagentoProductRequest();
		List<Attribute> customAttributes = new ArrayList<>();
		magentoProduct.setStatus(1);
		magentoProduct.setVisibility(4);
		magentoProduct.setPrice(Double.valueOf(product.getRetailPrice()));
		magentoProduct.setTypeId("simple");
		magentoProduct.setName(product.getName());
		
		String brand = helper.generateBrand(magentoProduct.getName());

		magentoProduct.setExtensionAttributes(new ExtensionAttributeRequest());
		magentoProduct.getExtensionAttributes().setItem(generateStockInfo(product.getAvailability()));
		
		customAttributes.add(helper.newKeyValueAttribute("manufacturer", brand));
		
		List<String> stringCategories = new ArrayList<>();
		for(Integer cat : categories){
			stringCategories.add(String.valueOf(cat));
		}
		
		customAttributes.add(helper.newKeyListAttribute("category_ids", stringCategories));
		
		magentoProduct.setCustomAttributes(customAttributes);
		
		return magentoProduct;
	}
	
	public MagentoStockItemRequest generateStockInfo(String stockInfo) {
		
		MagentoStockItemRequest magentoStockItem = new MagentoStockItemRequest();
		
		if(stockInfo != null){
			if(stockInfo.contains("YES, WITH ORDER")){
				magentoStockItem.setStock(true);
				magentoStockItem.setQty(1);
			}
			else if(stockInfo.contains("YES")){
				magentoStockItem.setStock(true);
				magentoStockItem.setQty(5);
			}
		}
		else{
			magentoStockItem.setStock(false);
			magentoStockItem.setQty(0);
		}
		
		return magentoStockItem;
	}
	
	public Map<String, String> getValues(String desc) {
		Map<String, String> values = new HashMap<>();
		if(desc == null){
			return values;
		}
		for (int i = 0; i < desc.length(); i++) {
			if (desc.charAt(i) == '[') {
				i++;
				StringBuilder st1 = new StringBuilder();
				while (i < desc.length()) {
					if (desc.charAt(i) == ']' || desc.charAt(i) == ':') {
						break;
					}
					st1.append(desc.charAt(i));
					i++;
				}
				i += 2;
				StringBuilder st2 = new StringBuilder();
				while (i < desc.length()) {
					if (i > desc.length() - 1 || desc.charAt(i) == '\n') {
						break;
					}
					st2.append(desc.charAt(i));
					i++;
				}
				values.put(st1.toString().trim(), st2.toString().trim());
			}
		}
		return values;
	}
	
	public String generateSku(StantekProduct product, Map<String, String> values) {
		StringBuilder st = new StringBuilder();
		String name = product.getName();
		String sku = "";
		if (product.getDescription() != null) {
			if (values.containsKey("Партиден номер")) {
				sku = values.get("Партиден номер");
			} else {
				if (!name.contains(",")) {
					for (int i = name.length() - 1; i >= name.length() - 40; i--) {
						if (name.charAt(i) == ' ') {
							break;
						}
						st.append(name.charAt(i));
					}
					sku = st.reverse().toString();
				} else {
					boolean shouldBrake = false;
					for (int i = 0; i < name.length(); i++) {
						if (name.charAt(i) == ',') {
							for (int j = i + 2; i < name.length(); j++) {
								if (j >= name.length() - 1) {
									shouldBrake = true;
									break;
								}
								if (name.charAt(j) == ',' || name.charAt(j) == ' ') {
									shouldBrake = true;
									break;
								}
								st.append(name.charAt(j));
							}
							if (shouldBrake) {
								break;
							}
						}
					}
				}
				sku = st.toString();
			}
		}
		return sku;
	}

}
