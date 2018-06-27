package bg.premiummobile.productimporter.magento.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryLink {

	@JsonProperty("position")
	private int position;
	@JsonProperty("category_id")
	private int categoryId;
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
