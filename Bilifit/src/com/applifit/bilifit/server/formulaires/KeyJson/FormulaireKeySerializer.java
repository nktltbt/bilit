package com.applifit.bilifit.server.formulaires.KeyJson;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.applifit.bilifit.server.formulaires.Formulaire;
import com.googlecode.objectify.Key;

public class FormulaireKeySerializer extends JsonSerializer<Key<Formulaire>>{

	@Override
	public void serialize(Key<Formulaire> arg0, JsonGenerator arg1,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		arg1.writeString(""+arg0.getId());
		
		
		
	}
}
