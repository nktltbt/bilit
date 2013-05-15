/**
 * 
 */
package com.applifit.bilifit.client.templatesUibinder.comptes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;


/**
 * @author Meryem
 *
 */
public class ContainerSA extends UIObject {

	private static ContainerSAUiBinder uiBinder = GWT.create(ContainerSAUiBinder.class);
	
	@UiField
	protected InputElement nom;
	
	@UiField
	protected InputElement prenom;
	
	@UiField
	protected InputElement mail;
	
	@UiField
	protected InputElement fonction;
	
	@UiField
	protected AnchorElement linkajout;
	
	@UiField
	protected TableSectionElement tbodySa;
	
	@UiField 
	protected TableRowElement rowSa;
	
	@UiField
	protected ButtonElement rechercher;

	interface ContainerSAUiBinder extends UiBinder<Element, ContainerSA> {
	}

	public ContainerSA() {
		
		setElement(uiBinder.createAndBindUi(this));
		
	}
	
	public AnchorElement getLinkajout(){
		return linkajout;
	}


	public TableSectionElement getTbodySa() {
		return tbodySa;
	}

	public void setTbodySa(TableSectionElement tbodySa) {
		this.tbodySa = tbodySa;
	}
	
	public ButtonElement getButtonRechercher() {
		return rechercher;
	}

	/**
	 * @return the nom
	 */
	public InputElement getNom(){
		return nom;
	}

	/**
	 * @return the prenom
	 */
	public InputElement getPrenom() {
		return prenom;
	}

	/**
	 * @return the mail
	 */
	public InputElement getMail() {
		return mail;
	}

	
	/**
	 * @return the fonction
	 */
	public InputElement getFonction() {
		return fonction;
	}


}
