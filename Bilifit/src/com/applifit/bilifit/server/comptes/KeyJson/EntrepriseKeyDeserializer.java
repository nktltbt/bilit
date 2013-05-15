package com.applifit.bilifit.server.comptes.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

import com.applifit.bilifit.server.comptes.Entreprise;
import com.googlecode.objectify.Key;

public class EntrepriseKeyDeserializer extends JsonDeserializer<Key<Entreprise>>{
	
	static final TypeReference<Entreprise> type = new TypeReference<Entreprise>() {};

	@Override
	public Key<Entreprise> deserialize(JsonParser arg0,
			DeserializationContext arg1) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		
		Entreprise entreprise = arg0.readValueAs(type);
	    Key<Entreprise> l = new Key<Entreprise>(Entreprise.class, entreprise.getId());
		return l;
	}

}
