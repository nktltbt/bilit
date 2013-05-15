package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class HeaderAccueilAdmin extends UIObject {

	@UiField
	protected SpanElement user;
	
	@UiField
	protected AnchorElement gsa;
	
	@UiField
	protected AnchorElement deconnexion;
	
	
	private static HeaderAccueilAdminUiBinder uiBinder = GWT
			.create(HeaderAccueilAdminUiBinder.class);

	interface HeaderAccueilAdminUiBinder extends
			UiBinder<Element, HeaderAccueilAdmin> {
	}

	

	public HeaderAccueilAdmin(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
	}



	public SpanElement getUser() {
		return user;
	}



	public void setUser(SpanElement user) {
		this.user = user;
	}



	public AnchorElement getGsa() {
		return gsa;
	}



	public void setGsa(AnchorElement gsa) {
		this.gsa = gsa;
	}



	public AnchorElement getDeconnexion() {
		return deconnexion;
	}



	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}

	
}
