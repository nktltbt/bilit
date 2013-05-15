package com.applifit.bilifit.server.comptes.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.applifit.bilifit.server.comptes.Entreprise;
import com.googlecode.objectify.Key;

public class EntrepriseKeySerializer extends JsonSerializer<Key<Entreprise>>{

	@Override
	public void serialize(Key<Entreprise> arg0, JsonGenerator arg1,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		arg1.writeString(""+arg0.getId());
		
		
		
	}
	

}
