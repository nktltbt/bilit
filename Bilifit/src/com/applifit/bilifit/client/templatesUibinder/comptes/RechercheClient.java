package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class RechercheClient extends UIObject {

	
	@UiField
	protected AnchorElement linkajout;
	
	@UiField
	protected InputElement rs;
	@UiField
	protected InputElement ville_cp;
	@UiField
	protected InputElement contact_nom;
	@UiField
	protected InputElement contact_mail;
	@UiField
	protected InputElement offre;
	@UiField
	protected InputElement date;
	@UiField
	protected ButtonElement rechercher;
	
	public InputElement getRs() {
		return rs;
	}


	public void setRs(InputElement rs) {
		this.rs = rs;
	}


	public InputElement getVille_cp() {
		return ville_cp;
	}


	public void setVille_cp(InputElement ville_cp) {
		this.ville_cp = ville_cp;
	}


	public InputElement getContact_nom() {
		return contact_nom;
	}


	public void setContact_nom(InputElement contact_nom) {
		this.contact_nom = contact_nom;
	}


	public InputElement getContact_mail() {
		return contact_mail;
	}


	public void setContact_mail(InputElement contact_mail) {
		this.contact_mail = contact_mail;
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


	public ButtonElement getRechercher() {
		return rechercher;
	}


	public void setRechercher(ButtonElement rechercher) {
		this.rechercher = rechercher;
	}


	public AnchorElement getLinkajout() {
		return linkajout;
	}


	public void setLinkajout(AnchorElement linkajout) {
		this.linkajout = linkajout;
	}


	private static RechercheClientUiBinder uiBinder = GWT
			.create(RechercheClientUiBinder.class);

	interface RechercheClientUiBinder extends
			UiBinder<Element, RechercheClient> {
	}


	public RechercheClient() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
