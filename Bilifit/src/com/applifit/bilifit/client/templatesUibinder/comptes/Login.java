package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class Login extends UIObject {

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Element, Login> {
	}

	@UiField
	protected InputElement login;
	
	@UiField
	protected InputElement pass;
	
	@UiField
	protected ButtonElement connexion;

	@UiField
	protected AnchorElement oublier;
	
	public Login() {
		setElement(uiBinder.createAndBindUi(this));
	}

	/**
	 * @return the uiBinder
	 */
	public static LoginUiBinder getUiBinder() {
		return uiBinder;
	}

	/**
	 * @param uiBinder the uiBinder to set
	 */
	public static void setUiBinder(LoginUiBinder uiBinder) {
		Login.uiBinder = uiBinder;
	}

	/**
	 * @return the login
	 */
	public InputElement getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(InputElement login) {
		this.login = login;
	}

	/**
	 * @return the pass
	 */
	public InputElement getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(InputElement pass) {
		this.pass = pass;
	}

	/**
	 * @return the connexion
	 */
	public ButtonElement getConnexion() {
		return connexion;
	}

	/**
	 * @param connexion the connexion to set
	 */
	public void setConnexion(ButtonElement connexion) {
		this.connexion = connexion;
	}

	public AnchorElement getOublier() {
		return oublier;
	}

	public void setOublier(AnchorElement oublier) {
		this.oublier = oublier;
	}
	
	
	
	

}
