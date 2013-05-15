package com.applifit.bilifit.server.formulaires.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

import com.applifit.bilifit.server.formulaires.Element;
import com.googlecode.objectify.Key;

public class ElementKeyDeserializer extends JsonDeserializer<Key<Element>>{
	
	static final TypeReference<Element> type = new TypeReference<Element>() {};

	@Override
	public Key<Element> deserialize(JsonParser arg0,
			DeserializationContext arg1) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		
		Element element = arg0.readValueAs(type);
	    Key<Element> l = new Key<Element>(Element.class, element.getId());
		return l;
	}
}
