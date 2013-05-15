package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;


public class AjoutSa extends UIObject {

	private static AjoutSaUiBinder uiBinder = GWT.create(AjoutSaUiBinder.class);

	interface AjoutSaUiBinder extends UiBinder<Element, AjoutSa> {
	}

	@UiField
	protected InputElement nom;
	
	@UiField
	protected InputElement prenom;
	
	@UiField
	protected InputElement mail;
	
	@UiField
	protected InputElement fonction;
	
	@UiField
	protected ButtonElement enregistrer;
	
	@UiField
	protected ButtonElement retour;
	
	

	public AjoutSa() {
		setElement(uiBinder.createAndBindUi(this));
	}
	
	public ButtonElement getButton()
	{
		return enregistrer;
	}
	
	/**
	 * @return the nom
	 */
	public InputElement getNom(){
		return nom;
	}

	/**
	 * @return the prenom
	 */
	public InputElement getPrenom() {
		return prenom;
	}

	/**
	 * @return the mail
	 */
	public InputElement getMail() {
		return mail;
	}

	
	/**
	 * @return the fonction
	 */
	public InputElement getFonction() {
		return fonction;
	}

	public void setValueNom(String value){
		nom.setValue(value);
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
