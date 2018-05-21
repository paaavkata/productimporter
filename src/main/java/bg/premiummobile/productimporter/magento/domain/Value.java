package bg.premiummobile.productimporter.magento.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {

	@JsonProperty("value_index")
	private int valueIndex;

	public int getValueIndex() {
		return valueIndex;
	}

	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}
	
	
}
