package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class MenuForm extends UIObject {

	private static MenuFormUiBinder uiBinder = GWT
			.create(MenuFormUiBinder.class);

	interface MenuFormUiBinder extends UiBinder<Element, MenuForm> {
	}

	@UiField
	SpanElement user;
	
	@UiField
	protected AnchorElement deconnexion;
	
	@UiField 
	protected AnchorElement accueil;
	
	@UiField 
	protected AnchorElement formulaire;
	

	public AnchorElement getDeconnexion() {
		return deconnexion;
	}

	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}
	
	public MenuForm(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
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
