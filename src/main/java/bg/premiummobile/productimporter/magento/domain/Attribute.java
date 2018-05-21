package bg.premiummobile.productimporter.magento.domain;

public abstract class Attribute {

	abstract String getAttributeCode();
	abstract void setAttributeCode(String code);
	abstract Object getValue();
	abstract void setValue(Object value);
}
