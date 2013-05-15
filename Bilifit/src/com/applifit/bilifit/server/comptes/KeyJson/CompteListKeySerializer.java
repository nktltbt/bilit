package com.applifit.bilifit.server.comptes.KeyJson;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.applifit.bilifit.server.comptes.Compte;
import com.googlecode.objectify.Key;

public class CompteListKeySerializer extends JsonSerializer<List<Key<Compte>>>{

	@Override
	public void serialize(List <Key<Compte>> arg0, JsonGenerator arg1,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		String comptes ="";
		for(int i = 0; i<arg0.size(); i++)
			comptes +=arg0.get(i).getId()+",";
		arg1.writeString(comptes);
		
		
		
	}
}
