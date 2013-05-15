package com.applifit.bilifit.client.comptes;

import com.applifit.bilifit.client.outils.BuildJsonObject;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.templatesUibinder.comptes.AjoutClient;
import com.applifit.bilifit.client.templatesUibinder.comptes.HeaderAno;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

public class CreerCompte implements EntryPoint {

	private AjoutClient formClient;
	private static final String Entreprise_URL = GWT.getHostPageBaseURL()
			+ "entreprise/";
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()
			+ "compte/";

	@Override
	public void onModuleLoad() {
		pageAjout();
	}

	public static native void nativeMethod(String id)
	/*-{
		$wnd.$('.' + id).datepicker({
			dateFormat : 'dd/mm/yy'
		});
	}-*/;

	/*** Fonction de recherche d'un client ***/

	/*** Créer la page d'ajout avec l'action du button ajouter ***/
	private void pageAjout() {
		HeaderAno menu = new HeaderAno();
		Document.get().getBody().appendChild(menu.getElement());
		formClient = new AjoutClient();
		Document.get().getBody().appendChild(formClient.getElement());
		nativeMethod("datepicker");

		Button buttonWrapper = Button.wrap(formClient.getEnregistrer());

		buttonWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ajouterClient();

			}

		});

		buttonWrapper = Button.wrap(formClient.getRetour());

		buttonWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.reload();

			}

		});
	}

	/*** Récupertaion des champs du formulaire et ajout d'un client ***/
	private void ajouterClient() {
		final String rs = (formClient.getForm_RS()).getValue().trim();
		final String siret = (formClient).getSiret().getValue().trim();
		final String adresse = (formClient.getAdresse()).getValue().trim();
		final String ville = (formClient.getVille()).getValue().trim();
		final String cp = (formClient.getCp()).getValue().trim();
		final String pays = (formClient.getPays()).getValue().trim();
		final String offre = (formClient.getOffre()).getValue().trim();
		final String dateString = (formClient.getDate()).getValue().trim();
		final String contact_nom = (formClient.getContact_nom()).getValue()
				.trim();
		final String contact_prenom = (formClient.getContact_prenom())
				.getValue().trim();
		final String contact_mail = (formClient.getContact_mail()).getValue()
				.trim();
		final String contact_tel = (formClient.getContact_tel()).getValue()
				.trim();
		final String contact_fonction = (formClient.getContact_fonction())
				.getValue().trim();
		final String quota_formulaire = (formClient.getQuota_formulaire())
				.getValue().trim();
		final String quota_utilisateur = (formClient.getQuota_utilisateur())
				.getValue().trim();

		if (rs.isEmpty()) {
			Window.alert("Le champs Raison sociale est obligatoire");
			return;
		}

		if (siret.isEmpty()) {
			Window.alert("Le champs SIRET est obligatoire");
			return;
		}

		if (adresse.isEmpty()) {
			Window.alert("Le champs Adresse est obligatoire");
			return;
		}

		if (ville.isEmpty()) {
			Window.alert("Le champs Ville est obligatoire");
			return;
		}

		if (cp.isEmpty()) {
			Window.alert("Le champs Code Postal est obligatoire");
			return;
		}

		if (offre.isEmpty()) {
			Window.alert("Le champs Offre souscrite est obligatoire");
			return;
		}

		if (dateString.isEmpty()) {
			Window.alert("Le champs Date est obligatoire");
			return;
		}

		if (contact_nom.isEmpty()) {
			Window.alert("Le champs Nom est obligatoire");
			return;
		}

		if (contact_prenom.isEmpty()) {
			Window.alert("Le champs Prenom est obligatoire");
			return;
		}

		if (contact_mail.isEmpty()) {
			Window.alert("Le champs Mail est obligatoire");
			return;
		}

		if (!contact_mail
				.matches("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")) {
			Window.alert("'" + contact_mail + "': Adresse Email invalide");
			return;
		}

		try {
			Integer.parseInt(quota_formulaire);
		} catch (Exception e) {
			Window.alert("Le champs 'Nombre de formulaires de collecte' doit être un nombre entier");
			return;
		}

		try {
			Integer.parseInt(quota_utilisateur);
		} catch (Exception e) {
			Window.alert("Le champs 'Nombre d'utilisateurs de Bilifit' doit être un nombre entier");
			return;
		}

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				COMPTE_URL + "recupererCompte/email/" + contact_mail);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération de mail");
				}

				public void onResponseReceived(Request request,
						Response response) {

					String compteAsJSONString = response.getText();
					if (!compteAsJSONString.isEmpty()) {
						Window.alert(contact_mail
								+ ": Cette adresse email correspond à un utilisateur actif");
						return;
					} else {
						RequestBuilder builder = new RequestBuilder(
								RequestBuilder.GET, Entreprise_URL
										+ "recupererEntreprise/raisonsocial/"
										+ rs);
						builder.setHeader("Content-Type", "application/json");
						try {

							builder.sendRequest(null, new RequestCallback() {

								public void onError(Request request,
										Throwable exception) {
									Window.alert("erreur de récupération d'entreprise");
								}

								public void onResponseReceived(Request request,
										Response response) {
									String compteAsJSONString = response
											.getText();
									if (!compteAsJSONString.isEmpty()) {
										Window.alert(rs
												+ ": Cette raison sociale correspond à une entreprise cliente");
										return;
									} else {
										RequestBuilder builder = new RequestBuilder(
												RequestBuilder.POST,
												Entreprise_URL
														+ "ajouterEntreprise");
										builder.setHeader("Content-Type",
												"application/json");

										final JSONObject entrepriseAsJSONObject = BuildJsonObject
												.buildJSONObjectEntreprise(
														null, rs, siret,
														adresse, ville, cp,
														pays, offre,
														dateString,
														contact_nom,
														contact_prenom,
														contact_mail,
														contact_tel,
														contact_fonction,
														quota_formulaire,
														quota_utilisateur);
										try {
											builder.sendRequest(
													entrepriseAsJSONObject
															.toString(),
													new RequestCallback() {
														public void onError(
																Request request,
																Throwable exception) {
															Window.alert("Erreur d'ajout");
														}

														public void onResponseReceived(
																Request request,
																Response response) {
															Window.alert("Ajout avec succès");
															Window.Location.replace(GWT.getHostPageBaseURL()
																	+ "Connexion.html" + Constantes.SUFFIXE_URL);
														}
													});
										} catch (RequestException e) {
											System.out
													.println("RequestException");
										}

									}
								}
							});

						} catch (RequestException e) {
							Window.alert("RequestException");
						}
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}

	}

}