package com.applifit.bilifit.server.formulaires;

import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.applifit.bilifit.server.formulaires.KeyJson.FormulaireKeyDeserializer;
import com.applifit.bilifit.server.formulaires.KeyJson.FormulaireKeySerializer;
import com.google.gwt.editor.client.Editor.Ignore;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;


@Entity
public class Element {

	@Id Long id;
	String type;
	int position_x;
	int position_y;
	@Ignore
	int ordre;
	Key<Formulaire> formulaire;
	
	
	public Element() {
		super();
	}
	
	
	public Element(String type, int position_x, int position_y) {
		super();
		this.type = type;
		this.position_x = position_x;
		this.position_y = position_y;
		ordre = 3*position_y + position_x;
	}

	
	public Element(String type, int position_x, int position_y,
			Key<Formulaire> formulaire) {
		super();
		this.type = type;
		this.position_x = position_x;
		this.position_y = position_y;
		this.formulaire = formulaire;
		ordre = 3*position_y + position_x;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPosition_x() {
		return position_x;
	}

	public void setPosition_x(int position_x) {
		this.position_x = position_x;
	}

	public int getPosition_y() {
		return position_y;
	}

	public void setPosition_y(int position_y) {
		this.position_y = position_y;
	}

	@JsonSerialize(using = FormulaireKeySerializer.class)
	public Key<Formulaire> getFormulaire() {
		return formulaire;
	}

	@JsonDeserialize(using = FormulaireKeyDeserializer.class)
	public void setFormulaire(Key<Formulaire> formulaire) {
		this.formulaire = formulaire;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the ordre
	 */
	public int getOrdre() {
		return 3*position_y+position_x;
	}


	/**
	 * @param ordre the ordre to set
	 */
	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}
	
	
	
}
