package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class FormFormulaire extends UIObject {
	
	@UiField 
	protected InputElement form_nom;
	
	@UiField 
	protected InputElement form_comment;

	private static FormFormulaireUiBinder uiBinder = GWT
			.create(FormFormulaireUiBinder.class);

	interface FormFormulaireUiBinder extends UiBinder<Element, FormFormulaire> {
	}

	public FormFormulaire() {
		setElement(uiBinder.createAndBindUi(this));
	
	}

	public InputElement getForm_nom() {
		return form_nom;
	}

	public void setForm_nom(InputElement form_nom) {
		this.form_nom = form_nom;
	}

	public InputElement getForm_comment() {
		return form_comment;
	}

	public void setForm_comment(InputElement form_comment) {
		this.form_comment = form_comment;
	}

	
}
