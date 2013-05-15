/**
 * 
 */
package com.applifit.bilifit.client.templatesUibinder.formulaires;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

/**
 * @author Meryem
 *
 */
public class BodyConsultation extends UIObject {

	@UiField
	protected TableSectionElement tbody;
	
	@UiField
	protected AnchorElement lien_enregistrer;
	
	@UiField
	protected DivElement part1;
	
	@UiField
	protected DivElement part2;
	
	@UiField
	protected DivElement part3;
	
	private static BodyConsultationUiBinder uiBinder = GWT
			.create(BodyConsultationUiBinder.class);

	interface BodyConsultationUiBinder extends
			UiBinder<Element, BodyConsultation> {
	}

	public BodyConsultation() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public TableSectionElement getTbody() {
		
		return tbody;
	}

	public void setTbody(TableSectionElement tbody) {
		this.tbody = tbody;
	}

	public AnchorElement getLien_enregistrer() {
		return lien_enregistrer;
	}

	public void setLien_enregistrer(AnchorElement lien_enregistrer) {
		this.lien_enregistrer = lien_enregistrer;
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
