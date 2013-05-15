package com.applifit.bilifit.server.comptes.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

import com.applifit.bilifit.server.comptes.Compte;
import com.googlecode.objectify.Key;

public class CompteKeyDeserializer extends JsonDeserializer<Key<Compte>>{

	static final TypeReference<Compte> type = new TypeReference<Compte>() {};

	@Override
	public Key<Compte> deserialize(JsonParser arg0,
			DeserializationContext arg1) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		
		Compte compte = arg0.readValueAs(type);
	    Key<Compte> l = new Key<Compte>(Compte.class, compte.getId());
		return l;
	}
}
