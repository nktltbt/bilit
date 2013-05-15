package com.applifit.bilifit.server.formulaires;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;
import com.applifit.bilifit.server.comptes.KeyJson.CompteKeyDeserializer;
import com.applifit.bilifit.server.comptes.KeyJson.CompteKeySerializer;
import com.applifit.bilifit.server.comptes.KeyJson.CompteListKeySerializer;
import com.applifit.bilifit.server.comptes.KeyJson.EntrepriseKeyDeserializer;
import com.applifit.bilifit.server.comptes.KeyJson.EntrepriseKeySerializer;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;


@Entity
public class Formulaire {

	@Id Long id;
	String nom;
	String commentaire;
	int version;
	String date_creation;
	Key<Entreprise> entreprise;
	Key<Compte> creator;
	List <Key<Compte>> users ;
	//Pour désactiver le formulaire au lieu de le supprimer
	int etat = 1;
	transient String nom_bis ;
	
	public Formulaire() {
		super();
		this.users = new ArrayList<Key<Compte>>();
	}
	
	public Formulaire(String nom, String commentaire, int version) {
		super();
		this.nom = nom;
		this.commentaire = commentaire;
		this.version = version;
		nom_bis = nom.toLowerCase(); 
	}
	
	public Formulaire(String nom, String commentaire, int version,
			Key<Entreprise> entreprise, Key<Compte> creator) {
		super();
		this.nom = nom;
		this.commentaire = commentaire;
		this.version = version;
		this.entreprise = entreprise;
		this.creator = creator;
		nom_bis = nom.toLowerCase(); 
	}

	public Formulaire(String nom, String commentaire, int version,
			String date_creation, Key<Entreprise> entreprise,
			Key<Compte> creator) {
		super();
		this.nom = nom;
		this.commentaire = commentaire;
		this.version = version;
		this.date_creation = date_creation;
		this.entreprise = entreprise;
		this.creator = creator;
		nom_bis = nom.toLowerCase(); 
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
		nom_bis = nom.toLowerCase(); 
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getDate_creation() {
		return date_creation;
	}

	public void setDate_creation(String date_creation) {
		this.date_creation = date_creation;
	}

	@JsonSerialize(using = EntrepriseKeySerializer.class)
	public Key<Entreprise> getEntreprise() {
		return entreprise;
	}
	@JsonDeserialize(using = EntrepriseKeyDeserializer.class)
	public void setEntreprise(Key<Entreprise> entreprise) {
		this.entreprise = entreprise;
	}

	@JsonSerialize(using = CompteKeySerializer.class)
	public Key<Compte> getCreator() {
		return creator;
	}
	@JsonDeserialize(using = CompteKeyDeserializer.class)
	public void setCreator(Key<Compte> creator) {
		this.creator = creator;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	@JsonSerialize(using = CompteListKeySerializer.class)
	public List<Key<Compte>> getUsers() {
		return users;
	}


	public void setUsers(List<Key<Compte>> users) {
		this.users = users;
	}

    public void addUser(Key<Compte> user){
    	users.add(user);
    }
	
	
}
