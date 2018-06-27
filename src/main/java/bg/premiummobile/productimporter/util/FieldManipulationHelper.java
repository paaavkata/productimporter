package bg.premiummobile.productimporter.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bg.premiummobile.productimporter.configuration.ConfigurationReader;
import bg.premiummobile.productimporter.magento.model.Attribute;
import bg.premiummobile.productimporter.magento.model.KeyListAttribute;
import bg.premiummobile.productimporter.magento.model.KeyValueAttribute;
import bg.premiummobile.productimporter.solytron.model.Property;


@Component
public class FieldManipulationHelper {

	private ConfigurationReader reader;
	
	private HashMap<String, String> colors;
	
	private HashMap<String, String> brands;
	
	private HashMap<String, String> magentoAttributesReversed;
	
	private HashMap<String, String> magentoAttributes;
	
	@Autowired
	public FieldManipulationHelper(ConfigurationReader reader){
		this.reader = reader;
		this.brands = reader.getBrands();
		this.colors = reader.getColors();
		this.magentoAttributesReversed = reader.getMagentoAttributesReversed();
		this.magentoAttributes = reader.getMagentoAttributes();
	}
	
	
	public KeyValueAttribute newKeyValueAttribute(String name, String value){
		KeyValueAttribute attribute = new KeyValueAttribute();
		attribute.setAttributeCode(name);
		attribute.setValue(value);
		return attribute;
	}
	
	public KeyListAttribute newKeyListAttribute(String name, List<String> values){
		KeyListAttribute attribute = new KeyListAttribute();
		attribute.setAttributeCode(name);
		attribute.setValue(values);
		return attribute;
	}
	
	public KeyListAttribute newKeyListAttribute(String name, String value){
		KeyListAttribute attribute = new KeyListAttribute();
		attribute.setAttributeCode(name);
		List<String> values = new ArrayList<>();
		values.add(value);
		attribute.setValue(values);
		return attribute;
	}
	
	public String generateMetaTitle(String name, boolean leasable){
		StringBuilder st = new StringBuilder();
		st.append(name);
		if(leasable){
			st.append(" на топ цена и на изплащане от Примиъм Мобайл ЕООД - ревю, мнения, характеристики");
		}
		else{
			st.append(" цена без конкуренция от Примиъм Мобайл ЕООД - ревю, мнения, характеристики");
		}
		return st.toString();
	}
	
	public String generateMetaDescription(String name, boolean leasable){
		StringBuilder st = new StringBuilder();
		st.append("Купете днес ");
		st.append(name);
		if(leasable){
			st.append(" на изплащане и на супер цена с минимално оскъпяване и светкавично одобрение от PremiumMobile.bg. Бърза доставка на следващия ден и любезно обслужване.");
		}
		else{
			st.append(" на страхотна цена с безплатна доставка от PremiumMobile.bg. Бърза доставка на следващия ден и любезно обслужване.");
		}
		return st.toString();
	}
	
	public String generateUrl(String name, boolean leasable){
		if(leasable){
			return name + "-cena-na-izplashtane";
		}
		else{
			return name + "-cena-bulgaria";
		}
	}
	
	public String generateShortDescription(String displaySize, String cpu, String ram, String hdd, String battery) {
		StringBuilder st = new StringBuilder();
		st.append("<ul class=\"short-description-list smartphone\"><div class=\"row\"><div class=\"col-md-2 col-md-offset-1\"><li class=\"display-size\">");
		st.append(magentoAttributes.get(displaySize));
		st.append("</li></div><div class=\"col-md-2\"><li class=\"processor\">");
		st.append(magentoAttributes.get(cpu));
		st.append("</li></div><div class=\"col-md-2\"><li class=\"memory\">");
		st.append(magentoAttributes.get(ram));
		st.append("</li></div><div class=\"col-md-2\"><li class=\"hdd\">");
		st.append(hdd);
		st.append("</li></div><div class=\"col-md-2\"><li class=\"battery\">");
		st.append(battery);
		st.append("</li></div></div></ul>");
		return st.toString();
	}
	
	public String trimName(String name, int spaces) {
		StringBuilder st = new StringBuilder();
		name = name.replace("WEEKLY", "");
		name = name.replace("PROMO BUNDLE", "");
		name = name.replace("NEW!", "");
		name = name.replace("NB", "");
		name = name.replace("Преносим компютър", "");
		name = name.replace("Tablet","");
		name = name.replace("Notebook","");
		name = name.replace("Mobile workstation","");
		name = name.replace("Ultrabook", "");
		name = name.replace("РАЗПРОДАЖБА!", "");
		name = name.replace("PROMO!", "");
		name = name.replace("&quot", "");
		name = name.replace("\"", "");
		name = name.trim();
		int counter = 0;
		for(int i = 0; i < name.length(); i++){
			if(name.charAt(i) == ',' || name.charAt(i) == ';' || name.charAt(i) == (char) 047 || name.charAt(i) == '/'){
				break;
			}
			if(name.charAt(i) == ' '){
				counter++;
			}
			if(counter > spaces){
				break;
			}
			st.append(name.charAt(i));
		}
		return st.toString().trim();
	}


	public List<Attribute> generateSimpleAttributes(HashMap<Integer, Property> list) {
		KeyValueAttribute description = new KeyValueAttribute();
		description.setAttributeCode("description");
		StringBuilder st = new StringBuilder();
		st.append("<h4>");
		for(Property property : list.values()){
			st.append(property.getName() + ": " + property.getValue().get(0).getText() + "<br>");
		}
		st.append("</h4>");
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		description.setValue(st.toString());
		attributes.add(description);
		return attributes;
	}
	
	public String generateBrand(String brand){
		if(brand != null && !brand.equals("")){
			for(String string : brands.keySet()){
				if(this.contains(string, brand)){
					return brands.get(string);
				}
			}
		}
		return brands.get("Други");
	}
	
	public String generateHddSize(String string) {
		StringBuilder st = new StringBuilder();
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == ' ' || string.charAt(i) == 'T' || string.charAt(i) == 'G'){
				break;
			}
			st.append(string.charAt(i));
		}
		int gb = Integer.parseInt(st.toString());
		
		if(gb <= 5){
			return gb + " TB";
		}
		else{
			return gb + " GB";
		}
	}

	public String generateOsFilter(String string) {
		if(string != null && !string.equals("")){
			if(this.contains("Windows", string)){
				return magentoAttributesReversed.get("Windows");
			}
			if(this.contains("DOS", string) || this.contains("No OS", string)){
				return magentoAttributesReversed.get("MS-Dos");
			}
			if(this.contains("Mac", string)){
				return magentoAttributesReversed.get("Mac OS");
			}
			if(this.contains("Linux", string)){
				return magentoAttributesReversed.get("Linux");
			}
		}
		return magentoAttributesReversed.get("MS-Dos");
	}
	
	public String generateRamFilter(String memory){
		if(memory != null && !memory.equals("")){
			memory = memory.trim();
			StringBuilder st = new StringBuilder();
			for(int i = 0; i < memory.length(); i++) {
				if(memory.charAt(i) == ' ' || memory.charAt(i) == 'G') {
					break;
				}
				if(memory.charAt(i) == 'x'){
					String multiplyier = String.valueOf(memory.charAt(i-1));
					String ram = new String();
					for(int j = i + 1; j < memory.length(); j++) {
						if(memory.charAt(j) > 57 || memory.charAt(j) < 48){
							break;
						}
						ram = ram + memory.charAt(j);
					}
					return Integer.valueOf(multiplyier)*Integer.valueOf(ram) + " GB";
				}
				st.append(memory.charAt(i));
			}
			return magentoAttributesReversed.get(st.toString() + " GB");
		}
		return magentoAttributesReversed.get("4 GB");
	}

	public String generateColorFilter(String color) {
		if(color != null && !color.equals("")){
			for(String string : colors.keySet()){
				if(this.contains(string, color)){
					return colors.get(string);
				}
			}
		}
		return colors.get("Black");
	}
	
	public boolean contains(String string, String contains){
		return contains.toLowerCase().contains(string.toLowerCase());
	}

	public String generateCpuFilter(String cpuFilter, String cpuFilter2) {
		if(cpuFilter == null || cpuFilter.equals("")){
			cpuFilter = cpuFilter2;
		}
		if(this.contains("i7", cpuFilter)){
			return magentoAttributesReversed.get("Intel Core i7");
		}
		if(this.contains("i5", cpuFilter)){
			return magentoAttributesReversed.get("Intel Core i5");
		}
		if(this.contains("i3", cpuFilter)){
			return magentoAttributesReversed.get("Intel Core i3");
		}
		if(this.contains("entium", cpuFilter)){
			return magentoAttributesReversed.get("Intel Pentium");
		}
		if(this.contains("AMD", cpuFilter)){
			return magentoAttributesReversed.get("AMD");
		}
		if(this.contains("eleron", cpuFilter)){
			return magentoAttributesReversed.get("Intel Celeron");
		}
		if(this.contains("eon", cpuFilter)){
			return magentoAttributesReversed.get("Intel Xeon");
		}
		if(this.contains("tom", cpuFilter)){
			return magentoAttributesReversed.get("Intel Atom");
		}
		return magentoAttributesReversed.get("Intel Core i3");
	}

	public List<String> generateLaptopYesNo(String bluetooth, String camera, String ssd, String ssd2, String reader, String fingerprint, String hdmi, 
			String hdmi2, String onelink, String usb3, String rj45, String sensorscreen, String usbc){
		List<String> list = new ArrayList<String>();
		if(bluetooth != null && !bluetooth.contains("No")){
			list.add("Bluetooth");
		}
		if(camera != null && !camera.contains("No")){
			list.add("Камера");
		}
		if(ssd != null && (ssd.contains("SSD") || ssd.contains("ssd") || ssd.contains("Ssd"))){
			list.add("SSD");
		}
		else if(ssd2 != null && (ssd2.contains("SSD") || ssd2.contains("ssd") || ssd2.contains("Ssd"))){
			list.add("SSD");
		}
		if(reader != null && !reader.contains("No")){
			list.add("Четец за карти");
		}
		if(fingerprint != null && !fingerprint.contains("No")){
			list.add("Сензор за отпечатък");
		}
		if(hdmi != null && !hdmi.contains("No")){
			list.add("HDMI порт");
		}
		else if(hdmi2 != null && !hdmi2.contains("No")){
			list.add("HDMI порт");
		}
		if(onelink != null && !onelink.contains("No")){
			list.add("OneLink порт");
		}
		if(usb3 != null && !usb3.equals("")){
			list.add("USB 3.0");
		}
		if(rj45 != null && !rj45.contains("No")){
			list.add("RJ-45 порт");
		}
		if(sensorscreen != null && sensorscreen.contains("ouch")){
			list.add("Сензорен екран");
		}
		if(usbc != null){
			if(Integer.valueOf(usbc) > 0){
				list.add("USB Type C");
			}
		}
		ArrayList<String> newList = new ArrayList<String>();
		for(String name : list){
			newList.add(magentoAttributesReversed.get(name));
		}
		return newList;
	}
	public String generateDisplaySize(String string) {
		if(string != null && !string.equals("")){
			if(string.contains("15.6")){
				return magentoAttributesReversed.get("15.6 inch (39.62 cm)");
			}
			if(string.contains("14")){
				return magentoAttributesReversed.get("14.0 inch (35.56 cm)");
			}
			if(string.contains("17")){
				return magentoAttributesReversed.get("17.3 inch (43.94 cm)");
			}
			if(string.contains("12.0")){
				return magentoAttributesReversed.get("12.0 inch (30.48 cm)");
			}
			if(string.contains("13.3")){
				return magentoAttributesReversed.get("13.3 inch (33.78 cm)");
			}
			if(string.contains("13.0")){
				return magentoAttributesReversed.get("13.0 inch (33.02 cm)");
			}
			if(string.contains("11.6")){
				return magentoAttributesReversed.get("11.6 inch (29.46 cm)");
			}
			if(string.contains("15.4")){
				return magentoAttributesReversed.get("15.4 inch (39.12 cm)");
			}
			return magentoAttributesReversed.get("15.6 inch (39.62 cm)");
		}
		return null;
	}

	public String generateDisplayResolution(String string, String string1) {
		String string2 = string1 != null && !string1.equals("") ? string1 : string;
		if(string2 != null && !string2.equals("")){
			if(string2.contains("1366") && string2.contains("768")){
				return magentoAttributesReversed.get("1366x768");
			}
			if(string2.contains("1280") && string2.contains("720")){
				return magentoAttributesReversed.get("HD (1280x720)");
			}
			if(string2.contains("1600") && string2.contains("1080")){
				return magentoAttributesReversed.get("HD+ (1600x1080)");
			}
			if(string2.contains("1920") && string2.contains("1080")){
				return magentoAttributesReversed.get("Full HD (1920x1080)");
			}
			if(string2.contains("2560") && string2.contains("1440")){
				return magentoAttributesReversed.get("Quad HD (2560x1440)");
			}
			if(string2.contains("3200") && string2.contains("1800")){
				return magentoAttributesReversed.get("Quad HD+ (3200x1800)");
			}
			if(string2.contains("3640") && string2.contains("2160")){
				return magentoAttributesReversed.get("Ultra HD - 4K (3640x2160)");
			}
			if(string2.contains("2304") && string2.contains("1440")){
				return magentoAttributesReversed.get("Retina (2304x1440)");
			}
			if(string2.contains("2880") && string2.contains("1800")){
				return magentoAttributesReversed.get("Retina (2880x1800)");
			}
		}
		return magentoAttributesReversed.get("1366x768");
	}

	public String generateHddFilter(String string) {
		StringBuilder st = new StringBuilder();
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == ' ' || string.charAt(i) == 'T' || string.charAt(i) == 'G'){
				break;
			}
			st.append(string.charAt(i));
		}
		int gb = Integer.parseInt(st.toString());
		if(gb <= 200){
			if(gb <= 5){
				return magentoAttributesReversed.get("1001-1500 GB");
			}
			else{
				return magentoAttributesReversed.get("под 200 GB");
			}
		}
		if(gb > 200 && gb <= 400){
			return magentoAttributesReversed.get("200-400 GB");
		}
		if(gb > 400 && gb <= 600){
			return magentoAttributesReversed.get("401-600 GB");
			
		}
		if(gb > 600 && gb <= 800){
			return magentoAttributesReversed.get("601-800 GB");
		}
		if(gb > 800 && gb <= 1000){
			return magentoAttributesReversed.get("801-1000 GB");
		}
		if(gb > 1000 && gb <= 1500){
			return magentoAttributesReversed.get("1001-1500 GB");
		}
		if(gb > 1500){
			return magentoAttributesReversed.get("над 1500 GB");
		}
		return magentoAttributesReversed.get("401-600 GB");
	}

}
