package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class Body_menu_formulaire extends UIObject {

	
	@UiField
	protected AnchorElement creation;
	
	@UiField
	protected AnchorElement modification;
	
	private static Body_menu_formulaireUiBinder uiBinder = GWT
			.create(Body_menu_formulaireUiBinder.class);

	interface Body_menu_formulaireUiBinder extends
			UiBinder<Element, Body_menu_formulaire> {
	}

	public Body_menu_formulaire() {
		setElement(uiBinder.createAndBindUi(this));
	}

	/**
	 * @return the creation
	 */
	public AnchorElement getCreation() {
		return creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(AnchorElement creation) {
		this.creation = creation;
	}

	/**
	 * @return the modification
	 */
	public AnchorElement getModification() {
		return modification;
	}

	/**
	 * @param modification the modification to set
	 */
	public void setModification(AnchorElement modification) {
		this.modification = modification;
	}

	public static Body_menu_formulaireUiBinder getUiBinder() {
		return uiBinder;
	}

	public static void setUiBinder(Body_menu_formulaireUiBinder uiBinder) {
		Body_menu_formulaire.uiBinder = uiBinder;
	}
	
	

}
