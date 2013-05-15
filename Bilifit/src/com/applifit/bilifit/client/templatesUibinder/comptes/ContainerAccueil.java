package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class ContainerAccueil extends UIObject {

	private static ContainerAccueilUiBinder uiBinder = GWT
			.create(ContainerAccueilUiBinder.class);

	interface ContainerAccueilUiBinder extends
			UiBinder<Element, ContainerAccueil> {
	}

	public ContainerAccueil() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
