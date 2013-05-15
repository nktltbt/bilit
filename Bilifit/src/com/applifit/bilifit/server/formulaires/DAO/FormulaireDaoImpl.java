package com.applifit.bilifit.server.formulaires.DAO;

import java.util.ArrayList;
import java.util.List;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;
import com.applifit.bilifit.server.comptes.DAO.CompteDaoImpl;
import com.applifit.bilifit.server.comptes.DAO.EntrepriseDaoImpl;
import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.Formulaire;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;


public class FormulaireDaoImpl implements FormulaireDao{

	static {  
        ObjectifyService.register(Formulaire.class);  
    }

	@Override
	public Formulaire getFormulaireById(Long id) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Formulaire.class, id);
	}

	@Override
	public Formulaire ajouterFormulaire(Formulaire formulaire) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(formulaire);
		return formulaire;
	}

	@Override
	public Formulaire getFormParNom(String nom) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Formulaire.class).filter("nom", nom).filter("etat !=", 0).get();
	}

	@Override
	public List<Formulaire> getFormulaires() {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Formulaire.class).filter("etat !=", 0).list();
	} 
	
	@Override
	public int getCountFormulaireByEntreprise(Entreprise entreprise) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Formulaire.class).filter("entreprise", entreprise).filter("etat !=", 0).count();
	}
	
	@Override
	public Boolean getEtatFormulaire(long idEntreprise) {
		EntrepriseDaoImpl Edao = new EntrepriseDaoImpl();
		Entreprise entreprise = Edao.getEntrepriseById(idEntreprise);
		int nbrFormulaire = getCountFormulaireByEntreprise(entreprise);
		
		if(nbrFormulaire < entreprise.getQuota_formulaire())
			return true;
		
		return false;
	}
	
	@Override
	public List<Formulaire> getFormulairesByEntreprise(long entreprise) {
		Objectify ofy = ObjectifyService.begin();
		EntrepriseDaoImpl Edao = new EntrepriseDaoImpl();
		Entreprise e = Edao.getEntrepriseById(entreprise);
		return ofy.query(Formulaire.class).filter("entreprise", e).filter("etat !=", 0).list();
	}

	
	/**
	 * Récuperer la liste des formulaire pour la recherche (nom , commentaire)
	 */
	@Override
	public List<Formulaire> getFormulairesByParam(long entreprise, String nom, String date) {
		Objectify ofy = ObjectifyService.begin();
		EntrepriseDaoImpl Edao = new EntrepriseDaoImpl();
		Entreprise e = Edao.getEntrepriseById(entreprise);
		Query<Formulaire> query = ofy.query(Formulaire.class).filter("entreprise", e);
		
		if(!date.equals("-"))
		{
			query = query.filter("date_creation",date);
		}
		
		//Query<Formulaire> queryBis = query.clone();
		query= query.filter("etat !=", 0);
		
		List<Formulaire> forms = query.list();
		List<Formulaire> results = new ArrayList<Formulaire>();
		
		if(!nom.equals("-"))
		{
			for(int i = 0; i< forms.size(); i++){
				if(forms.get(i).getNom().contains(nom))
					results.add(forms.get(i));
			}
			//queryBis = queryBis.filter("nom_bis >= ",nom.toLowerCase()).filter("nom_bis < ",nom.toLowerCase()+"\uFFFD");
		}
		return results;
	}
	
	/**
	 * Récuperer le formulaire par son nom et sa version
	 */
	@Override
	public Formulaire getFormParNomAndVersion(String nom, int version) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Formulaire.class).filter("nom", nom).filter("version", version).filter("etat !=", 0).get();
	}

	/**
	 * Récuperer la derniere version d'un formulaire
	 */
	@Override
	public int getLastVersion( String nom) {
		Objectify ofy = ObjectifyService.begin();
		List<Formulaire> fs = ofy.query(Formulaire.class).filter("nom", nom).filter("etat !=", 0).list();
		int lastVersion=1;
		for(int i =0; i <fs.size(); i++)
			if(fs.get(i).getVersion() > lastVersion)
				lastVersion = fs.get(i).getVersion();
		
		return lastVersion;
	}

	/**
	 * Désactiver un formulaire
	 */
	@Override
	public void supprimer(Long id) {
		Formulaire form = getFormulaireById(id);
		form.setEtat(0);
		ajouterFormulaire(form);
	}

	@Override
	public void allouerForm(Long compte, Long formulaire) {
		Objectify ofy = ObjectifyService.begin();
		Formulaire f = this.getFormulaireById(formulaire);
		CompteDaoImpl cdao = new CompteDaoImpl();
		Compte c = cdao.getCompteById(compte);
		Key<Compte> kc =new Key<Compte>(Compte.class, compte);
		f.addUser(kc);
		ofy.put(f);
		Key<Formulaire> kf =new Key<Formulaire>(Formulaire.class, f.getId());
		c.addFormulaire(kf);
		ofy.put(c);
	}

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@Override
	public List<Compte> getAllocationUsers(Long formulaire) {
		Objectify ofy = ObjectifyService.begin();
		Formulaire f = getFormulaireById(formulaire);
		List <Key<Compte>> usersKey = f.getUsers();
		List users = new ArrayList<Compte>();
		CompteDaoImpl cdao = new CompteDaoImpl();
		for(int i = 0; i<usersKey.size(); i++)
			if(cdao.getCompteById(usersKey.get(i).getId()).getEtat() != 0)
				users.add(cdao.getCompteById(usersKey.get(i).getId()));
		return users;
	}
	
	
	@Override
	public List<Formulaire> getFormulairesByCompte(long compte) {
		CompteDaoImpl cdao = new CompteDaoImpl();
		Compte c = cdao.getCompteById(compte);
		List<Key<Formulaire>> formulaires = c.getFormulaire();
		List <Formulaire> forms = new ArrayList<Formulaire>();
		for(int i=0; i<formulaires.size(); i++){
			Formulaire f = getFormulaireById(formulaires.get(i).getId());
			if(f.getEtat() != 0)
				forms.add(getFormulaireById(formulaires.get(i).getId()));
			
		}
		return forms;
	}
	
	@Override
	public List<Formulaire> getFormulairesVersionByCompte(long compte) {
		List <Formulaire> forms = getFormulairesByCompte(compte);
		List <Formulaire> formsVersion = new ArrayList<Formulaire>();
		while(forms.size() > 0){
			Formulaire f = forms.get(0);
			forms.remove(0);
			int i =0;
			while(forms.size() > i ){
				if(forms.get(i).getNom().equals(f.getNom())){
					if(forms.get(i).getVersion() > f.getVersion())
						f = forms.get(i);
					forms.remove(i);
				}else i++;
			}
			formsVersion.add(f);
		}
		return formsVersion;
	}
	
	@Override
	public List<Formulaire> getFormulairesComptesByParam(long compte, String nom, String date) {
		Objectify ofy = ObjectifyService.begin();
		CompteDaoImpl cdao = new CompteDaoImpl();
		Compte c = cdao.getCompteById(compte);
		List<Key<Formulaire>> formulaires = c.getFormulaire();
		List <Formulaire> forms = new ArrayList<Formulaire>();
		for(int i=0; i<formulaires.size(); i++)
			forms.add(getFormulaireById(formulaires.get(i).getId()));
		
		
		Query<Formulaire> query = ofy.query(Formulaire.class).filter("etat !=", 0);
		
		if(!date.equals("-"))
		{
			query = query.filter("date_creation",date);
		}
		
		List<Formulaire> forms1 = retainAllCompte(forms , query.list());
		List<Formulaire> results = new ArrayList<Formulaire>();
		
		if(!nom.equals("-"))
		{
			for(int i = 0; i< forms1.size(); i++){
				if(forms1.get(i).getNom().contains(nom))
					results.add(forms1.get(i));
			}
		}
		
		return results;
		
	}
	
	
	public boolean comprarerComptes(Formulaire c1 , Formulaire c2){
		Objectify ofy = ObjectifyService.begin();
		if (c1.getId().equals(c2.getId()))
			return true;
		else return false;
	}
	
	
	public boolean contientCompte(List<Formulaire> list , Formulaire c){
		int i = 0;
		boolean found = false;
		while( i< list.size() && !found){
			if(comprarerComptes(c , list.get(i)))
				found = true;
			i++;
		}
		return found;
	}
	
	public List<Formulaire> retainAllCompte(List<Formulaire> list1 , List<Formulaire> list2 ){
		
		List<Formulaire> retour = new ArrayList<Formulaire> ();
		for(int i=0; i<list1.size(); i++){
			if(contientCompte(list2, list1.get(i))){
				retour.add(list1.get(i));
			}
		}
		
		return retour;
	}
	
	
	@Override
	public void ajouterFormulaire(Formulaire formulaire, List<Element> elements) {
		Objectify ofy = ObjectifyService.begin();
		ofy.put(formulaire);
		ElementDaoImpl edao = new ElementDaoImpl();
		Element element;
		for(int i = 0; i<elements.size(); i++){
			element = elements.get(i);
			element.setId(null);
			element.setFormulaire(new Key<Formulaire>(Formulaire.class, formulaire.getId()));
			edao.ajouterElement(element);
		}
		
	}

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public void initialiserAllocation(Long id) {
		Objectify ofy = ObjectifyService.begin();
		Formulaire f = this.getFormulaireById(id);
		CompteDaoImpl cdao = new CompteDaoImpl();
		List<Key<Compte>> c = new ArrayList<Key<Compte>>();
		List<Compte> comptes = cdao.getAllocationFormulaire(id);
		f.setUsers(null);
		f.setUsers(c);
		this.ajouterFormulaire(f);
		
		for(int i =0; i< comptes.size(); i++){
			List<Key<Formulaire>> forms = comptes.get(i).getFormulaire();
			List<Key<Formulaire>> formsBis = new ArrayList<Key<Formulaire>>();
			for(int j = 0; j<forms.size(); j++)
				if(!forms.get(j).equals(new Key(Formulaire.class, id)))
					formsBis.add(forms.get(j));
			
			comptes.get(i).setFormulaire(formsBis);
			cdao.ajouterCompte(comptes.get(i));
		}
		
	}

}
