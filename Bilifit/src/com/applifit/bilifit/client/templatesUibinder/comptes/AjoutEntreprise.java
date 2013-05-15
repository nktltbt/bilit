package com.applifit.bilifit.client.templatesUibinder.comptes;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class AjoutEntreprise extends UIObject {

	private static AjoutEntrepriseUiBinder uiBinder = GWT.create(AjoutEntrepriseUiBinder.class);

	interface AjoutEntrepriseUiBinder extends UiBinder<Element, AjoutEntreprise> {
	}
	
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
	protected InputElement contact_nom;
	
	@UiField
	protected InputElement contact_prenom;
	
	@UiField
	protected InputElement offre;
	
	@UiField
	protected InputElement date_creation;
	
	@UiField
	protected InputElement fonction;
	
	@UiField
	protected InputElement telephone;
	
	@UiField
	protected InputElement mail;
	
	@UiField
	protected InputElement quota_user;
	
	@UiField
	protected InputElement quota_form;
	
	
	@UiField
	protected ButtonElement enregistrer;
	
	@UiField
	SpanElement titreButton;
	
	
	public AjoutEntreprise() {
		setElement(uiBinder.createAndBindUi(this));
	}
	
	public AjoutEntreprise(String form_RS, String adresse, String siret, String ville, String cp, String pays, String contact_nom,
			String contact_prenom, String offre, String date_creation, String fonction, String telephone, String mail, String quota_form, String quota_user) {
		setElement(uiBinder.createAndBindUi(this));
		this.titreButton.setInnerText("Modifier");
		this.form_RS.setValue(form_RS);
		this.adresse.setValue(adresse);
		this.siret.setValue(siret);
		this.ville.setValue(ville);
		this.cp.setValue(cp);
		this.pays.setValue(pays);
		this.contact_nom.setValue(contact_nom);
		this.contact_prenom.setValue(contact_prenom);
		this.offre.setValue(offre);
		this.date_creation.setValue(date_creation);
		this.fonction.setValue(fonction);
		this.telephone.setValue(telephone);
		this.mail.setValue(mail);
		this.quota_form.setValue(quota_form);
		this.quota_user.setValue(quota_user);
		
		this.form_RS.setReadOnly(true);
		this.adresse.setReadOnly(true);
		this.siret.setReadOnly(true);
		this.ville.setReadOnly(true);
		this.cp.setReadOnly(true);
		this.pays.setReadOnly(true);
		this.contact_nom.setReadOnly(true);
		this.contact_prenom.setReadOnly(true);
		this.offre.setReadOnly(true);
		this.date_creation.setReadOnly(true);
		this.fonction.setReadOnly(true);
		this.telephone.setReadOnly(true);
		this.mail.setReadOnly(true);
		this.quota_form.setReadOnly(true);
		this.quota_user.setReadOnly(true);
	}


	public InputElement getForm_RS() {
		return form_RS;
	}


	public void setForm_RS(InputElement form_RS) {
		this.form_RS = form_RS;
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


	public InputElement getDate_creation() {
		return date_creation;
	}


	public void setDate_creation(InputElement date_creation) {
		this.date_creation = date_creation;
	}


	public InputElement getFonction() {
		return fonction;
	}


	public void setFonction(InputElement fonction) {
		this.fonction = fonction;
	}


	public InputElement getTelephone() {
		return telephone;
	}


	public void setTelephone(InputElement telephone) {
		this.telephone = telephone;
	}


	public InputElement getMail() {
		return mail;
	}


	public void setMail(InputElement mail) {
		this.mail = mail;
	}


	public ButtonElement getEnregistrer() {
		return enregistrer;
	}


	public void setEnregistrer(ButtonElement enregistrer) {
		this.enregistrer = enregistrer;
	}

	public static AjoutEntrepriseUiBinder getUiBinder() {
		return uiBinder;
	}

	public static void setUiBinder(AjoutEntrepriseUiBinder uiBinder) {
		AjoutEntreprise.uiBinder = uiBinder;
	}

	public InputElement getSiret() {
		return siret;
	}

	public void setSiret(InputElement siret) {
		this.siret = siret;
	}

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

	/**
	 * @return the titreButton
	 */
	public SpanElement getTitreButton() {
		return titreButton;
	}

	/**
	 * @param titreButton the titreButton to set
	 */
	public void setTitreButton(SpanElement titreButton) {
		this.titreButton = titreButton;
	}

	/**
	 * @return the quota_user
	 */
	public InputElement getQuota_user() {
		return quota_user;
	}

	/**
	 * @param quota_user the quota_user to set
	 */
	public void setQuota_user(InputElement quota_user) {
		this.quota_user = quota_user;
	}

	/**
	 * @return the quota_form
	 */
	public InputElement getQuota_form() {
		return quota_form;
	}

	/**
	 * @param quota_form the quota_form to set
	 */
	public void setQuota_form(InputElement quota_form) {
		this.quota_form = quota_form;
	}




	
}
