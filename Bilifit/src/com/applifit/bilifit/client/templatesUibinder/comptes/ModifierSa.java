package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class ModifierSa extends UIObject {

	private static ModifierSaUiBinder uiBinder = GWT
			.create(ModifierSaUiBinder.class);

	interface ModifierSaUiBinder extends UiBinder<Element, ModifierSa> {
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
	protected InputElement password;
	
	@UiField
	protected InputElement password_confirmation;
	
	@UiField
	protected DivElement divPass;
	
	@UiField
	protected ButtonElement enregistrer;
	
	@UiField
	protected ButtonElement retour;

	public ModifierSa() {
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

	/**
	 * @return the password
	 */
	public InputElement getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(InputElement password) {
		this.password = password;
	}

	/**
	 * @return the password_confirmation
	 */
	public InputElement getPassword_confirmation() {
		return password_confirmation;
	}

	/**
	 * @param password_confirmation the password_confirmation to set
	 */
	public void setPassword_confirmation(InputElement password_confirmation) {
		this.password_confirmation = password_confirmation;
	}

	/**
	 * @return the divPass
	 */
	public DivElement getDivPass() {
		return divPass;
	}

	/**
	 * @param divPass the divPass to set
	 */
	public void setDivPass(DivElement divPass) {
		this.divPass = divPass;
	}

	
	

}
