package com.applifit.bilifit.server.formulaires;

import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.KeyJson.CompteKeyDeserializer;
import com.applifit.bilifit.server.comptes.KeyJson.CompteKeySerializer;
import com.applifit.bilifit.server.formulaires.KeyJson.ElementKeyDeserializer;
import com.applifit.bilifit.server.formulaires.KeyJson.ElementKeySerializer;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class Element_valeur {

	@Id Long id;
	String valeur;
	Key<Element> element;
	Key<Compte> compte;
	String date;
	
	//Pour regrouper les valeurs saisies pour une collecte
	int indice;
	
	
	public Element_valeur() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValeur() {
		return valeur;
	}
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	
	public Element_valeur(String valeur, Key<Element> element, Key<Compte> compte) {
		super();
		this.valeur = valeur;
		this.element = element;
		this.compte = compte;
	}
	
	public Element_valeur(String valeur, Key<Element> element, Key<Compte> compte, String date, int indice) {
		super();
		this.valeur = valeur;
		this.element = element;
		this.compte = compte;
		this.date = date;
		this.indice = indice;
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
	
	/*** Utiliser la classe de sérialisation developpée pour l'objet Key<Compte> **/
	@JsonSerialize(using = CompteKeySerializer.class)
	public Key<Compte> getCompte() {
		return compte;
	}
	
	/*** Utiliser la classe de desérialisation developpée pour l'objet Key<Compte> **/
	@JsonDeserialize(using = CompteKeyDeserializer.class)
	public void setCompte(Key<Compte> compte) {
		this.compte = compte;
	}
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	} 
	
	
	
}
