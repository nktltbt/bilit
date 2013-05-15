package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.UIObject;

public class HeaderAno extends UIObject {

	private static HeaderAnoUiBinder uiBinder = GWT
			.create(HeaderAnoUiBinder.class);

	interface HeaderAnoUiBinder extends UiBinder<Element, HeaderAno> {
	}

	public HeaderAno() {
		setElement(uiBinder.createAndBindUi(this));
	}

}
