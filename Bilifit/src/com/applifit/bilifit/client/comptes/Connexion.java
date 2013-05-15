package com.applifit.bilifit.client.comptes;

import java.util.Date;

import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.comptes.Login;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;

public class Connexion implements EntryPoint {

	final Login login = new Login();
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()
			+ "compte/";

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		String id = Cookies.getCookie("mail");
		if (id == null) {
			// page d'authetification
			pageLogin();
		} else {
			// page d'acceil
			if (Utils.VerificationProfil("SA"))
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
			else
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "Accueil_applifit.html" + Constantes.SUFFIXE_URL);
		}
	}

	/*** Afficher le formulaire de connexion ***/
	public void pageLogin() {

		Document.get().getBody().appendChild(login.getElement());
		Button buttonConnexion = Button.wrap(login.getConnexion());

		buttonConnexion.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginAction();
			}
		});

		Anchor an = Anchor.wrap(login.getOublier());
		an.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				oublierAction();

			}
		});
	}

	/*** Action de clique sur le button de connexion ***/
	public void loginAction() {
		final String log = (login.getLogin()).getValue().trim();
		final String pass = (login.getPass()).getValue().trim();

		// Tous les champs sont obligatoires

		if (log.isEmpty()) {
			Window.alert("Le champs Login est obligatoire");
			return;
		}
		if (pass.isEmpty()) {
			Window.alert("Le champs Password est obligatoire");
			return;
		}

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				COMPTE_URL + "recupererCompte/email/" + log);
		builder.setHeader("Content-Type", "application/json");

		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération de mail");
				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					String compteAsJSONString = response.getText();
					if (compteAsJSONString.isEmpty()) {
						Window.alert("Login ou password incorrect");
						return;
					} else {
						JSONValue compteAsJSONValue = JSONParser
								.parse(compteAsJSONString);
						final JSONObject compteAsJSONObject = compteAsJSONValue
								.isObject();
						String password = compteAsJSONObject.get("password")
								.isString().stringValue();
						if (!Utils.cripterPass(pass).equals(password)) {
							Window.alert("Password incorrect");
						} else {

							final long DURATION = 1000 * 60 * 60 * 2; // Durée
																		// de la
																		// session
																		// 2h

							Date expires = new Date(System.currentTimeMillis()
									+ DURATION);

							// Enregistrer les données de l'utilisateur dans la
							// session
							Cookies.setCookie("id", compteAsJSONObject
									.get("id").isNumber().toString(), expires,
									null, "/", false);
							Cookies.setCookie("mail",
									compteAsJSONObject.get("mail").isString()
											.stringValue(), expires, null, "/",
									false);
							Cookies.setCookie("nom",
									compteAsJSONObject.get("nom").isString()
											.stringValue(), expires, null, "/",
									false);
							Cookies.setCookie("prenom",
									compteAsJSONObject.get("prenom").isString()
											.stringValue(), expires, null, "/",
									false);
							Cookies.setCookie("profil",
									compteAsJSONObject.get("profil").isString()
											.stringValue(), expires, null, "/",
									false);

							if (Utils.VerificationProfil("CA")
									|| Utils.VerificationProfil("CU")) {
								Cookies.setCookie("client", compteAsJSONObject
										.get("entreprise").isString()
										.stringValue());
							}
							Document.get().getBody()
									.removeChild(login.getElement());
							if (Utils.VerificationProfil("CA")
									|| Utils.VerificationProfil("CU")) {
								Window.Location.replace(GWT
										.getHostPageBaseURL()
										+ "Accueil_applifit.html"
										+ Constantes.SUFFIXE_URL);
							} else {
								Window.Location.replace(GWT
										.getHostPageBaseURL()
										+ "Accueil_superadmin.html"
										+ Constantes.SUFFIXE_URL);
							}
						}
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}

		String sessionID = "BilifitWeb";
		final long DURATION = 1000 * 60 * 60 * 2; // Durée de la session 2h
		Date expires = new Date(System.currentTimeMillis() + DURATION);
		Cookies.setCookie("sid", sessionID, expires, null, "/", false);

	}

	public void oublierAction() {
		final String log = (login.getLogin()).getValue().trim();

		if (log.isEmpty()) {
			Window.alert("Le champs Login est obligatoire");
			return;
		}

		RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
				COMPTE_URL + "oublierCompte/" + log);
		builder.setHeader("Content-Type", "application/json");

		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération de mail");
				}

				public void onResponseReceived(Request request,
						Response response) {
					Window.alert("un nouveau mot de pass a été envoyé vers l'adresse mail "
							+ log);
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}

	}

}
