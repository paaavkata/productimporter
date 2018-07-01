package bg.premiummobile.productimporter.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import bg.premiummobile.productimporter.magento.model.Attribute;
import bg.premiummobile.productimporter.magento.model.KeyListAttribute;
import bg.premiummobile.productimporter.magento.model.KeyValueAttribute;

public class MagentoAttributeDeserializer extends JsonDeserializer<Attribute> {
    
   @Override
	public Attribute deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		JsonNode root = oc.readTree(jp);
		Object value = root.get("value");

		if (value.getClass().isInstance(List.class)) {
			KeyListAttribute attribute = new KeyListAttribute();
			attribute.setAttributeCode(root.get("attribute_code").asText());
			attribute.setValue(value);
			return attribute;
		} else {
			KeyValueAttribute attribute = new KeyValueAttribute();
			attribute.setAttributeCode(root.get("attribute_code").asText());
			attribute.setValue(root.get("value").deepCopy());
			return attribute;
		}
	}
}