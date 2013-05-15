package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class HeaderAdministration extends UIObject {

	@UiField
	protected SpanElement user;
	
	@UiField
	protected AnchorElement deconnexion;
	
	@UiField
	protected AnchorElement accueil;
	
	@UiField
	protected AnchorElement formulaire;
	
	
	private static HeaderAdminstrationUiBinder uiBinder = GWT
			.create(HeaderAdminstrationUiBinder.class);

	interface HeaderAdminstrationUiBinder extends
			UiBinder<Element, HeaderAdministration> {
	}

	

	public HeaderAdministration(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
	}



	public AnchorElement getDeconnexion() {
		return deconnexion;
	}



	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}



	/**
	 * @return the user
	 */
	public SpanElement getUser() {
		return user;
	}



	/**
	 * @param user the user to set
	 */
	public void setUser(SpanElement user) {
		this.user = user;
	}



	/**
	 * @return the accueil
	 */
	public AnchorElement getAccueil() {
		return accueil;
	}



	/**
	 * @param accueil the accueil to set
	 */
	public void setAccueil(AnchorElement accueil) {
		this.accueil = accueil;
	}



	/**
	 * @return the formulaire link
	 */
	public AnchorElement getFormulaire() {
		return formulaire;
	}



	/**
	 * @param formulaire the formulaire link to set
	 */
	public void setFormulaire(AnchorElement formulaire) {
		this.formulaire = formulaire;
	}
	
	
	

}
