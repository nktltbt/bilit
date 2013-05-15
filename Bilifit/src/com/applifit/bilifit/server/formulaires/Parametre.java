package com.applifit.bilifit.server.formulaires;

import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.applifit.bilifit.server.formulaires.KeyJson.ElementKeyDeserializer;
import com.applifit.bilifit.server.formulaires.KeyJson.ElementKeySerializer;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;


@Entity
public class Parametre {
	
	@Id Long id;
	String nom;
	String valeur;
	Key<Element> element;
	
	public Parametre() {
		super();
	}
	
	public Parametre(String nom, String valeur, Key<Element> element) {
		super();
		this.nom = nom;
		this.valeur = valeur;
		this.element = element;
	}


	public Parametre(Long id, String nom, String valeur, Key<Element> element) {
		super();
		this.id = id;
		this.nom = nom;
		this.valeur = valeur;
		this.element = element;
	}

	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}



	public String getValeur() {
		return valeur;
	}



	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	/*** Utiliser la classe de sérialisation developpée pour l'objet Key<Element> **/
	@JsonSerialize(using = ElementKeySerializer.class)
	public Key<Element> getElement() {
		return element;
	}

	/*** Utiliser la classe de desérialisation developpée pour l'objet Key<Element> **/
	@JsonDeserialize(using = ElementKeyDeserializer.class)
	public void setElement(Key<Element> element) {
		this.element = element;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
	
	

}
