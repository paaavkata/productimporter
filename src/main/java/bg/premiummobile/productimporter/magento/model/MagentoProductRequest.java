package bg.premiummobile.productimporter.magento.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MagentoProductRequest implements MagentoProduct{

	private Integer id;
	private String sku;
	private String name;
	@JsonProperty("attribute_set_id")
	private Integer attributeSetId;
	private Double price;
	@JsonProperty("special_price")
	private Double specialPrice;
	private Integer status;
	private Integer visibility;
	@JsonProperty("type_id")
	private String typeId;
	private Double weight;
	@JsonProperty("extension_attributes")
	private ExtensionAttributeRequest extensionAttributes;
	@JsonProperty("product_links")
	private List<ProductLink> productLinks;
	private List<Option> options;
	@JsonProperty("media_gallery_entries")
	private List<MediaGalleryEntry> mediaGalleryEntries;
	@JsonProperty("tier_prices")
	private List<TierPrice> tierPrices;
	@JsonProperty("custom_attributes")
	private List<Attribute> customAttributes;
	
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getSpecialPrice() {
		return specialPrice;
	}
	public void setSpecialPrice(Double specialPrice) {
		this.specialPrice = specialPrice;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAttributeSetId() {
		return attributeSetId;
	}
	public void setAttributeSetId(Integer attributeSetId) {
		this.attributeSetId = attributeSetId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public ExtensionAttributeRequest getExtensionAttributes() {
		return extensionAttributes;
	}
	public void setExtensionAttributes(ExtensionAttributeRequest extensionAttributes) {
		this.extensionAttributes = extensionAttributes;
	}
	public List<ProductLink> getProductLinks() {
		return productLinks;
	}
	public void setProductLinks(List<ProductLink> productLinks) {
		this.productLinks = productLinks;
	}
	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public List<MediaGalleryEntry> getMediaGalleryEntries() {
		return mediaGalleryEntries;
	}
	public void setMediaGalleryEntries(List<MediaGalleryEntry> mediaGalleryEntries) {
		this.mediaGalleryEntries = mediaGalleryEntries;
	}
	public List<TierPrice> getTierPrices() {
		return tierPrices;
	}
	public void setTierPrices(List<TierPrice> tierPrices) {
		this.tierPrices = tierPrices;
	}
	public List<Attribute> getCustomAttributes() {
		return customAttributes;
	}
	public void setCustomAttributes(List<Attribute> customAttributes) {
		this.customAttributes = customAttributes;
	}
	
}
