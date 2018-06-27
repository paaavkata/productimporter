package bg.premiummobile.productimporter.solytron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bg.premiummobile.productimporter.magento.model.Attribute;
import bg.premiummobile.productimporter.magento.model.ExtensionAttributeRequest;
import bg.premiummobile.productimporter.magento.model.KeyValueAttribute;
import bg.premiummobile.productimporter.magento.model.MagentoProductRequest;
import bg.premiummobile.productimporter.magento.model.MagentoStockItemRequest;
import bg.premiummobile.productimporter.solytron.model.Property;
import bg.premiummobile.productimporter.solytron.model.PropertyGroup;
import bg.premiummobile.productimporter.solytron.model.SolytronProduct;
import bg.premiummobile.productimporter.util.FieldManipulationHelper;

@Component
public class SolytronToMagentoMapper {

	@Autowired
	private FieldManipulationHelper helper;
	
	public static final boolean TAX_INCLUDED = true;
	
	public MagentoProductRequest mapSolytronProduct(SolytronProduct solytronProduct, String type, List<Integer> categories){
		MagentoProductRequest magentoProduct;
		
		if("laptop".equals(type)){
			magentoProduct = generateLaptop(solytronProduct, categories);
		}
		else if("tablet".equals(type)){
			magentoProduct = generateTablet(solytronProduct, categories);
		}
		else{
			magentoProduct = generateAccessory(solytronProduct, categories);
		}
		
		return magentoProduct;
	}
	
	private MagentoProductRequest generateLaptop(SolytronProduct solytronProduct, List<Integer> categories){
		MagentoProductRequest magentoProduct = generateSkeleton(solytronProduct, categories);
		HashMap<Integer, Property> properties = generatePropertiesMap(solytronProduct);
		HashMap<Integer, String> propertiesMap = new HashMap<Integer, String>();
		List<Attribute> customAttributes = magentoProduct.getCustomAttributes();
		
		magentoProduct.setName(helper.trimName(solytronProduct.getName(), 4));
		magentoProduct.setWeight(Double.valueOf(properties.get(48) != null ? properties.get(48).getValue().get(0).getText() : "1"));
		
		for(Property property : properties.values()){
			propertiesMap.put(property.getPropertyId(), property.getValue().get(0).getText());
		}
		
		String displaySize = helper.generateDisplaySize(propertiesMap.get(1));
		String hdd = helper.generateHddSize(propertiesMap.get(11));
		String cpu = helper.generateCpuFilter(propertiesMap.remove(55), propertiesMap.get(2));
		String battery = propertiesMap.get(43) != null ? propertiesMap.remove(43) : "" + propertiesMap.get(44) != null ? " " + propertiesMap.remove(44) : "";
		String ram = helper.generateRamFilter(propertiesMap.remove(5));
		String laptopDisplayInfo = propertiesMap.get(9) != null ? propertiesMap.remove(9).trim() : "";

		customAttributes.add(helper.newKeyListAttribute("hdd_razmer_filt_r_laptop", helper.generateHddFilter(hdd)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_battery", battery));
		
		customAttributes.add(helper.newKeyValueAttribute("color", helper.generateColorFilter(propertiesMap.remove(71))));

		customAttributes.add(helper.newKeyListAttribute("laptop_cpu_filter", cpu));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_dimensions", propertiesMap.remove(47)));

		customAttributes.add(helper.newKeyValueAttribute("laptop_display_info", propertiesMap.get(1) + " " + laptopDisplayInfo));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_display_resolution", helper.generateDisplayResolution(propertiesMap.remove(1), propertiesMap.remove(70)).trim()));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_display_size", displaySize));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_gpu", propertiesMap.remove(10)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_hdd_info", propertiesMap.get(11)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_hdd_size", hdd));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_optical", propertiesMap.remove(13)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_os_filter", helper.generateOsFilter(propertiesMap.remove(57))));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_processor", propertiesMap.remove(2)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_ram", ram));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_ram_info", propertiesMap.remove(6)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_weight", propertiesMap.remove(48)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_wifi", propertiesMap.remove(15)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_warranty", propertiesMap.remove(49)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_yes_no", helper.generateLaptopYesNo(propertiesMap.remove(17),propertiesMap.remove(51),propertiesMap.remove(11), propertiesMap.remove(74), propertiesMap.remove(38),propertiesMap.remove(40),
				propertiesMap.remove(59),propertiesMap.remove(28),propertiesMap.remove(62),propertiesMap.remove(52),propertiesMap.remove(22), laptopDisplayInfo, propertiesMap.remove(69))));
		
		StringBuilder portsString = new StringBuilder();
		List<Property> productProperties2 = new ArrayList<Property>(properties.values());
		for(Property property : properties.values()){
			if(property.getPropertyId() == 18){
				propertiesMap.remove(18);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				propertiesMap.remove(29);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 41){
				propertiesMap.remove(41);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 42){
				propertiesMap.remove(42);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 53){
				propertiesMap.remove(53);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 61){
				propertiesMap.remove(61);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 62){
				propertiesMap.remove(62);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 24){
				propertiesMap.remove(24);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 28){
				propertiesMap.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
		}
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_ports", portsString.toString()));
		
		StringBuilder otherInfoString = new StringBuilder();
		for(Property property : productProperties2){
			if(properties.containsKey(property.getPropertyId())){
				otherInfoString.append(property.getName() + ": " + property.getValue().get(0).getText());
				if(properties.size() != 1){
					otherInfoString.append("; ");
				}
			}
		}
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_other_info", otherInfoString.toString()));
		
		customAttributes.add(helper.newKeyValueAttribute("short_description", helper.generateShortDescription(displaySize, cpu, ram, hdd, battery)));
		
		magentoProduct.setCustomAttributes(customAttributes);
		
		return magentoProduct;
	}
	
	private MagentoProductRequest generateTablet(SolytronProduct solytronProduct, List<Integer> categories){
		MagentoProductRequest magentoProduct = generateSkeleton(solytronProduct, categories);
		HashMap<Integer, Property> properties = generatePropertiesMap(solytronProduct);
		HashMap<Integer, String> propertiesMap = new HashMap<Integer, String>();
		List<Attribute> customAttributes = new ArrayList<>();

		magentoProduct.setName(helper.trimName(solytronProduct.getName(), 4));
		magentoProduct.setWeight(Double.valueOf(properties.get(48) != null ? properties.get(48).getValue().get(0).getText() : "1"));
		
		for(Property property : properties.values()){
			propertiesMap.put(property.getPropertyId(), property.getValue().get(0).getText());
		}
		
		String displaySize = helper.generateDisplaySize(propertiesMap.remove(1));
		String hdd = helper.generateHddSize(propertiesMap.remove(11));
		String cpu = helper.generateCpuFilter(propertiesMap.remove(55), propertiesMap.get(2));
		String battery = propertiesMap.get(43) != null ? propertiesMap.remove(43) : "" + propertiesMap.get(44) != null ? " " + propertiesMap.remove(44) : "";
		String ram = helper.generateRamFilter(propertiesMap.remove(5));
		
		customAttributes.add(helper.newKeyListAttribute("memory_tablet", helper.generateHddFilter(propertiesMap.get(5))));
		
		customAttributes.add(helper.newKeyValueAttribute("tablet_battery", battery));
		
		customAttributes.add(helper.newKeyValueAttribute("color", helper.generateColorFilter(propertiesMap.remove(35))));
		
		customAttributes.add(helper.newKeyListAttribute("tablet_cpu", helper.generateCpuFilter(propertiesMap.remove(55), propertiesMap.get(2))));
		
		customAttributes.add(helper.newKeyValueAttribute("tablet_dimensions", propertiesMap.remove(47)));
		
		customAttributes.add(helper.newKeyValueAttribute("tablet_display_type", (propertiesMap.get(1) + " " + propertiesMap.get(9)).trim()));
		
		customAttributes.add(helper.newKeyListAttribute("tablet_display", helper.generateDisplayResolution(propertiesMap.get(1), propertiesMap.remove(70)).trim()));
		
		customAttributes.add(helper.newKeyListAttribute("tablet_display_size_filt", helper.generateDisplaySize(propertiesMap.remove(1))));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_gpu", propertiesMap.remove(10)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_hdd_info", propertiesMap.get(11)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_hdd_size", helper.generateHddSize(propertiesMap.get(11))));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_optical", propertiesMap.remove(13)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_os_filter", helper.generateOsFilter(propertiesMap.remove(57))));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_processor", propertiesMap.remove(2)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_ram", ram));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_ram_info", propertiesMap.remove(6)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_weight", propertiesMap.remove(48)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_wifi", propertiesMap.remove(15)));
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_warranty", propertiesMap.remove(49)));
		
		customAttributes.add(helper.newKeyListAttribute("laptop_yes_no", helper.generateLaptopYesNo(propertiesMap.remove(17),propertiesMap.remove(51),propertiesMap.remove(11), propertiesMap.remove(74), propertiesMap.remove(38),propertiesMap.remove(40),
				propertiesMap.remove(59),propertiesMap.remove(28),propertiesMap.remove(62),propertiesMap.remove(52),propertiesMap.remove(22),propertiesMap.remove(9), propertiesMap.remove(69))));
		
		StringBuilder portsString = new StringBuilder();
		List<Property> productProperties2 = new ArrayList<Property>(properties.values());
		for(Property property : properties.values()){
			if(property.getPropertyId() == 18){
				properties.remove(18);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				properties.remove(29);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 41){
				properties.remove(41);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 42){
				properties.remove(42);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 53){
				properties.remove(53);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 61){
				properties.remove(61);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 62){
				properties.remove(62);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 24){
				properties.remove(24);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 28){
				properties.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
			if(property.getPropertyId() == 29){
				properties.remove(28);
				portsString.append(property.getName() + ", ");
				productProperties2.remove(property);
				continue;
			}
		}
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_ports", portsString.toString()));
		
		
		StringBuilder otherInfoString = new StringBuilder();
		for(Property property : productProperties2){
			if(properties.containsKey(property.getPropertyId())){
				otherInfoString.append(property.getName() + ": " + property.getValue().get(0).getText());
				if(properties.size() != 1){
					otherInfoString.append("; ");
				}
			}
		}
		
		customAttributes.add(helper.newKeyValueAttribute("laptop_other_info", otherInfoString.toString()));
		
		customAttributes.add(helper.newKeyValueAttribute("short_description", helper.generateShortDescription(displaySize, cpu, ram, hdd, battery)));
		
		magentoProduct.setCustomAttributes(customAttributes);
		
		return magentoProduct;
	}
	
	private MagentoProductRequest generateAccessory(SolytronProduct solytronProduct, List<Integer> categories){
		MagentoProductRequest magentoProduct = generateSkeleton(solytronProduct, categories);
		magentoProduct.setName(helper.trimName(solytronProduct.getName(), 6));
		HashMap<Integer, Property> properties = generatePropertiesMap(solytronProduct);
		
		return magentoProduct;
	}
	
	private HashMap<Integer, Property> generatePropertiesMap(SolytronProduct product){
		HashMap<Integer, Property> properties = new HashMap<Integer, Property>();
		KeyValueAttribute productGroup = new KeyValueAttribute();
		productGroup.setAttributeCode("product_group");
		if(product.getProperties().size() > 0){
			for(PropertyGroup g : product.getProperties()){
				productGroup.setValue(g.getProductGroupName() != null ? g.getProductGroupName() : "");
				if(g.getList() != null){
					for(Property property : g.getList()){
						properties.put(property.getPropertyId(), property);
					}
				}
			}
		}
		return properties;
	}
	
	private MagentoProductRequest generateSkeleton(SolytronProduct product, List<Integer> categories){
		MagentoProductRequest magentoProduct = new MagentoProductRequest();
		List<Attribute> customAttributes = new ArrayList<>();
		magentoProduct.setStatus(1);
		magentoProduct.setVisibility(4);
		String sku = product.getCodeId();
		sku = sku.replaceAll("/", "");
		sku = sku.replace((char) 92, (char) 0);
		sku = sku.replace((char) 34, (char) 0);
		magentoProduct.setSku(sku);
		magentoProduct.setPrice(generatePrice(product));
		magentoProduct.setTypeId("simple");
		magentoProduct.setName(product.getName());
		
		if(!magentoProduct.getName().toLowerCase().contains(product.getVendor().toLowerCase())){
			magentoProduct.setName(product.getVendor() + " " + magentoProduct.getName());
		}
		
		magentoProduct.setExtensionAttributes(new ExtensionAttributeRequest());
		magentoProduct.getExtensionAttributes().setItem(generateStockInfo(product.getStockInfoValue()));
		
		String brand;
		if(product.getVendor() == null){
			brand = helper.generateBrand(magentoProduct.getName());
		}
		else{
			brand = helper.generateBrand(product.getVendor());
		}
		customAttributes.add(helper.newKeyValueAttribute("manufacturer", brand));
		
		customAttributes.add(helper.newKeyValueAttribute("ean", product.getEan()));
		
		List<String> stringCategories = new ArrayList<>();
		for(Integer cat : categories){
			stringCategories.add(String.valueOf(cat));
		}
		
		customAttributes.add(helper.newKeyListAttribute("categories", stringCategories));
		
		magentoProduct.setCustomAttributes(customAttributes);
		
		return magentoProduct;
	}
	
	public MagentoStockItemRequest generateStockInfo(String stockInfo) {
		
		MagentoStockItemRequest magentoStockItem = new MagentoStockItemRequest();
		
		if(stockInfo != null){
			if(stockInfo.contains("OnHand")){
				magentoStockItem.setStock(true);
				magentoStockItem.setQty(5);
			}
			else if(stockInfo.contains("Minimum")){
				magentoStockItem.setStock(true);
				magentoStockItem.setQty(2);
			}
			else{
				magentoStockItem.setStock(false);
				magentoStockItem.setQty(0);
			}
		}
		
		else{
			magentoStockItem.setStock(true);
			magentoStockItem.setQty(2);
		}
		
		return magentoStockItem;
	}
	
	public double generatePrice(SolytronProduct product){
		Double price;
		if(product.getPriceEndUser() != null){
			if(product.getPriceEndUser().getCurrency().equals("BGN")){
				price = Double.valueOf(product.getPriceEndUser().getText());
			}
			else if(product.getPriceEndUser().getCurrency().equals("EUR")){
				price = Double.valueOf(product.getPriceEndUser().getText()) * 1.96;
			}
			else {
				price = 50.0;
			}
		}
		else if(product.getPrice() != null){
			if(product.getPrice().getCurrency().equals("BGN")){
				price = Double.valueOf(product.getPrice().getText()) * 1.2 * 1.3;
			}
			else if(product.getPrice().getCurrency().equals("EUR")){
				price = Integer.valueOf(product.getPrice().getText()) * 1.96 * 1.2 * 1.3;
			}
			else {
				price = 50.0;
			}
		}
		else {
			price = 50.0;
		}
		if(!TAX_INCLUDED){
			price = price / 1.2;
		}
		return price;
	}
}
