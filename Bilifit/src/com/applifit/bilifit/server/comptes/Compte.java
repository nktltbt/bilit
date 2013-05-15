package com.applifit.bilifit.server.comptes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.applifit.bilifit.server.comptes.KeyJson.EntrepriseKeyDeserializer;
import com.applifit.bilifit.server.comptes.KeyJson.EntrepriseKeySerializer;
import com.applifit.bilifit.server.formulaires.Formulaire;
import com.applifit.bilifit.server.formulaires.KeyJson.FormulaireListKeySerializer;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class Compte {

	@Id
	Long id;
	String nom;
	String prenom;
	String fonction;
	String mail;

	String password;
	String profil;
	Key<Entreprise> entreprise;
	List<Key<Formulaire>> formulaire;

	// Pour marquer les comptes supprimés
	int etat;
	// Pour marquer les comptes dont la société à été désactivé
	int desactivation;
	// Pour enregistrer le device avec lequel un client utilisateur est
	// connecté => eviter plusieurs connexion d'un seul compte
	String device;

	transient String nom_bis;
	transient String prenom_bis;
	transient String fonction_bis;

	public Compte(Long id, String nom, String prenom, String fonction,
			String mail, String password, String profil,
			Key<Entreprise> entreprise, int etat) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.fonction = fonction;
		this.mail = mail;
		this.password = password;
		this.profil = profil;
		this.entreprise = entreprise;
		this.etat = etat;
		this.desactivation = 0;
		nom_bis = nom;
		prenom_bis = prenom;
		fonction_bis = fonction;
	}

	public Compte() {
		super();
		this.formulaire = new ArrayList<Key<Formulaire>>();
		this.etat = 1;
		this.desactivation = 0;
	}

	public Compte(String nom, String prenom, String fonction, String mail,
			String password, String profil) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.fonction = fonction;
		this.mail = mail;
		this.password = password;
		this.profil = profil;
		nom_bis = nom.toLowerCase();
		prenom_bis = prenom.toLowerCase();
		fonction_bis = fonction.toLowerCase();

	}

	public Compte(String nom, String prenom, String fonction, String mail,
			String password, String profil, Key<Entreprise> entreprise, int etat) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.fonction = fonction;
		this.mail = mail;
		this.password = password;
		this.profil = profil;
		this.entreprise = entreprise;
		this.etat = etat;
		nom_bis = nom.toLowerCase();
		prenom_bis = prenom.toLowerCase();
		fonction_bis = fonction.toLowerCase();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom
	 *            the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
		nom_bis = nom.toLowerCase();
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom
	 *            the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
		prenom_bis = prenom.toLowerCase();
	}

	/**
	 * @return the fonction
	 */
	public String getFonction() {
		return fonction;
	}

	/**
	 * @param fonction
	 *            the fonction to set
	 */
	public void setFonction(String fonction) {
		this.fonction = fonction;
		fonction_bis = fonction.toLowerCase();
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the profil
	 */
	public String getProfil() {
		return profil;
	}

	/**
	 * @param profil
	 *            the profil to set
	 */
	public void setProfil(String profil) {
		this.profil = profil;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	@JsonSerialize(using = EntrepriseKeySerializer.class)
	public Key<Entreprise> getEntreprise() {
		return this.entreprise;
	}

	@JsonDeserialize(using = EntrepriseKeyDeserializer.class)
	public void setEntreprise(Key<Entreprise> entreprise) {
		this.entreprise = entreprise;
	}

	@JsonSerialize(using = FormulaireListKeySerializer.class)
	public List<Key<Formulaire>> getFormulaire() {
		return formulaire;
	}

	public void setFormulaire(List<Key<Formulaire>> formulaire) {
		this.formulaire = formulaire;
	}

	public void addFormulaire(Key<Formulaire> kf) {
		this.getFormulaire().add(kf);
	}

	/**
	 * @return the nom_bis
	 */
	public String getNom_bis() {
		return nom_bis;
	}

	/**
	 * @param nom_bis
	 *            the nom_bis to set
	 */
	public void setNom_bis(String nom_bis) {
		this.nom_bis = nom_bis;
	}

	/**
	 * @return the prenom_bis
	 */
	public String getPrenom_bis() {
		return prenom_bis;
	}

	/**
	 * @param prenom_bis
	 *            the prenom_bis to set
	 */
	public void setPrenom_bis(String prenom_bis) {
		this.prenom_bis = prenom_bis;
	}

	/**
	 * @return the fonction_bis
	 */
	public String getFonction_bis() {
		return fonction_bis;
	}

	/**
	 * @param fonction_bis
	 *            the fonction_bis to set
	 */
	public void setFonction_bis(String fonction_bis) {
		this.fonction_bis = fonction_bis;
	}

	/**
	 * @return the desactivation
	 */
	public int getDesactivation() {
		return desactivation;
	}

	/**
	 * @param desactivation
	 *            the desactivation to set
	 */
	public void setDesactivation(int desactivation) {
		this.desactivation = desactivation;
	}

	/**
	 * @return the device
	 */
	public String getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(String device) {
		this.device = device;
	}

}
