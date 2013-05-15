package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class ModifierClient extends UIObject {

	@UiField
	protected InputElement form_RS;
	
	@UiField
	protected InputElement adresse;
	
	@UiField
	protected InputElement siret;
	
	@UiField
	protected InputElement ville;
	
	@UiField
	protected InputElement cp;
	
	@UiField
	protected InputElement pays;
	
	@UiField
	protected InputElement date;
	
	@UiField
	protected InputElement offre;
	
	@UiField
	protected InputElement quota_formulaire;
	
	@UiField
	protected InputElement quota_utilisateur;
	
	@UiField
	protected ButtonElement enregistrer;
	
	@UiField
	protected ButtonElement retour;
	

	
	private static ModifierClientUiBinder uiBinder = GWT
			.create(ModifierClientUiBinder.class);

	interface ModifierClientUiBinder extends UiBinder<Element, ModifierClient> {
	}

	public ModifierClient() {
		setElement(uiBinder.createAndBindUi(this));
	}

	/**
	 * @return the form_RS
	 */
	public InputElement getForm_RS() {
		return form_RS;
	}

	/**
	 * @param form_RS the form_RS to set
	 */
	public void setForm_RS(InputElement form_RS) {
		this.form_RS = form_RS;
	}

	/**
	 * @return the adresse
	 */
	public InputElement getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(InputElement adresse) {
		this.adresse = adresse;
	}

	/**
	 * @return the siret
	 */
	public InputElement getSiret() {
		return siret;
	}

	/**
	 * @param siret the siret to set
	 */
	public void setSiret(InputElement siret) {
		this.siret = siret;
	}

	/**
	 * @return the ville
	 */
	public InputElement getVille() {
		return ville;
	}

	/**
	 * @param ville the ville to set
	 */
	public void setVille(InputElement ville) {
		this.ville = ville;
	}

	/**
	 * @return the cp
	 */
	public InputElement getCp() {
		return cp;
	}

	/**
	 * @param cp the cp to set
	 */
	public void setCp(InputElement cp) {
		this.cp = cp;
	}

	/**
	 * @return the pays
	 */
	public InputElement getPays() {
		return pays;
	}

	/**
	 * @param pays the pays to set
	 */
	public void setPays(InputElement pays) {
		this.pays = pays;
	}

	/**
	 * @return the offre
	 */
	public InputElement getOffre() {
		return offre;
	}

	/**
	 * @param offre the offre to set
	 */
	public void setOffre(InputElement offre) {
		this.offre = offre;
	}

	/**
	 * @return the enregistrer
	 */
	public ButtonElement getEnregistrer() {
		return enregistrer;
	}

	/**
	 * @param enregistrer the enregistrer to set
	 */
	public void setEnregistrer(ButtonElement enregistrer) {
		this.enregistrer = enregistrer;
	}

	/**
	 * @return the date
	 */
	public InputElement getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(InputElement date) {
		this.date = date;
	}

	/**
	 * @return the quota_formulaire
	 */
	public InputElement getQuota_formulaire() {
		return quota_formulaire;
	}

	/**
	 * @param quota_formulaire the quota_formulaire to set
	 */
	public void setQuota_formulaire(InputElement quota_formulaire) {
		this.quota_formulaire = quota_formulaire;
	}

	/**
	 * @return the quota_utilisateur
	 */
	public InputElement getQuota_utilisateur() {
		return quota_utilisateur;
	}

	/**
	 * @param quota_utilisateur the quota_utilisateur to set
	 */
	public void setQuota_utilisateur(InputElement quota_utilisateur) {
		this.quota_utilisateur = quota_utilisateur;
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
