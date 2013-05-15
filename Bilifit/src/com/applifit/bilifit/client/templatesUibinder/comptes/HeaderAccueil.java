package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class HeaderAccueil extends UIObject {

	private static HeaderAccueilUiBinder uiBinder = GWT
			.create(HeaderAccueilUiBinder.class);

	interface HeaderAccueilUiBinder extends UiBinder<Element, HeaderAccueil> {
	}
	@UiField
	SpanElement user;
	
	@UiField
	protected AnchorElement deconnexion;
	

	public AnchorElement getDeconnexion() {
		return deconnexion;
	}

	public void setDeconnexion(AnchorElement deconnexion) {
		this.deconnexion = deconnexion;
	}

	public HeaderAccueil(String firstName) {
		setElement(uiBinder.createAndBindUi(this));
		user.setInnerText(firstName);
	}

}
