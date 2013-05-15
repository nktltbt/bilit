/**
 * 
 */
package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

/**
 * @author Meryem
 *
 */
public class Header extends UIObject {

	private static HeaderUiBinder uiBinder = GWT.create(HeaderUiBinder.class);

	interface HeaderUiBinder extends UiBinder<Element, Header> {
	}

	@UiField
	SpanElement user;
	@UiField
	AnchorElement accueil;
	@UiField
	AnchorElement deconnexion;
	
	public Header(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
	}

	public AnchorElement getAccueil() {
		return accueil;
	}

	public void setAccueil(AnchorElement accueil) {
		this.accueil = accueil;
	}

	public AnchorElement getDeconnexion() {
		return deconnexion;
	}

	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}
	
	

}
