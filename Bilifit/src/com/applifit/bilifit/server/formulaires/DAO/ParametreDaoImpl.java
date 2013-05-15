package com.applifit.bilifit.server.formulaires.DAO;

import java.util.List;

import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.Parametre;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ParametreDaoImpl implements ParametreDao{
	
	static {  
        ObjectifyService.register(Parametre.class);  
    }

	@Override
	public Parametre getParametreById(Long id) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Parametre.class, id);
	}

	@Override
	public Parametre ajouterParametre(Parametre param) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(param);
		return param;
	}

	@Override
	public List<Parametre> getParametres() {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Parametre.class).list();
	}

	@Override
	public List<Parametre> getParametresByElement(Long element) {
		Objectify ofy = ObjectifyService.begin();
		ElementDaoImpl edao= new ElementDaoImpl();
		Element e = edao.getElementById(element);
		return ofy.query(Parametre.class).filter("element", e).list();
	} 
	
	
}
