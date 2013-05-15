package com.applifit.bilifit.server.formulaires.DAO;

import java.util.List;

import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.Formulaire;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ElementDaoImpl implements ElementDao{

	static {  
        ObjectifyService.register(Element.class);  
    }

	@Override
	public Element ajouterElement(Element element) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(element);
		return element;
	}
	
	@Override
	public List<Element> getElements() {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Element.class).order("ordre").list();	
	}

	@Override
	public Element getElementByParams(int position_x, int position_y, Long form) {
		Objectify ofy = ObjectifyService.begin();
		FormulaireDaoImpl fdao= new FormulaireDaoImpl();
		Formulaire f = fdao.getFormulaireById(form);
		return ofy.query(Element.class).filter("position_x", position_x).filter("position_y", position_y).filter("formulaire", f).get();
	}
	
	@Override
	public List<Element> getElementsByForm(long formulaire) {
		Objectify ofy = ObjectifyService.begin();
		FormulaireDaoImpl fdao= new FormulaireDaoImpl();
		Formulaire f = fdao.getFormulaireById(formulaire);
		return ofy.query(Element.class).filter("formulaire", f).order("ordre").list();
	}
	
	@Override
	public Element getElementById(long id) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Element.class, id);
	}
}
