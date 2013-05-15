package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class BodyFormulaire extends UIObject {

	
	@UiField
	protected  AnchorElement link_allocation;
	
	private static BodyFormulaireUiBinder uiBinder = GWT
			.create(BodyFormulaireUiBinder.class);

	interface BodyFormulaireUiBinder extends UiBinder<Element, BodyFormulaire> {
	}

	public BodyFormulaire() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public AnchorElement getLink_allocation() {
		return link_allocation;
	}

	public void setLink_allocation(AnchorElement link_allocation) {
		this.link_allocation = link_allocation;
	}
	

}
