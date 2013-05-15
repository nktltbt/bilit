package com.applifit.bilifit.client.outils;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/***
 * Objectif: Définir tous les fonctions de parsing entre les données et les
 * objets json
 * 
 * comme régle de nomination, nous appelons toujours la fonction
 * buildJSONObject[le nom de l'objet]
 * ***/

public class BuildJsonObject {

	/*** Créer les objets Json correspondant à la classe Compte ***/
	public static JSONObject buildJSONObjectCompte(String id, String nom, String prenom, String mdp, String fonction, String mail,
		String profil, JSONObject entrepriseJson) {
		JSONObject compteAsJSONObject = new JSONObject();
		compteAsJSONObject.put("id", new JSONString(id));
		compteAsJSONObject.put("nom", new JSONString(nom));
		compteAsJSONObject.put("prenom", new JSONString(prenom));
		if (mdp != null  && !mdp.isEmpty())
			compteAsJSONObject.put("password", new JSONString(mdp));
		compteAsJSONObject.put("fonction", new JSONString(fonction));
		compteAsJSONObject.put("mail", new JSONString(mail));
		compteAsJSONObject.put("profil", new JSONString(profil));
		compteAsJSONObject.put("etat", new JSONString("1"));
		compteAsJSONObject.put("entreprise", entrepriseJson);
		if(entrepriseJson != null && entrepriseJson.get("etat").isNumber().toString().equals("0"))
			compteAsJSONObject.put("desactivation", new JSONString("1"));
		return compteAsJSONObject;
	}

	/*** Créer les objets Json correspondant à la classe Formulaire ***/
	public static JSONObject buildJSONObjectFormBis(String id, String nom, String comment, String version, String date_creation, String etat,  String entreprise) {
		JSONObject formAsJSONObject = new JSONObject();
		formAsJSONObject.put("id", new JSONString(id));
		formAsJSONObject.put("nom", new JSONString(nom));
		formAsJSONObject.put("commentaire", new JSONString(comment));
		formAsJSONObject.put("version", new JSONString(version));
		formAsJSONObject.put("date_creation", new JSONString(date_creation));
		formAsJSONObject.put("entreprise", new JSONString(entreprise));
		formAsJSONObject.put("etat", new JSONString(etat));
		formAsJSONObject.put("elements", null);
		return formAsJSONObject;
	}

	
	/*** Créer les objets Json correspondant à la classe Element ***/
	public static JSONObject buildJSONObjectElementBis(String id, String type,
			String position_x, String position_y) {
		JSONObject elementAsJSONObject = new JSONObject();
		elementAsJSONObject.put("id", new JSONString(id));
		elementAsJSONObject.put("type", new JSONString(type));
		elementAsJSONObject.put("position_x", new JSONString(position_x));
		elementAsJSONObject.put("position_y", new JSONString(position_y));
		return elementAsJSONObject;
	}
	
	/*** Créer les objets Json correspondant à la classe Parametre ***/
	public static JSONObject buildJSONObjectParametreBis(String id, String paramName, String ParamValue, String element) {
		JSONObject paramAsJSONObject = new JSONObject();
		paramAsJSONObject.put("id", new JSONString(id));
		paramAsJSONObject.put("nom", new JSONString(paramName));
		paramAsJSONObject.put("valeur", new JSONString(ParamValue));
		paramAsJSONObject.put("element", new JSONString(element));
		return paramAsJSONObject;
	}

	/*** Créer les objets Json correspondant à la classe Entreprise ***/
	public static JSONObject buildJSONObjectEntreprise(String id , String rs, String siret,
			String adresse, String ville, String cp, String pays, String offre,
			String dateString, String contact_nom, String contact_prenom,
			String contact_mail, String contact_tel, String contact_fonction,
			String quota_formulaire, String quota_utilisateur) {

		JSONObject entrepriseAsJSONObject = new JSONObject();
		if(id == null)
			entrepriseAsJSONObject.put("id", new JSONString("-1"));
		else entrepriseAsJSONObject.put("id", new JSONString(id));
		entrepriseAsJSONObject.put("raison_sociale", new JSONString(rs));
		entrepriseAsJSONObject.put("siret", new JSONString(siret));
		entrepriseAsJSONObject.put("adresse", new JSONString(adresse));
		entrepriseAsJSONObject.put("ville", new JSONString(ville));
		entrepriseAsJSONObject.put("code_postal", new JSONString(cp));
		entrepriseAsJSONObject.put("pays", new JSONString(pays));
		entrepriseAsJSONObject.put("offre_souscrite", new JSONString(offre));
		entrepriseAsJSONObject.put("date", new JSONString(dateString));
		entrepriseAsJSONObject.put("contact_nom", new JSONString(contact_nom));
		entrepriseAsJSONObject.put("contact_prenom", new JSONString(contact_prenom));
		entrepriseAsJSONObject.put("contact_mail", new JSONString(contact_mail));
		entrepriseAsJSONObject.put("contact_tel", new JSONString(contact_tel));
		entrepriseAsJSONObject.put("contact_fonction", new JSONString(
				contact_fonction));
		entrepriseAsJSONObject.put("quota_formulaire", new JSONString(
				quota_formulaire));
		entrepriseAsJSONObject.put("quota_utilisateur", new JSONString(
				quota_utilisateur));

		return entrepriseAsJSONObject;
	}
	


}
