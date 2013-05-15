package com.applifit.bilifit.client.outils;

import com.google.gwt.user.client.Cookies;



/***
 * Objectif: Regrouper toutes les constantes et les variables de configuration du projet
 * 
 * ***/
public class Constantes {

	// En mode developpement
//	 public static final String SUFFIXE_URL = "?gwt.codesvr=127.0.0.1:9997";

	// En mode prod
	 public static final String SUFFIXE_URL = "";

	// Le nom de l'utilisateur connecté
	public static final String USER = Cookies.getCookie("prenom") +" "+ Cookies.getCookie("nom");

	/*** L'adresse email pour l'envoie des emails de confirmation ***/
//	public static final String EMAIL = "bilifit205@gmail.com";
	public static final String EMAIL = "nkt.nguyen.kim.trong@gmail.com";
	//public static final String EMAIL = "meryem.elhadi@gmail.com";
	
	//Le nombre de colonne dans la table de création 
	public static final int nbrColonne = 3;
	
	//Le nombre de ligne dans la table de création 
	public static final int nbrLigne = 50;
	
}
