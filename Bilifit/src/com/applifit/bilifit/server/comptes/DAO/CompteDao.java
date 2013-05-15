package com.applifit.bilifit.server.comptes.DAO;

import java.util.List;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;



public interface CompteDao {
	
	public void ajouterCompte(Compte c);
	public void modifierCompte(Compte c);
	public List<Compte> getCompteByProfil(String profil);
	public Compte getCompteById(Long id);
	public Compte getCompteByMail(String mail);
	public List<Compte> getCompteSaByParam(String mail,String nom,String prenom,String fonction, String profil);
	List<Compte> getComptes();
	List<Compte> getComptesByEntreprise(Entreprise entreprise);
	void supprimerCompte(Long id);
	boolean comprarerComptes(Compte c1, Compte c2);
	void desactiverComptes(Entreprise e);
	List<Compte> getAllocationFormulaire(Long formulaire);

	

	
	

}
