package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

public class BodyApercu extends UIObject {

	@UiField
	protected InputElement nom;
	
	@UiField
	protected InputElement comment;
	
	@UiField
	protected AnchorElement retour;
	
	@UiField
	protected DivElement part1;
	
	@UiField
	protected DivElement part2;
	
	@UiField
	protected DivElement part3;
	
	
	private static BodyApercuUiBinder uiBinder = GWT
			.create(BodyApercuUiBinder.class);

	interface BodyApercuUiBinder extends UiBinder<Element, BodyApercu> {
	}

	public BodyApercu() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public InputElement getNom() {
		return nom;
	}

	public void setNom(InputElement nom) {
		this.nom = nom;
	}

	public InputElement getComment() {
		return comment;
	}

	public void setComment(InputElement comment) {
		this.comment = comment;
	}

	/**
	 * @return the retour
	 */
	public AnchorElement getRetour() {
		return retour;
	}

	/**
	 * @param retour the retour to set
	 */
	public void setRetour(AnchorElement retour) {
		this.retour = retour;
	}

	/**
	 * @return the part1
	 */
	public DivElement getPart1() {
		return part1;
	}

	/**
	 * @param part1 the part1 to set
	 */
	public void setPart1(DivElement part1) {
		this.part1 = part1;
	}

	/**
	 * @return the part2
	 */
	public DivElement getPart2() {
		return part2;
	}

	/**
	 * @param part2 the part2 to set
	 */
	public void setPart2(DivElement part2) {
		this.part2 = part2;
	}

	/**
	 * @return the part3
	 */
	public DivElement getPart3() {
		return part3;
	}

	/**
	 * @param part3 the part3 to set
	 */
	public void setPart3(DivElement part3) {
		this.part3 = part3;
	}
	
	

}
