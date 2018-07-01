package bg.premiummobile.productimporter.magento.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import bg.premiummobile.productimporter.util.MagentoAttributeDeserializer;


@JsonDeserialize(using = MagentoAttributeDeserializer.class)
public abstract class Attribute {

	public abstract String getAttributeCode();
	public abstract void setAttributeCode(String code);
	public abstract Object getValue();
	public abstract void setValue(Object value);
}
