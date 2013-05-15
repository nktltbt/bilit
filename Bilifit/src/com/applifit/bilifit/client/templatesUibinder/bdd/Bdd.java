package com.applifit.bilifit.client.templatesUibinder.bdd;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;



public class Bdd extends UIObject {
	
	
	@UiField 
	protected SelectElement selectForm;
	
	
	@UiField
	protected AnchorElement export;
	
	
	@UiField
	protected DivElement divtest;

	private static BddUiBinder uiBinder = GWT.create(BddUiBinder.class);

	interface BddUiBinder extends UiBinder<Element, Bdd> {
	}

	public Bdd() {
		setElement(uiBinder.createAndBindUi(this));
	}



	/**
	 * @return the uiBinder
	 */
	public static BddUiBinder getUiBinder() {
		return uiBinder;
	}

	/**
	 * @param uiBinder the uiBinder to set
	 */
	public static void setUiBinder(BddUiBinder uiBinder) {
		Bdd.uiBinder = uiBinder;
	}

	public SelectElement getSelectForm() {
		return selectForm;
	}

	public void setSelectForm(SelectElement selectForm) {
		this.selectForm = selectForm;
	}

	/**
	 * @return the export
	 */
	public AnchorElement getExport() {
		return export;
	}

	/**
	 * @param export the export to set
	 */
	public void setExport(AnchorElement export) {
		this.export = export;
	}

	/**
	 * @return the divtest
	 */
	public DivElement getDivtest() {
		return divtest;
	}

	/**
	 * @param divtest the divtest to set
	 */
	public void setDivtest(DivElement divtest) {
		this.divtest = divtest;
	}
	
	
	
}
