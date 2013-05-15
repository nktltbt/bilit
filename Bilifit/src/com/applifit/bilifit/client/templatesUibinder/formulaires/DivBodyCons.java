package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class DivBodyCons extends UIObject {

	
	@UiField
	protected TableSectionElement tbody;
	
	private static DivBodyConsUiBinder uiBinder = GWT
			.create(DivBodyConsUiBinder.class);

	interface DivBodyConsUiBinder extends UiBinder<Element, DivBodyCons> {
	}

	public DivBodyCons() {
		setElement(uiBinder.createAndBindUi(this));
	}
	
	public TableSectionElement getTbody() {
		
		return tbody;
	}

	public void setTbody(TableSectionElement tbody) {
		this.tbody = tbody;
	}

}
