package com.applifit.bilifit.server.comptes.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.applifit.bilifit.server.comptes.Compte;
import com.googlecode.objectify.Key;

public class CompteKeySerializer  extends JsonSerializer<Key<Compte>>{

	@Override
	public void serialize(Key<Compte> arg0, JsonGenerator arg1,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		arg1.writeString(""+arg0.getId());
		
		
		
	}
}
