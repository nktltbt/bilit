package com.applifit.bilifit.client.formulaires;

import java.util.ArrayList;

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
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.applifit.bilifit.client.outils.BuildJsonObject;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.formulaires.FormulaireBoard;
import com.applifit.bilifit.client.formulaires.FormulaireController;
import com.applifit.bilifit.client.templatesUibinder.formulaires.BodyApercu;
import com.applifit.bilifit.client.templatesUibinder.formulaires.BodyFormulaire;
import com.applifit.bilifit.client.templatesUibinder.formulaires.FormFormulaire;
import com.applifit.bilifit.client.templatesUibinder.formulaires.MenuForm;

public class Formulaire_applifit implements EntryPoint {

	/***
	 * Les url pour l'utilisation des services Rest pour les objets Formulaire,
	 * Entreprise, Element et Parametre
	 ***/
	private static final String FORMULAIRE_URL = GWT.getHostPageBaseURL()
			+ "formulaire/";
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()
			+ "compte/";
	private Formulaire_applifit fa;
	private FormFormulaire form = new FormFormulaire();
	private BodyFormulaire body = new BodyFormulaire();
	private BodyApercu bodyapercu = new BodyApercu();
	JSONValue entrepriseAsJSONValue;
	JSONObject formAsJSONObject;
	String nom;
	@SuppressWarnings("rawtypes")
	ArrayList parametres = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList choices = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList options = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList elements = new ArrayList();
	final Anchor enregistrer = new Anchor();
	DualListBox dualListBox = new DualListBox();

	boolean last = false;
	boolean paramlast = false;

	JSONObject formAsJSONObjectBis;

	@Override
	public void onModuleLoad() {

		/***
		 * Seuls les profils Super Adminisatreur et CA peuvent se connecter à
		 * cette page
		 ***/

		if (Cookies.getCookie("profil") == null) {
			Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html"
					+ Constantes.SUFFIXE_URL);
		} else if (Cookies.getCookie("client") == null)
			Window.Location.replace(GWT.getHostPageBaseURL()
					+ "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
		else if (Cookies.getCookie("profil").equals("CU")) {
			Window.Location.replace(GWT.getHostPageBaseURL()
					+ "Accueil_applifit.html" + Constantes.SUFFIXE_URL);
		} else {
			fa = this;
			MenuForm menu = new MenuForm(Constantes.USER);
			Document.get().getBody().appendChild(menu.getElement());
			Document.get().getBody().appendChild(body.getElement());

			/*** action de deconnexion ***/
			Anchor deconnexionWrapper = Anchor.wrap(menu.getDeconnexion());
			deconnexionWrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Utils.deconnexion();
				}

			});

			/*** action Rubrique accueil ***/
			Anchor accueilWrapper = Anchor.wrap(menu.getAccueil());
			accueilWrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (Cookies.getCookie("profil").equals("SA"))
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Accueil_superadmin.html"
								+ Constantes.SUFFIXE_URL);
					else
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Accueil_applifit.html"
								+ Constantes.SUFFIXE_URL);
				}

			});
			/*** action de rubrique formulaire ***/
			Anchor formulaireWrapper = Anchor.wrap(menu.getFormulaire());
			formulaireWrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (Cookies.getCookie("profil").equals("SA")
							|| Cookies.getCookie("profil").equals("CA"))
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Formulaire_menu_applifit.html"
								+ Constantes.SUFFIXE_URL);
					else
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Formulaire_consulter_applifit.html"
								+ Constantes.SUFFIXE_URL);
				}

			});

			/*** Vérification du quota du nombre de formulaire ***/
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
					FORMULAIRE_URL + "etatFormulaire/idEntreprise/"
							+ Cookies.getCookie("client"));
			builder.setHeader("Content-Type", "application/json");

			try {
				builder.sendRequest(null, new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						Window.alert("Erreur de récuperation de nombre des formulaires!");
					}

					public void onResponseReceived(Request request,
							Response response) {
						if (response.getStatusCode() == Response.SC_OK) {
							Boolean etatFormulaire = Boolean
									.parseBoolean(response.getText());

							if (!etatFormulaire) {
								Utils.createDialogBox(
										"Attention !! Vous avez depassé le nombre maximal des formulaires")
										.show();
							}
						}
					}
				});
			} catch (RequestException e) {
				System.out.println("RequestException");
			}

			// Alimenter la liste des comptes CU et CA
			alimenterList();
			// Ajouter Action au lien "Allocation du formulaire"
			Anchor link_allocation = Anchor.wrap(body.getLink_allocation());
			link_allocation.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					// Afficher le popup contenant la dual liste qui contient la
					// liste des comptes CA et CU
					FormulaireController.CreateDialogBoxAllocation(dualListBox,
							false, fa).show();
				}
			});

			final Anchor apercu = new Anchor();

			// Création du lien de l'aperçu
			apercu.setHTML("<a class='bouton_enregistrer floatRight' style='margin-right:13%; cursor:pointer'>Aperçu</a>");
			RootPanel.get("button_Enregistrer").add(apercu);
			apercu.addClickHandler(new ClickHandler() {
				@SuppressWarnings("rawtypes")
				public void onClick(ClickEvent event) {
					elements = new ArrayList();
					choices = new ArrayList();
					options = new ArrayList();
					parametres = new ArrayList();
					FormulaireController.recupererElements(choices, options,
							elements, parametres);
					bodyapercu = new BodyApercu();
					// Passer à l'interface de l'aperçu
					Document.get().getBody().removeChild(body.getElement());
					Document.get().getBody()
							.appendChild(bodyapercu.getElement());
					// Création de la page de l'aperçu
					FormulaireController
							.pageApercu(bodyapercu, form.getForm_nom()
									.getValue(), form.getForm_comment()
									.getValue(), elements, parametres, choices,
									options);

					Anchor retourAnchor = Anchor.wrap(bodyapercu.getRetour());

					retourAnchor.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							elements = new ArrayList();
							choices = new ArrayList();
							options = new ArrayList();
							parametres = new ArrayList();
							Document.get().getBody()
									.removeChild(bodyapercu.getElement());
							Document.get().getBody()
									.appendChild(body.getElement());
						}

					});

				}
			});

			/*** Ajouter le lien "Enregistrer" à lapage de création du formulaire ***/
			RootPanel.get("form_formulaire_creation").getElement()
					.appendChild(form.getElement());
			enregistrer
					.setHTML("<a class='bouton_enregistrer floatRight' style='margin-right:15px; cursor:pointer'>Enregistrer</a>");
			RootPanel.get("button_Enregistrer").add(enregistrer);
			RootPanel.get("contenu_principal_formulaire").add(
					new FormulaireBoard());
			RootPanel.get("container_principal").insert(
					FormulaireController.createImageGrid(), 0);

			// Action de l'enregistrement du formulaire dans la base de données
			enregistrer.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					MouseListBox mlb = dualListBox.getRight();
					ArrayList<Widget> widgets = mlb.widgetList();
					if (widgets.size() == 0) {
						if (Window
								.confirm("Souhaitez vous allouer ce formulaire à un collaborateur maintenant ?")) {
							FormulaireController.CreateDialogBoxAllocation(
									dualListBox, true, fa).show();
						} else {
							addFormulaire();
						}
					} else {
						addFormulaire();
					}
				}
			});

		}
	}

	public void addFormulaire() {
		FormulaireController.recupererElements(choices, options, elements,
				parametres);
		// Ajouter les elements regroupés dans la base de
		// données
		ajouterFormulaire(elements);
	}

	// Alimenter la liste des comptes CA et CU
	private void alimenterList() {
		// Récuperer la liste des comptes
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				COMPTE_URL + "listerCompteEntrepriseAU/client/"
						+ Cookies.getCookie("client"));
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération du formulaire");
				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					JSONValue comptesAsJSONValue = JSONParser.parse(response
							.getText());
					JSONArray comptesAsJSONArray = comptesAsJSONValue.isArray();
					// Definir la taille de la liste
					dualListBox.setLIST_SIZE(comptesAsJSONArray.size());
					dualListBox.construire("10em");
					// Alimenter la liste
					for (int i = 0; i < comptesAsJSONArray.size(); i++) {
						JSONValue compteAsJSONValue = comptesAsJSONArray.get(i);
						JSONObject compteAsJSONObject = compteAsJSONValue
								.isObject();
						// Ajouter le compte à la zone gauche de la liste
						dualListBox.addLeft(compteAsJSONObject.get("nom")
								.isString().stringValue()
								+ " "
								+ compteAsJSONObject.get("prenom").isString()
										.stringValue(),
								compteAsJSONObject.get("id").isNumber()
										.toString());
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}
	}

	@SuppressWarnings("rawtypes")
	private void ajouterFormulaire(final ArrayList elements) {

		// Récuperer les champs nom et commentaire
		nom = form.getForm_nom().getValue();
		final String comment = form.getForm_comment().getValue();

		// Vérifier Si les champs ne sont pa vide
		if (nom.isEmpty()) {
			Window.alert("Le champs nom est obligatoire");
			return;
		}
		if (comment.isEmpty()) {
			Window.alert("Le champs commentaire est obligatoire");
			return;
		}

		// Vérifier si le chmaps nom n'existe pas déja dans la base de données
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				FORMULAIRE_URL + "recupererFormulaire/nom/" + nom);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération du formulaire");
				}


				public void onResponseReceived(Request request,
						Response response) {
					String formAsJSONString = response.getText();

					if (!formAsJSONString.isEmpty()) {
						Window.alert(nom
								+ ": Nom de formulaire dupliqué, veuillez choisir un autre nom!");
						return;
					} else {

						formAsJSONObjectBis = BuildJsonObject
								.buildJSONObjectFormBis("-1", nom, comment,
										"1", Utils.getToday(), "1",
										Cookies.getCookie("client")).isObject();
						for (int i = 0; i < elements.size(); i++) {
							JSONObject elementAsJSONObject = BuildJsonObject
									.buildJSONObjectElementBis("" + i,
											((ArrayList) elements.get(i))
													.get(0).toString(),
											((ArrayList) elements.get(i))
													.get(1).toString(),
											((ArrayList) elements.get(i))
													.get(2).toString());
							formAsJSONObjectBis.put("element" + i,
									elementAsJSONObject);
							ajouterParametre(
									((ArrayList) elements.get(i)).get(1)
											.toString(), ((ArrayList) elements
											.get(i)).get(2).toString(), i,
									((ArrayList) elements.get(i)).get(3)
											.toString(), ((ArrayList) elements
											.get(i)).get(4).toString());
						}

						FormulaireController.allouerFormulaireBis(dualListBox,
								formAsJSONObjectBis);

						try {
							RequestBuilder builder = new RequestBuilder(
									RequestBuilder.POST, FORMULAIRE_URL
											+ "addForm");
							builder.setHeader("Content-Type",
									"application/json");
							builder.sendRequest(formAsJSONObjectBis.toString(),
									new RequestCallback() {
										public void onError(Request request,
												Throwable exception) {
											Window.alert("Erreur d'ajout");
										}

										public void onResponseReceived(
												Request request,
												Response response) {
											RootPanel.get("button_Enregistrer")
													.remove(enregistrer);
											Window.alert("Formulaire: Ajout avec succès");
											Window.Location.replace(GWT
													.getHostPageBaseURL()
													+ "Formulaire_menu_applifit.html"
													+ Constantes.SUFFIXE_URL);
										}

									});
						} catch (RequestException e) {
							System.out.println("RequestException");
						}

					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}
	}

	/***
	 * Enregistrer les paramètres liés à chaque Element (titre) Params: Les
	 * positions de l'elements (X,Y) et son emplacement dans la liste des
	 * elements
	 * ***/
	@SuppressWarnings("rawtypes")
	private void ajouterParametre(String position_x, String position_y,
			final int emplacement, final String name, final String option) {

		ArrayList params = (ArrayList) parametres.get(emplacement);
		int k = 0;
		for (int i = 0; i < params.size(); i++) {

			final ArrayList p = (ArrayList) params.get(i);

			/*********/
			JSONObject paramAsJSONObjectBis = BuildJsonObject
					.buildJSONObjectParametreBis("-1", "" + p.get(0),
							"" + p.get(1), "" + emplacement);
			/*********/

			formAsJSONObjectBis.put("parametre" + emplacement + i,
					paramAsJSONObjectBis);
			k = i + 1;
		}

		if (((ArrayList) elements.get(emplacement)).get(0).equals("checkbox")
				|| ((ArrayList) elements.get(emplacement)).get(0).equals(
						"radio")) {

			JSONObject paramAsJSONObjectBis = BuildJsonObject
					.buildJSONObjectParametreBis("-1", "libelles",
							"" + choices.get(Integer.parseInt(name)), ""
									+ emplacement);
			formAsJSONObjectBis.put("parametre" + emplacement + k,
					paramAsJSONObjectBis);
		} else if (((ArrayList) elements.get(emplacement)).get(0).equals(
				"combobox")) {
			JSONObject paramAsJSONObjectBis = BuildJsonObject
					.buildJSONObjectParametreBis("-1", "libelles",
							"" + options.get(Integer.parseInt(option)), ""
									+ emplacement);
			formAsJSONObjectBis.put("parametre" + emplacement + k,
					paramAsJSONObjectBis);
		}

		/************/

	}

}
