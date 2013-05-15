package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.dom.client.DivElement;


public class SousGestionCU extends UIObject {

	@UiField
	protected InputElement id;

	@UiField
	protected DivElement info;
	
	@UiField
	protected DivElement infonew;
	
	@UiField
	protected DivElement infos_collabs;
	
	@UiField
	protected InputElement nom;
	
	@UiField
	protected InputElement prenom;
	
	@UiField
	protected InputElement fonction;

	@UiField
	protected InputElement profil_admin;
	
	@UiField
	protected InputElement mail;
	
	@UiField
	protected InputElement mdp1;
	
	@UiField
	protected InputElement mdp2;
	
	@UiField
	protected DivElement mdpDiv;
	
	@UiField
	protected ButtonElement enregistrer;
	
	@UiField
	protected ButtonElement annuler;
	
	@UiField
	protected TableSectionElement tbody;
	
	@UiField
	protected LabelElement profil_label;
	
	private static SousGestionCUUiBinder uiBinder = GWT
			.create(SousGestionCUUiBinder.class);

	interface SousGestionCUUiBinder extends UiBinder<Element, SousGestionCU> {
	}

	public SousGestionCU() {
		setElement(uiBinder.createAndBindUi(this));
	
	}

	/**
	 * @return the mdpDiv
	 */
	public DivElement getMdpDiv() {
		return mdpDiv;
	}

	/**
	 * @param mdpDiv the mdpDiv to set
	 */
	public void setMdpDiv(DivElement mdpDiv) {
		this.mdpDiv = mdpDiv;
	}

	public InputElement getId() {
		return id;
	}

	public void setId(InputElement id) {
		this.id = id;
	}
	
	public InputElement getNom() {
		return nom;
	}

	public void setNom(InputElement nom) {
		this.nom = nom;
	}
	
	public void setNomValue(String nom) {
		this.nom.setValue(nom);
	}
	
	public InputElement getPrenom() {
		return prenom;
	}

	public void setPrenom(InputElement prenom) {
		this.prenom = prenom;
	}
	
	public void setPrenomValue(String prenom) {
		this.prenom.setValue(prenom);
	}

	public InputElement getFonction() {
		return fonction;
	}

	public void setFonction(InputElement fonction) {
		this.fonction = fonction;
	}

	public void setFonctionValue(String fonction) {
		this.fonction.setValue(fonction);
	}
	
	public InputElement getProfil_admin() {
		return profil_admin;
	}

	public void setProfil_admin(InputElement profil_admin) {
		this.profil_admin = profil_admin;
	}
	
	public ButtonElement getEnregistrer() {
		return enregistrer;
	}

	public void setEnregistrer(ButtonElement enregistrer) {
		this.enregistrer = enregistrer;
	}
	
	public InputElement getMail() {
		return mail;
	}

	public void setMail(InputElement mail) {
		this.mail = mail;
	}
	
	public void setMailValue(String email) {
		this.mail.setValue(email);
	}

	public TableSectionElement getTbody() {
		return tbody;
	}

	public void setTbody(TableSectionElement tbody) {
		this.tbody = tbody;
	}

	public DivElement getInfo() {
		return info;
	}

	public void setInfo(DivElement info) {
		this.info = info;
	}

	public DivElement getInfonew() {
		return infonew;
	}

	public void setInfonew(DivElement infonew) {
		this.infonew = infonew;
	}

	public DivElement getInfos_collabs() {
		return infos_collabs;
	}

	public void setInfos_collabs(DivElement infos_collabs) {
		this.infos_collabs = infos_collabs;
	}

	/**
	 * @return the mdp1
	 */
	public InputElement getMdp1() {
		return mdp1;
	}

	/**
	 * @param mdp1 the mdp1 to set
	 */
	public void setMdp1(InputElement mdp1) {
		this.mdp1 = mdp1;
	}

	/**
	 * @return the mdp2
	 */
	public InputElement getMdp2() {
		return mdp2;
	}

	/**
	 * @param mdp2 the mdp2 to set
	 */
	public void setMdp2(InputElement mdp2) {
		this.mdp2 = mdp2;
	}

	public ButtonElement getAnnuler() {
		return annuler;
	}

	public void setAnnuler(ButtonElement annuler) {
		this.annuler = annuler;
	}

	/**
	 * @return the profil_label
	 */
	public LabelElement getProfil_label() {
		return profil_label;
	}

	/**
	 * @param profil_label the profil_label to set
	 */
	public void setProfil_label(LabelElement profil_label) {
		this.profil_label = profil_label;
	}
	
	
}

