package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class AjoutClient extends UIObject {

	
	@UiField
	protected InputElement form_RS;
	
	@UiField
	protected InputElement siret;
	
	@UiField
	protected InputElement adresse;
	
	@UiField
	protected InputElement ville;
	
	@UiField
	protected InputElement cp;
	
	@UiField
	protected InputElement pays;
	
	
	@UiField
	protected InputElement offre;
	
	@UiField
	protected InputElement date;
	
	@UiField
	protected InputElement contact_nom;
	
	@UiField
	protected InputElement contact_prenom;
	
	@UiField
	protected InputElement contact_mail;
	
	@UiField
	protected InputElement contact_tel;
	
	@UiField
	protected InputElement contact_fonction;
	
	@UiField
	protected InputElement quota_formulaire;
	
	@UiField
	protected InputElement quota_utilisateur;
	
	public InputElement getContact_nom() {
		return contact_nom;
	}


	public void setContact_nom(InputElement contact_nom) {
		this.contact_nom = contact_nom;
	}


	public InputElement getContact_prenom() {
		return contact_prenom;
	}


	public void setContact_prenom(InputElement contact_prenom) {
		this.contact_prenom = contact_prenom;
	}


	public InputElement getContact_mail() {
		return contact_mail;
	}


	public void setContact_mail(InputElement contact_mail) {
		this.contact_mail = contact_mail;
	}


	public InputElement getContact_tel() {
		return contact_tel;
	}


	public void setContact_tel(InputElement contact_tel) {
		this.contact_tel = contact_tel;
	}


	public InputElement getContact_fonction() {
		return contact_fonction;
	}


	public void setContact_fonction(InputElement contact_fonction) {
		this.contact_fonction = contact_fonction;
	}


	@UiField
	protected ButtonElement enregistrer;
	
	
	@UiField
	protected ButtonElement retour;
	
	public InputElement getForm_RS() {
		return form_RS;
	}


	public void setForm_RS(InputElement form_RS) {
		this.form_RS = form_RS;
	}


	public InputElement getSiret() {
		return siret;
	}


	public void setSiret(InputElement siret) {
		this.siret = siret;
	}


	public InputElement getAdresse() {
		return adresse;
	}


	public void setAdresse(InputElement adresse) {
		this.adresse = adresse;
	}


	public InputElement getVille() {
		return ville;
	}


	public void setVille(InputElement ville) {
		this.ville = ville;
	}


	public InputElement getCp() {
		return cp;
	}


	public void setCp(InputElement cp) {
		this.cp = cp;
	}


	public InputElement getPays() {
		return pays;
	}


	public void setPays(InputElement pays) {
		this.pays = pays;
	}


	public InputElement getOffre() {
		return offre;
	}


	public void setOffre(InputElement offre) {
		this.offre = offre;
	}


	public InputElement getDate() {
		return date;
	}


	public void setDate(InputElement date) {
		this.date = date;
	}


	public ButtonElement getEnregistrer() {
		return enregistrer;
	}


	public void setEnregistrer(ButtonElement enregistrer) {
		this.enregistrer = enregistrer;
	}

	public InputElement getQuota_formulaire() {
		return quota_formulaire;
	}


	public void setQuota_formulaire(InputElement quota_formulaire) {
		this.quota_formulaire = quota_formulaire;
	}


	public InputElement getQuota_utilisateur() {
		return quota_utilisateur;
	}


	public void setQuota_utilisateur(InputElement quota_utilisateur) {
		this.quota_utilisateur = quota_utilisateur;
	}


	private static AjoutClientUiBinder uiBinder = GWT
			.create(AjoutClientUiBinder.class);

	interface AjoutClientUiBinder extends UiBinder<Element, AjoutClient> {
	}


	public AjoutClient() {
		setElement(uiBinder.createAndBindUi(this));
	}


	/**
	 * @return the retour
	 */
	public ButtonElement getRetour() {
		return retour;
	}


	/**
	 * @param retour the retour to set
	 */
	public void setRetour(ButtonElement retour) {
		this.retour = retour;
	}

}
