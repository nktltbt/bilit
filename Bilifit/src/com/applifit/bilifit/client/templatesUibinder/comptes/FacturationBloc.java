package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class FacturationBloc extends UIObject {

	private static FacturationBlocUiBinder uiBinder = GWT
			.create(FacturationBlocUiBinder.class);

	interface FacturationBlocUiBinder extends
			UiBinder<Element, FacturationBloc> {
	}

	public FacturationBloc() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
