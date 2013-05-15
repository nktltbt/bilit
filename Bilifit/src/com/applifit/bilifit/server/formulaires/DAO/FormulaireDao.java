package com.applifit.bilifit.server.formulaires.DAO;

import java.util.List;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;
import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.Formulaire;

public interface FormulaireDao {

	Formulaire getFormulaireById(Long id);

	Formulaire ajouterFormulaire(Formulaire formulaire);

	Formulaire getFormParNom(String nom);

	List<Formulaire> getFormulaires();

	int getCountFormulaireByEntreprise(Entreprise entreprise);

	Boolean getEtatFormulaire(long idEntreprise);

	List<Formulaire> getFormulairesByEntreprise(long entreprise);

	List<Formulaire> getFormulairesByParam(long entreprise, String nom,String date);

	Formulaire getFormParNomAndVersion(String nom, int version);

	int getLastVersion(String id);

	void supprimer(Long id);

	List<Compte> getAllocationUsers(Long formulaire);

	void allouerForm(Long compte, Long formulaire);

	List<Formulaire> getFormulairesByCompte(long compte);

	List<Formulaire> getFormulairesComptesByParam(long compte, String nom, String date);

	void ajouterFormulaire(Formulaire formulaire, List<Element> elements);

	List<Formulaire> getFormulairesVersionByCompte(long compte);

}
