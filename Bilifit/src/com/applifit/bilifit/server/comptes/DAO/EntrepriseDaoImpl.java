package com.applifit.bilifit.server.comptes.DAO;

import java.util.ArrayList;
import java.util.List;

import com.applifit.bilifit.server.comptes.Entreprise;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class EntrepriseDaoImpl implements EntrepriseDao{

	
	static {  
        ObjectifyService.register(Entreprise.class);  
    } 
	
	@Override
	public Entreprise ajouterEntreprise(Entreprise entreprise) {
		Objectify ofy = ObjectifyService.begin();
		entreprise.setRaison_bis(entreprise.getRaison_sociale().toLowerCase());
		ofy.put(entreprise);
		return entreprise;
	}
	
	@Override
	public Entreprise getEntrepriseById(Long id) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(Entreprise.class, id);
	}
	
	@Override
	public List<Entreprise> getEntreprises() {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Entreprise.class).list();		
	}

	@Override
	public Entreprise getEntrepriseByRs(String rs) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.query(Entreprise.class).filter("raison_sociale", rs).get();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Entreprise> getEntrepriseByParams(String rs, String ville_cp,
			String contact_nom, String contact_mail, String offre, String date) {
		Objectify ofy = ObjectifyService.begin();
		List<Entreprise> entreprises = new ArrayList<Entreprise>();
		Query<Entreprise> query_min = ofy.query(Entreprise.class);

		Query<Entreprise> query_min_rs = query_min.clone();
		Query<Entreprise> query_min_nom = query_min.clone();
		Query<Entreprise> query_min_mail = query_min.clone();
		Query<Entreprise> query_min_offre = query_min.clone();
		
		
		boolean rsfound = false;
		boolean villefound = false;
		boolean nomfound = false;
		boolean mailfound = false;
		boolean offrefound = false;
		
		if(!rs.equals("-"))
		{
			rsfound =true;
			query_min_rs = query_min_rs.filter("raison_bis >= ",rs.toLowerCase()).filter("raison_bis <= ",rs.toLowerCase()+"\uFFFD");
		}
		
		if(!contact_nom.equals("-"))
		{   
			nomfound=true;
			query_min_nom = query_min_nom.filter("nom_contact_bis >= ",contact_nom.toLowerCase()).filter("nom_contact_bis < ",contact_nom.toLowerCase()+"Z\uFFFD");
		}
		if(!contact_mail.equals("-"))
		{
			mailfound =true;
			query_min_mail = query_min_mail.filter("mail_contact_bis >= ",contact_mail.toLowerCase()).filter("mail_contact_bis < ",contact_mail.toLowerCase()+"\uFFFD");
		}
		if(!offre.equals("-"))
		{
			offrefound =true;
			query_min_offre = query_min_offre.filter("offre_bis >= ",offre.toLowerCase()).filter("offre_bis < ",offre.toLowerCase()+"\uFFFD");
		}
		if(!date.equals("-"))
		{
			query_min = query_min.filter("date",date);
		}
		List villes = new ArrayList();
		
		if(!ville_cp.equals("-"))
		{	
			villefound= true;
			Query<Entreprise> queryBis_min = query_min.clone();
			Query<Entreprise> queryBis_min_ville = query_min.clone();
			villes.addAll(queryBis_min.filter("cp_bis >= ", ville_cp.toLowerCase()).filter("cp_bis < ", ville_cp.toLowerCase()+"\uFFFD").list());
			villes.addAll(queryBis_min_ville.filter("ville_bis >= ", ville_cp.toLowerCase()).filter("ville_bis < ", ville_cp.toLowerCase()+"\uFFFD").list());
			
		}
		
		List<Entreprise> entreprisesBis = new ArrayList<Entreprise>();
		entreprises.addAll(query_min.list());
		
		if(rsfound){
			entreprisesBis.addAll(query_min_rs.list());
			entreprises = retainAllEntreprise(entreprises,entreprisesBis);
		}
		if(nomfound){
			entreprisesBis.clear();
			entreprisesBis.addAll(query_min_nom.list());
			entreprises = retainAllEntreprise(entreprises,entreprisesBis);
		}
		if(villefound){
			entreprises = retainAllEntreprise(entreprises,villes);
		}
		if(mailfound){
			entreprisesBis.clear();
			entreprisesBis.addAll(query_min_mail.list());
			entreprises = retainAllEntreprise(entreprises,entreprisesBis);
		}
		if(offrefound){
			entreprisesBis.clear();
			entreprisesBis.addAll(query_min_offre.list());
			entreprises = retainAllEntreprise(entreprises,entreprisesBis);
		}

		return entreprises;
	}

	@Override
	public void supprimerClient(Long id) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(Entreprise.class, id);
	}
	
	@Override
	public boolean comprarerEntreprises(Entreprise e1 , Entreprise e2){
		if (e1.getId().equals(e2.getId()))
			return true;
		else return false;
	}
	
	
	public boolean contientEntreprise(List<Entreprise> list , Entreprise e){
		int i = 0;
		boolean found = false;
		while( i< list.size() && !found){
			if(comprarerEntreprises(e , list.get(i)))
				found = true;
			i++;
		}
		return found;
	}
	
	public List<Entreprise> retainAllEntreprise(List<Entreprise> list1 , List<Entreprise> list2 ){
		
		List<Entreprise> retour = new ArrayList<Entreprise> ();
		for(int i=0; i<list1.size(); i++){
			if(contientEntreprise(list2, list1.get(i))){
				retour.add(list1.get(i));
			}
		}
		
		return retour;
	}

	@Override
	public Entreprise modifierEntreprise(Entreprise entreprise) {
		
		Entreprise e = this.getEntrepriseById(entreprise.getId());
		entreprise.setContact_fonction(e.getContact_fonction());
		entreprise.setContact_mail(e.getContact_mail());
		entreprise.setContact_nom(e.getContact_nom());
		entreprise.setContact_prenom(e.getContact_prenom());
		entreprise.setContact_tel(e.getContact_tel());
		
		Objectify ofy = ObjectifyService.begin();
		ofy.put(entreprise);
				
		return entreprise;
	}

	@Override
	public void supprimerEntreprise(long entreprise) {
		Objectify ofy = ObjectifyService.begin();
		Entreprise e = this.getEntrepriseById(entreprise);
		CompteDaoImpl cdao = new CompteDaoImpl();
		cdao.desactiverComptes(e);
		e.setEtat(0);
		ofy.put(e);
	}


}
