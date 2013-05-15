package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class QuotaAjout extends UIObject {

	private static QuotaAjoutUiBinder uiBinder = GWT
			.create(QuotaAjoutUiBinder.class);

	interface QuotaAjoutUiBinder extends UiBinder<Element, QuotaAjout> {
	}

	public QuotaAjout() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
