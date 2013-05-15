package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class ListeClient extends UIObject {
	
	@UiField
	protected TableSectionElement tbody;


	public void setTbody(TableSectionElement tbody) {
		this.tbody = tbody;
	}

	private static ListeClientUiBinder uiBinder = GWT
			.create(ListeClientUiBinder.class);

	interface ListeClientUiBinder extends UiBinder<Element, ListeClient> {
	}

	public ListeClient() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public TableSectionElement getTbody() {
		return tbody;
	}

	public void setTbodySa(TableSectionElement tbody) {
		this.tbody = tbody;
	}

}
