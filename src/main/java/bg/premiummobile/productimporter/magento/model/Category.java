package bg.premiummobile.productimporter.magento.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Category {
	
	private int id;
	@JsonProperty("parent_id")
	private int parentId;
	private String name;
	@JsonProperty("is_active")
	private boolean active;
	private int position;
	private int level;
	@JsonProperty("product_count")
	private int productsCount;
	@JsonProperty("children_data")
	private List<Category> childrenData;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getProductsCount() {
		return productsCount;
	}
	public void setProductsCount(int productsCount) {
		this.productsCount = productsCount;
	}
	public List<Category> getChildrenData() {
		return childrenData;
	}
	public void setChildrenData(List<Category> childrenData) {
		this.childrenData = childrenData;
	}
	
}
