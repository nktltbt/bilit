package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class FormRecherche extends UIObject {

	
	@UiField 
	protected SelectElement formulaire;
	
	@UiField
	protected InputElement search_nom;
	
	@UiField
	protected InputElement search_date;
	
	@UiField
	protected AnchorElement search_link;
	
	
	private static FormRechercheUiBinder uiBinder = GWT
			.create(FormRechercheUiBinder.class);

	interface FormRechercheUiBinder extends UiBinder<Element, FormRecherche> {
	}

	public FormRecherche() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public SelectElement getFormulaire() {
		return formulaire;
	}

	public void setFormulaire(SelectElement formulaire) {
		this.formulaire = formulaire;
	}

	public InputElement getSearch_nom() {
		return search_nom;
	}

	public void setSearch_nom(InputElement search_nom) {
		this.search_nom = search_nom;
	}

	public InputElement getSearch_date() {
		return search_date;
	}

	public void setSearch_date(InputElement search_date) {
		this.search_date = search_date;
	}

	public AnchorElement getSearch_link() {
		return search_link;
	}

	public void setSearch_link(AnchorElement search_link) {
		this.search_link = search_link;
	}
	
	

}
