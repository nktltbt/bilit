package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class Accueil_Applifit extends UIObject {

	@UiField
	protected SpanElement user;
	
	@UiField
	protected AnchorElement administration_applifit;
	
	@UiField
	protected AnchorElement formulaire_menu_applifit;
	
	@UiField
	protected AnchorElement bdd_applifit;
	
	@UiField
	protected AnchorElement dashbords;
	
	
	@UiField 
	protected AnchorElement deconnexion;
	
	@UiField 
	protected AnchorElement accueil;
	
	private static Accueil_ApplifitUiBinder uiBinder = GWT
			.create(Accueil_ApplifitUiBinder.class);

	interface Accueil_ApplifitUiBinder extends
			UiBinder<Element, Accueil_Applifit> {
	}

	public Accueil_Applifit(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
	}

	public SpanElement getUser() {
		return user;
	}

	public void setUser(SpanElement user) {
		this.user = user;
	}

	public AnchorElement getAdministration_applifit() {
		return administration_applifit;
	}

	public void setAdministration_applifit(AnchorElement administration_applifit) {
		this.administration_applifit = administration_applifit;
	}

	public AnchorElement getDeconnexion() {
		return deconnexion;
	}

	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}

	public AnchorElement getAccueil() {
		return accueil;
	}

	public void setAccueil(AnchorElement accueil) {
		this.accueil = accueil;
	}

	public AnchorElement getFormulaire_menu_applifit() {
		return formulaire_menu_applifit;
	}

	public void setFormulaire_menu_applifit(AnchorElement formulaire_menu_applifit) {
		this.formulaire_menu_applifit = formulaire_menu_applifit;
	}

	public AnchorElement getBdd_applifit() {
		return bdd_applifit;
	}

	public void setBdd_applifit(AnchorElement bdd_applifit) {
		this.bdd_applifit = bdd_applifit;
	}

	/**
	 * @return the dashbords
	 */
	public AnchorElement getDashbords() {
		return dashbords;
	}

	/**
	 * @param dashbords the dashbords to set
	 */
	public void setDashbords(AnchorElement dashbords) {
		this.dashbords = dashbords;
	}

	
	
}
