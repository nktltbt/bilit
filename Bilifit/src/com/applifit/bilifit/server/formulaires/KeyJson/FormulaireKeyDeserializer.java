package com.applifit.bilifit.server.formulaires.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.TypeReference;

import com.applifit.bilifit.server.formulaires.Formulaire;
import com.googlecode.objectify.Key;

public class FormulaireKeyDeserializer extends JsonDeserializer<Key<Formulaire>>{
	
	static final TypeReference<Formulaire> type = new TypeReference<Formulaire>() {};

	@Override
	public Key<Formulaire> deserialize(JsonParser arg0,
			DeserializationContext arg1) throws IOException,
			JsonProcessingException {
		// TODO Auto-generated method stub
		
		Formulaire formulaire = arg0.readValueAs(type);
	    Key<Formulaire> l = new Key<Formulaire>(Formulaire.class, formulaire.getId());
		return l;
	}
}
