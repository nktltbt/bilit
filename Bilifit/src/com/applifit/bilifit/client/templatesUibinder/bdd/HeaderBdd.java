package com.applifit.bilifit.client.templatesUibinder.bdd;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class HeaderBdd extends UIObject {

	private static HeaderBddUiBinder uiBinder = GWT
			.create(HeaderBddUiBinder.class);

	interface HeaderBddUiBinder extends UiBinder<Element, HeaderBdd> {
	}

	@UiField
	SpanElement user;

	@UiField
	AnchorElement deconnexion;
	
	@UiField
	AnchorElement accueil;
	
	@UiField
	AnchorElement formulaire;
	
	public HeaderBdd(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
	}

	/**
	 * @return the uiBinder
	 */
	public static HeaderBddUiBinder getUiBinder() {
		return uiBinder;
	}

	/**
	 * @param uiBinder the uiBinder to set
	 */
	public static void setUiBinder(HeaderBddUiBinder uiBinder) {
		HeaderBdd.uiBinder = uiBinder;
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
	 * @return the deconnexion
	 */
	public AnchorElement getDeconnexion() {
		return deconnexion;
	}

	/**
	 * @param deconnexion the deconnexion to set
	 */
	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}

	public AnchorElement getAccueil() {
		return accueil;
	}

	public void setAccueil(AnchorElement accueil) {
		this.accueil = accueil;
	}

	public AnchorElement getFormulaire() {
		return formulaire;
	}

	public void setFormulaire(AnchorElement formulaire) {
		this.formulaire = formulaire;
	}
	
	

}
