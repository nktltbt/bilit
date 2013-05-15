package com.applifit.bilifit.server.formulaires.DAO;

import java.util.ArrayList;
import java.util.List;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.DAO.CompteDaoImpl;
import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.Element_valeur;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class Element_valeurDaoImpl implements Element_valeurDao{
	
	static {  
        ObjectifyService.register(Element_valeur.class);  
    }

	@Override
	public Element_valeur ajouterValeur(String valeur, long element, long compte , String date, int indice) {
		Objectify ofy = ObjectifyService.begin();
		Element_valeur ev = new Element_valeur(valeur, new Key<Element>(Element.class, element) ,new Key<Compte>(Compte.class, compte) , date, indice);
		ofy.put(ev);
		return ev;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList getDistinctCompteIndice(ArrayList<Long> e) {
		
		Objectify ofy = ObjectifyService.begin();
		List<Key<Element>> elements = new ArrayList();
		for(int i = 0; i<e.size(); i++)
			elements.add(new Key<Element>(Element.class, e.get(i)));
		List<Element_valeur> lev = ofy.query(Element_valeur.class).order("-date").filter("element in ", elements).list();
		ArrayList comptes = new ArrayList();
		CompteDaoImpl cdao = new CompteDaoImpl();
		ArrayList indices = new ArrayList();
		for(int i = 0 ; i<lev.size(); i++){
			if(!indices.contains(lev.get(i).getIndice())){
				ArrayList compte = new ArrayList();
				indices.add(lev.get(i).getIndice());
				Compte c = cdao.getCompteById(lev.get(i).getCompte().getId());
				compte.add(c.getId());
				compte.add(lev.get(i).getIndice());
				compte.add(c.getNom());
				compte.add(c.getPrenom());
				compte.add(lev.get(i).getDate());
				comptes.add(compte);
			}
		}
		
		return comptes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ArrayList getDistinctCompteIndice(ArrayList<Long> e , long cid) {
		
		Objectify ofy = ObjectifyService.begin();
		List<Key<Element>> elements = new ArrayList();
		CompteDaoImpl cdao = new CompteDaoImpl();
		for(int i = 0; i<e.size(); i++)
			elements.add(new Key<Element>(Element.class, e.get(i)));
		List<Element_valeur> lev = ofy.query(Element_valeur.class).filter("element in ", elements).filter("compte", cdao.getCompteById(cid)).list();
		ArrayList comptes = new ArrayList();
		ArrayList indices = new ArrayList();
		for(int i = 0 ; i<lev.size(); i++){
			if(!indices.contains(lev.get(i).getIndice())){
				ArrayList compte = new ArrayList();
				indices.add(lev.get(i).getIndice());
				Compte c = cdao.getCompteById(lev.get(i).getCompte().getId());
				compte.add(c.getId());
				compte.add(lev.get(i).getIndice());
				compte.add(c.getNom());
				compte.add(c.getPrenom());
				compte.add(lev.get(i).getDate());
				comptes.add(compte);
			}
		}
		
		return comptes;
	}

	@Override
	public Element_valeur getValue(Long element, int indice) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Element_valeur.class).filter("element", new Key<Element>(Element.class, element)).filter("indice", indice).get();
	}

	@Override
	public List<Element_valeur> getAllValues() {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Element_valeur.class).list();
	}

	@Override
	public List<Element_valeur> getValuesByIndice(int indice) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Element_valeur.class).filter("indice", indice).list();
	}

	@Override
	public void modifierValeur(String valeur, long element, int indice) {
		Objectify ofy = ObjectifyService.begin();
		Element_valeur value = getValue(element, indice);
		value.setValeur(valeur);
		ofy.put(value);
	}

}
