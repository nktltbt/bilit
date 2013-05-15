package com.applifit.bilifit.server.comptes.DAO;

import java.util.ArrayList;
import java.util.List;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;
import com.applifit.bilifit.server.formulaires.Formulaire;
import com.applifit.bilifit.server.formulaires.DAO.FormulaireDaoImpl;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class CompteDaoImpl implements CompteDao{
	
	static {  
	        ObjectifyService.register(Compte.class);  
	    } 

	@Override
	public void ajouterCompte(Compte c) {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		c.setMail(c.getMail().toLowerCase());
		ofy.put(c);

	}

	@Override
	public List<Compte> getCompteByProfil(String profil) {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Compte.class).filter("profil", profil).filter("etat", 1).list();
	}
	
	@Override
	public List<Compte> getComptes() {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Compte.class).filter("etat", 1).list();
	}
	
	@Override
	public Compte getCompteById(Long id) {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Compte.class, id);
	}

	@Override
	public Compte getCompteByMail(String mail) {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Compte.class).filter("mail", mail).filter("desactivation", 0).filter("etat", 1).get();
	}

	@Override
	public List<Compte> getCompteSaByParam(String mail, String nom, String prenom, String fonction, String profil) {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		Query<Compte> query_min = ofy.query(Compte.class).filter("profil", profil).filter("etat", 1);
		
		Query<Compte> query_min_mail = query_min.clone();
		
		Query<Compte> query_min_nom = query_min.clone();
		
		Query<Compte> query_min_prenom = query_min.clone();
		
		Query<Compte> query_min_fonction = query_min.clone();
		
		boolean mailFound = false;
		boolean nomFound = false;
		boolean prenomFound = false;
		boolean fonctionFound = false;
		
		
		if(! mail.equals("-"))
		{
			mailFound =true;
			query_min_mail = query_min_mail.filter("mail >= ", mail.toLowerCase()).filter("mail < ", mail.toLowerCase()+"\uFFFD");
			
		}
		if(!nom.equals("-"))
		{
			nomFound =true;
			query_min_nom = query_min_nom.filter("nom_bis >= ", nom.toLowerCase()).filter("nom_bis < ", nom.toLowerCase()+"\uFFFD");
		}
		if(!prenom.equals("-"))
		{
			prenomFound =true;
			query_min_prenom = query_min_prenom.filter("prenom_bis >= ", prenom.toLowerCase()).filter("prenom_bis <", prenom.toLowerCase()+"\uFFFD");
		}
		if(!fonction.equals("-"))
		{
			fonctionFound =true;
			query_min_fonction = query_min_fonction.filter("fonction_bis >= ", fonction.toLowerCase()).filter("fonction_bis <", fonction.toLowerCase()+"\uFFFD");
			
		}
		
		List<Compte> comptes = new ArrayList<Compte>();
		List<Compte> comptesBis = new ArrayList<Compte>();
	
		comptes.addAll(query_min.list());
		
		if(mailFound){
			
			comptesBis.addAll(query_min_mail.list());
			comptes = retainAllCompte(comptes,comptesBis);
		}
		if(nomFound){
			comptesBis.clear();
			comptesBis.addAll(query_min_nom.list());
			comptes = retainAllCompte(comptes,comptesBis);
		}
		if(prenomFound){
			comptesBis.clear();
			comptesBis.addAll(query_min_prenom.list());
			comptes = retainAllCompte(comptes,comptesBis);
		}
		if(fonctionFound){
			comptesBis.clear();
			comptesBis.addAll(query_min_fonction.list());
			comptes = retainAllCompte(comptes,comptesBis);
		}
		
		return comptes;
	}

	@Override
	public void modifierCompte(Compte c) {
		// TODO Auto-generated method stub
		Objectify ofy = ObjectifyService.begin();
		c.setMail(c.getMail().toLowerCase());
		ofy.put(c);
	}
	
	@Override
	public List<Compte> getComptesByEntreprise(Entreprise entreprise) {
		Objectify ofy = ObjectifyService.begin();
		List<Compte> comptes = new ArrayList<Compte>();
		comptes.addAll(ofy.query(Compte.class).filter("entreprise", entreprise).filter("etat", 1).filter("profil", "CA").list());
		comptes.addAll(ofy.query(Compte.class).filter("entreprise", entreprise).filter("etat", 1).filter("profil", "CU").list());
		return comptes;
		
	}

	@Override
	public void supprimerCompte(Long id) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(Compte.class, id);
	}

	
	@Override
	public boolean comprarerComptes(Compte c1 , Compte c2){
		if (c1.getId().equals(c2.getId()))
			return true;
		else return false;
	}
	
	
	public boolean contientCompte(List<Compte> list , Compte c){
		int i = 0;
		boolean found = false;
		while( i< list.size() && !found){
			if(comprarerComptes(c , list.get(i)))
				found = true;
			i++;
		}
		return found;
	}
	
	public List<Compte> retainAllCompte(List<Compte> list1 , List<Compte> list2 ){
		
		List<Compte> retour = new ArrayList<Compte> ();
		for(int i=0; i<list1.size(); i++){
			if(contientCompte(list2, list1.get(i))){
				retour.add(list1.get(i));
			}
		}
		
		return retour;
	}

	@Override
	public void desactiverComptes(Entreprise e) {
		Objectify ofy = ObjectifyService.begin();
		List <Compte> comptes = new ArrayList<Compte>();
		comptes = getComptesByEntreprise(e);
		for(int i = 0; i<comptes.size(); i++){
			comptes.get(i).setDesactivation(1);
			ofy.put(comptes.get(i));
		}
		
	}
	
	@Override
	public List<Compte> getAllocationFormulaire(Long formulaire) {
		Objectify ofy = ObjectifyService.begin();
		FormulaireDaoImpl fdao = new FormulaireDaoImpl();
		Formulaire f = fdao.getFormulaireById(formulaire);
		List <Compte> users = ofy.query(Compte.class).filter("formulaire", f).list();
		
		return users;
	}


	
	

}
