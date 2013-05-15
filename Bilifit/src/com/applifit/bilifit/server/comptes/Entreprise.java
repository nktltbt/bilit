package com.applifit.bilifit.server.comptes;


import javax.jdo.annotations.Index;
import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;


@Entity
public class Entreprise {


	@Id
	Long id;
	@Index
	String raison_sociale;
	@Index
	String siret;
	
	String adresse;
	@Index
	String ville;
	@Index
	String code_postal;
	String pays;
	@Index
	String offre_souscrite;
	@Index
	String date;
	@Index
	String contact_nom;
	@Index
	String contact_prenom;
	@Index
	String contact_mail;
	String contact_tel;
	@Index
	String contact_fonction;
	
	int quota_formulaire;
	int quota_utilisateur;
	
	//Pour marquer les entreprises désactivées
	int etat;
	
	
	transient String raison_bis ;
	transient String ville_bis ;
	transient String cp_bis ;
	transient String nom_contact_bis ;
	transient String mail_contact_bis ;
	transient String offre_bis ;
	
	public Entreprise() {
		super();
		this.etat=1;
	}

	public Entreprise(String raison_sociale, String siret, String adresse,
			String ville, String code_postal, String pays,
			String offre_souscrite, String date, String contact_nom,
			String contact_prenom, String contact_mail, String contact_tel,
			String contact_fonction, int quota_formulaire, int quota_utilisateur) {
		super();
		this.raison_sociale = raison_sociale;
		this.siret = siret;
		this.adresse = adresse;
		this.ville = ville;
		this.code_postal = code_postal;
		this.pays = pays;
		this.offre_souscrite = offre_souscrite;
		this.date = date;
		this.contact_nom = contact_nom;
		this.contact_prenom = contact_prenom;
		this.contact_mail = contact_mail;
		this.contact_tel = contact_tel;
		this.contact_fonction = contact_fonction;
		this.quota_formulaire = quota_formulaire;
		this.quota_utilisateur = quota_utilisateur;
		this.etat=1;
		raison_bis = raison_sociale.toLowerCase();
		ville_bis = ville.toLowerCase();
		cp_bis = code_postal.toLowerCase();
		nom_contact_bis = contact_nom.toLowerCase();
		mail_contact_bis = contact_mail.toLowerCase();
		offre_bis = offre_souscrite.toLowerCase();
	
	}




	public String getRaison_sociale() {
		return raison_sociale;
	}


	public void setRaison_sociale(String raison_sociale) {
		this.raison_sociale = raison_sociale;
		raison_bis = raison_sociale.toLowerCase();
	}
	
	


	/**
	 * @return the siret
	 */
	public String getSiret() {
		return siret;
	}

	/**
	 * @param siret the siret to set
	 */
	public void setSiret(String siret) {
		this.siret = siret;
	}

	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getVille() {
		return ville;
	}


	public void setVille(String ville) {
		this.ville = ville;
		ville_bis = ville.toLowerCase();
	}


	public String getCode_postal() {
		return code_postal;
	}


	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
		cp_bis = code_postal.toLowerCase();
	}


	public String getPays() {
		return pays;
	}


	public void setPays(String pays) {
		this.pays = pays;
	}


	public String getOffre_souscrite() {
		return offre_souscrite;
	}


	public void setOffre_souscrite(String offre_souscrite) {
		this.offre_souscrite = offre_souscrite;
		offre_bis = offre_souscrite.toLowerCase();
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getContact_nom() {
		return contact_nom;
	}

	public void setContact_nom(String contact_nom) {
		this.contact_nom = contact_nom;
		nom_contact_bis= contact_nom.toLowerCase();
	}

	public String getContact_prenom() {
		return contact_prenom;
	}

	public void setContact_prenom(String contact_prenom) {
		this.contact_prenom = contact_prenom;
	}

	public String getContact_mail() {
		return contact_mail;
	}

	public void setContact_mail(String contact_mail) {
		this.contact_mail = contact_mail;
		mail_contact_bis = contact_mail.toLowerCase();
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public String getContact_fonction() {
		return contact_fonction;
	}

	public void setContact_fonction(String contact_fonction) {
		this.contact_fonction = contact_fonction;
	}

	public int getQuota_formulaire() {
		return quota_formulaire;
	}

	public void setQuota_formulaire(int quota_formulaire) {
		this.quota_formulaire = quota_formulaire;
	}

	public int getQuota_utilisateur() {
		return quota_utilisateur;
	}

	public void setQuota_utilisateur(int quota_utilisateur) {
		this.quota_utilisateur = quota_utilisateur;
	}

	/**
	 * @return the raison_bis
	 */
	public String getRaison_bis() {
		return raison_bis;
	}

	/**
	 * @param raison_bis the raison_bis to set
	 */
	public void setRaison_bis(String raison_bis) {
		this.raison_bis = raison_bis;
	}

	/**
	 * @return the ville_bis
	 */
	public String getVille_bis() {
		return ville_bis;
	}

	/**
	 * @param ville_bis the ville_bis to set
	 */
	public void setVille_bis(String ville_bis) {
		this.ville_bis = ville_bis;
	}

	/**
	 * @return the cp_bis
	 */
	public String getCp_bis() {
		return cp_bis;
	}

	/**
	 * @param cp_bis the cp_bis to set
	 */
	public void setCp_bis(String cp_bis) {
		this.cp_bis = cp_bis;
	}

	/**
	 * @return the nom_contact_bis
	 */
	public String getNom_contact_bis() {
		return nom_contact_bis;
	}

	/**
	 * @param nom_contact_bis the nom_contact_bis to set
	 */
	public void setNom_contact_bis(String nom_contact_bis) {
		this.nom_contact_bis = nom_contact_bis;
	}

	/**
	 * @return the mail_contact_bis
	 */
	public String getMail_contact_bis() {
		return mail_contact_bis;
	}

	/**
	 * @param mail_contact_bis the mail_contact_bis to set
	 */
	public void setMail_contact_bis(String mail_contact_bis) {
		this.mail_contact_bis = mail_contact_bis;
	}

	/**
	 * @return the offre_bis
	 */
	public String getOffre_bis() {
		return offre_bis;
	}

	/**
	 * @param offre_bis the offre_bis to set
	 */
	public void setOffre_bis(String offre_bis) {
		this.offre_bis = offre_bis;
	}

	/**
	 * @return the etat
	 */
	public int getEtat() {
		return etat;
	}

	/**
	 * @param etat the etat to set
	 */
	public void setEtat(int etat) {
		this.etat = etat;
	}
	
	
	
	
	
	
	
	
}
