package com.applifit.bilifit.server.formulaires.KeyJson;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.applifit.bilifit.server.formulaires.Formulaire;
import com.googlecode.objectify.Key;

public class FormulaireListKeySerializer extends JsonSerializer<List<Key<Formulaire>>>{

	@Override
	public void serialize(List <Key<Formulaire>> arg0, JsonGenerator arg1,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		String formulaires ="";
		for(int i = 0; i<arg0.size(); i++)
			formulaires +=arg0.get(i).getId()+",";
		arg1.writeString(formulaires);
		
		
		
	}

	
}
