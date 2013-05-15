package com.applifit.bilifit.client.formulaires;

import static com.applifit.bilifit.client.formulaires.FormulaireController.CHOICE;
import static com.applifit.bilifit.client.formulaires.FormulaireController.OPTION;

import java.util.ArrayList;

import com.applifit.bilifit.client.formulaires.FormulaireController.Position;
import com.applifit.bilifit.client.outils.BuildJsonObject;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.formulaires.BodyApercu;
import com.applifit.bilifit.client.templatesUibinder.formulaires.BodyFormulaire;
import com.applifit.bilifit.client.templatesUibinder.formulaires.FormFormulaire;
import com.applifit.bilifit.client.templatesUibinder.formulaires.FormRecherche;
import com.applifit.bilifit.client.templatesUibinder.formulaires.MenuForm;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Formulaire_modifier_applifit implements EntryPoint {

	// Les liens pour les services web
	private static final String FORMULAIRE_URL = GWT.getHostPageBaseURL()
			+ "formulaire/";
	private static final String ELEMENT_URL = GWT.getHostPageBaseURL()
			+ "element/";
	private static final String PARAM_URL = GWT.getHostPageBaseURL()
			+ "parametre/";
	private static final String Entreprise_URL = GWT.getHostPageBaseURL()
			+ "entreprise/";
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()
			+ "compte/";
	private static final String VALUE_URL = GWT.getHostPageBaseURL()
			+ "element_valeur/";

	// Les parties recherche, le body du formulaire et le formulaire contenant
	// le nom et le commentaire
	FormRecherche recherche = new FormRecherche();
	BodyFormulaire bodyForm;
	FormFormulaire form = new FormFormulaire();
	BodyApercu bodyapercu = new BodyApercu();
	DualListBox dualListBox = new DualListBox();

	static boolean bodyExist = false;

	Anchor enregistrer = new Anchor();
	final Anchor supprimer = new Anchor();

	String nom;
	String ancienNom;
	long formulaire_id;
	int lastVersion;

	@SuppressWarnings("rawtypes")
	ArrayList parametres = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList choices = new ArrayList();

	@SuppressWarnings("rawtypes")
	ArrayList options = new ArrayList();
	@SuppressWarnings("rawtypes")
	ArrayList elements = new ArrayList();

	JSONObject formAsJSONObject;
	JSONValue entrepriseAsJSONValue;

	String elementsAsString = "";
	static boolean changerVersion;

	boolean last = false;
	boolean paramlast = false;

	JSONObject formAsJSONObjectBisDupliquer;
	JSONObject formAsJSONObjectBisEcraser;
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

			// Ajouter le menu à la page
			MenuForm menu = new MenuForm(Constantes.USER);
			Document.get().getBody().appendChild(menu.getElement());

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

			// Ajouter la zone de recherche à la page
			Document.get().getBody()
					.insertAfter(recherche.getElement(), menu.getElement());
			nativeMethod("datepicker");
			// Remplire la liste des formulaires par les formulaires actifs liés
			// à la société courante
			FormulaireController.alimenterListeForm(
					FORMULAIRE_URL + "listerFormulaire/entreprise/"
							+ Cookies.getCookie("client"), recherche);

			// Ajouter action por le button de recherche
			Anchor search = Anchor.wrap(recherche.getSearch_link());
			search.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					final ListBox lb = ListBox.wrap(recherche.getFormulaire());
					// Vider la liste
					lb.clear();
					lb.addItem("Choisir un formulaire...", "");

					// Lancer la recherche du formulaire selon le nom et la date
					// de création
					FormulaireController.rechercheForm(recherche);

					// Ajouter action on change de la liste des formulaires ,
					// affichant le formulaire selectionné
					final ListBox lb2 = ListBox.wrap(recherche.getFormulaire());
					lb2.addChangeHandler(new ChangeHandler() {
						@SuppressWarnings("rawtypes")
						@Override
						public void onChange(ChangeEvent event) {
							elements = new ArrayList();
							parametres = new ArrayList();
							choices = new ArrayList();
							options = new ArrayList();
							// Si la ligne selectionnée n'est pas le prompt de
							// la liste
							if (lb.getSelectedIndex() != 0) {
								// Récuperer la valeur selectionnée
								formulaire_id = Long.parseLong(lb.getValue(lb
										.getSelectedIndex()));

								// Récuperer les elements et parametres du
								// formulaire et les afficher dans la zone de
								// modification
								chargerFormulaire();
							}
						}
					});
				}

			});

			// Ajouter action on change de la liste des formulaires , affichant
			// le formulaire selectionné
			final ListBox lb = ListBox.wrap(recherche.getFormulaire());

			lb.addChangeHandler(new ChangeHandler() {
				@SuppressWarnings("rawtypes")
				@Override
				public void onChange(ChangeEvent event) {
					elements = new ArrayList();
					parametres = new ArrayList();
					choices = new ArrayList();
					options = new ArrayList();
					// Si la ligne selectionnée n'est pas le prompt de la liste
					if (lb.getSelectedIndex() != 0) {
						// Récuperer la valeur selectionnée
						formulaire_id = Long.parseLong(lb.getValue(lb
								.getSelectedIndex()));
						// Récuperer les elements et parametres du formulaire et
						// les afficher dans la zone de modification
						chargerFormulaire();
					}
				}
			});
		}
	}

	// Charger la zone de modification par les differents elements du formulaire
	public void chargerFormulaire() {
		// Vider la zone de modification dans le cas où elle est occupée par un
		// autre formulaire
		if (bodyExist) {
			RootPanel.get("container_principal").clear();
			Document.get().getBody().removeChild(bodyForm.getElement());
			dualListBox.clear();
			enregistrer = new Anchor();

		}

		// Construire la zone de modification
		bodyForm = new BodyFormulaire();

		Document.get().getBody().appendChild(bodyForm.getElement());

		// Alimenter la liste des comptes CU et CA
		alimenterList();
		// Ajouter Action au lien "Allocation du formulaire"
		Anchor link_allocation = Anchor.wrap(bodyForm.getLink_allocation());
		link_allocation.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Afficher le popup contenant la dual liste qui contient la
				// liste des comptes CA et CU
				FormulaireController.CreateDialogBoxAllocation(dualListBox,
						false, null).show();
			}
		});

		// Ajouter le button supprimer Formulaire

		supprimer
				.setHTML("<a class='bouton_enregistrer floatRight' style='margin-right:13%; cursor:pointer'>Supprimer</a>");
		RootPanel.get("button_Enregistrer").add(supprimer);

		// RootPanel.get("form_formulaire_allocation").getElement().appendChild(dualListBox.getElement());
		RootPanel.get("form_formulaire_creation").getElement()
				.appendChild(form.getElement());
		enregistrer
				.setHTML("<a class='bouton_enregistrer floatRight' style='margin-right:15px; cursor:pointer'>Enregistrer</a>");
		RootPanel.get("button_Enregistrer").add(enregistrer);

		RootPanel.get("contenu_principal_formulaire")
				.add(new FormulaireBoard());
		RootPanel.get("container_principal").insert(
				FormulaireController.createImageGrid(), 0);
		bodyExist = true;

		// Remplir le formulaire nom et commentaire
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				FORMULAIRE_URL + formulaire_id);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						JSONValue formAsJSONValue = JSONParser.parse(response
								.getText());
						formAsJSONObject = formAsJSONValue.isObject();
						((InputElement) form.getForm_nom())
								.setValue(formAsJSONObject.get("nom")
										.isString().stringValue());
						((InputElement) form.getForm_comment())
								.setValue(formAsJSONObject.get("commentaire")
										.isString().stringValue());
						ancienNom = form.getForm_nom().getValue();
						// ((InputElement)form.getForm_nom()).setDisabled(true);
					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

		// Remplir la zone de création par les elements et les parametres
		builder = new RequestBuilder(RequestBuilder.GET, ELEMENT_URL
				+ "listerElement/formulaire/" + formulaire_id);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						String elementsAsJSONString = response.getText();
						JSONValue elementsAsJSONValue = JSONParser
								.parse(elementsAsJSONString);
						JSONArray elementsAsJSONArray = elementsAsJSONValue
								.isArray();
						for (int i = 0; i < elementsAsJSONArray.size(); i++) {

							JSONValue elementAsJSONValue = elementsAsJSONArray
									.get(i);
							JSONObject elementAsJSONObject = elementAsJSONValue
									.isObject();
							elementsAsString += elementAsJSONObject.get("id")
									.isNumber().toString()
									+ "-";
							getParamsByElement(
									Long.parseLong(elementAsJSONObject
											.get("id").isNumber().toString()),
									Integer.parseInt(elementAsJSONObject
											.get("position_y").isNumber()
											.toString()),
									Integer.parseInt(elementAsJSONObject
											.get("position_x").isNumber()
											.toString()), elementAsJSONObject
											.get("type").isString()
											.stringValue());
						}

					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

		// Action de suppression d'un formulaire
		supprimer.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Supprimer le formulaire
				RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
						FORMULAIRE_URL + "supprimer/" + formulaire_id);
				builder.setHeader("Content-Type", "application/json");
				try {
					builder.sendRequest(null, new RequestCallback() {
						public void onError(Request request, Throwable exception) {

						}

						public void onResponseReceived(Request request,
								Response response) {
							Window.Location.reload();
						}
					});
				} catch (RequestException e) {
					System.out.println("RequestException");
				}
			}
		});

		// Action de l'enregistrement du formulaire dans la base de données
		enregistrer.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				FormulaireController.recupererElements(choices, options,
						elements, parametres);
				// Ajouter les elements regroupés dans la base de données
				ajouterFormulaire(elements);

			}
		});

		final Anchor apercu = new Anchor();

		// Création du lien de l'aperçu
		apercu.setHTML("<a class='bouton_enregistrer floatRight' style='margin-right:15px; cursor:pointer'>Aperçu</a>");
		RootPanel.get("button_Enregistrer").add(apercu);
		apercu.addClickHandler(new ClickHandler() {
			@SuppressWarnings("rawtypes")
			public void onClick(ClickEvent event) {
				choices = new ArrayList();
				options = new ArrayList();
				elements = new ArrayList();
				parametres = new ArrayList();

				FormulaireController.recupererElements(choices, options,
						elements, parametres);
				bodyapercu = new BodyApercu();
				// Passer à l'interface de l'aperçu
				Document.get().getBody().removeChild(bodyForm.getElement());
				Document.get().getBody().appendChild(bodyapercu.getElement());
				// Création de la page de l'aperçu
				FormulaireController.pageApercu(bodyapercu, form.getForm_nom()
						.getValue(), form.getForm_comment().getValue(),
						elements, parametres, choices, options);

				Anchor retourAnchor = Anchor.wrap(bodyapercu.getRetour());

				retourAnchor.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						choices = new ArrayList();
						options = new ArrayList();
						elements = new ArrayList();
						parametres = new ArrayList();
						Document.get().getBody()
								.removeChild(bodyapercu.getElement());
						Document.get().getBody()
								.appendChild(bodyForm.getElement());
					}

				});

			}
		});
	}

	// Remplir les deux zones de la liste par les comptes
	@SuppressWarnings("rawtypes")
	private void alimenterList() {

		final ArrayList comptes = new ArrayList();
		final ArrayList allocation = new ArrayList();
		// Récuperer la liste de tous les comptes CA et CU
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				COMPTE_URL + "listerCompteEntrepriseAU/client/"
						+ Cookies.getCookie("client"));
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération du formulaire");
				}

				@SuppressWarnings({ "deprecation", "unchecked" })
				public void onResponseReceived(Request request,
						Response response) {

					JSONValue comptesAsJSONValue = JSONParser.parse(response
							.getText());
					JSONArray comptesAsJSONArray = comptesAsJSONValue.isArray();
					// Alimenter la liste
					for (int i = 0; i < comptesAsJSONArray.size(); i++) {
						JSONValue compteAsJSONValue = comptesAsJSONArray.get(i);
						JSONObject compteAsJSONObject = compteAsJSONValue
								.isObject();
						ArrayList compte = new ArrayList();
						compte.add(compteAsJSONObject.get("nom").isString()
								.stringValue()
								+ " "
								+ compteAsJSONObject.get("prenom").isString()
										.stringValue());
						compte.add(compteAsJSONObject.get("id").isNumber()
								.toString());
						comptes.add(compte);
					}
					// Récuperer la liste des comptes pour lesquels, nous avons
					// allouer cette version du formulaire
					RequestBuilder builder = new RequestBuilder(
							RequestBuilder.GET, FORMULAIRE_URL
									+ "listerCompteAllocation/formulaire/"
									+ formulaire_id);
					builder.setHeader("Content-Type", "application/json");
					try {

						builder.sendRequest(null, new RequestCallback() {

							public void onError(Request request,
									Throwable exception) {
								Window.alert("erreur de récupération du formulaire");
							}

							public void onResponseReceived(Request request,
									Response response) {
								JSONValue comptesAsJSONValue = JSONParser
										.parse(response.getText());
								JSONArray comptesAsJSONArray = comptesAsJSONValue
										.isArray();
								for (int i = 0; i < comptesAsJSONArray.size(); i++) {
									JSONValue compteAsJSONValue = comptesAsJSONArray
											.get(i);
									JSONObject compteAsJSONObject = compteAsJSONValue
											.isObject();
									ArrayList compte = new ArrayList();
									compte.add(compteAsJSONObject.get("nom")
											.isString().stringValue()
											+ " "
											+ compteAsJSONObject.get("prenom")
													.isString().stringValue());
									compte.add(compteAsJSONObject.get("id")
											.isNumber().toString());
									allocation.add(compte);
								}

								dualListBox.setLIST_SIZE(comptes.size());
								dualListBox.construire("10em");

								// Ajouter la liste des comptes auxquels nous
								// avons alloué le formulaire à la zone droite
								for (int i = 0; i < allocation.size(); i++) {
									comptes.remove(allocation.get(i));
									ArrayList compte = (ArrayList) allocation
											.get(i);
									dualListBox.addRight(compte.get(0)
											.toString(), compte.get(1)
											.toString());
								}

								// Ajouter la liste des comptes auxquels nous
								// n'avons pas encore alloué le formulaire à la
								// zone gauche
								for (int i = 0; i < comptes.size(); i++) {
									ArrayList compte = (ArrayList) comptes
											.get(i);
									dualListBox.addLeft(compte.get(0)
											.toString(), compte.get(1)
											.toString());
								}
							}
						});
					} catch (RequestException e) {
						Window.alert("RequestException");
					}

				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}

	}

	public void ajouterElement(JSONArray parametres, final int x, final int y,
			final String type) {

		final FormulaireController gc = FormulaireController.getInstance();
		FormulaireBoard cb = gc.getCheckerBoard();
		Widget element = null;
		TextBox label = new TextBox();
		label.addStyleName("inputLabel");
		JSONValue paramAsJSONValue;
		if (type.equals("text") || type.equals("date") || type.equals("time")
				|| type.equals("chiffre"))
			paramAsJSONValue = parametres.get(0);
		else {
			JSONValue parametresValue = parametres.get(0);
			JSONObject parametresObject = parametresValue.isObject();
			if (parametresObject.get("nom").isString().stringValue()
					.equals("titre"))
				paramAsJSONValue = parametres.get(0);
			else
				paramAsJSONValue = parametres.get(1);
		}
		JSONObject paramAsJSONObject = paramAsJSONValue.isObject();
		label.setText(paramAsJSONObject.get("valeur").isString().stringValue());
		TextBox choix = null;
		final DroppableElement cellule = (DroppableElement) cb.getCell(x, y);
		cellule.type = type;
		Element elem = new Element(new Position(y, x), "deplace");
		elem.getElement().setClassName(elem.getType() + "Style");
		Image suppression = new Image();
		suppression.setUrl("images/formulaire/delete.png");
		suppression.addStyleName("deleteForm");

		String id = Utils.generateId();
		;

		// Création de l'element selon le type
		if (type.equals("text")) {
			element = new TextBox();
		}
		if (type.equals("image")) {
			element = new Image("../images/formulaire/Photo.png");
		} else if (type.equals("checkbox")) {
			CheckBox c = new CheckBox();
			// Ajout de l'attribut name pour les champs checkbox et radio
			cellule.name = "" + CHOICE;
			element = c;
			// Ajout du label de choix pour les champs de type checkbox et radio
			choix = new TextBox();
			choix.addStyleName("inputLabel");
			choix.getElement().setAttribute("placeholder", "Choix");

		} else if (type.equals("radio")) {
			RadioButton r = new RadioButton("Choix");
			cellule.name = "" + CHOICE;
			element = r;
			choix = new TextBox();
			choix.addStyleName("inputLabel");
			choix.getElement().setAttribute("placeholder", "Choix");
		} else if (type.equals("combobox")) {
			OPTION++;
			// Ajout de l'option pour les champs de type combobox
			cellule.option = "" + OPTION;
			element = new ListBox();
		} else if (type.equals("chiffre")) {
			element = new HTML(
					"<input type='text' class='gwt-TextBox' onkeypress=\"if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} \" />");
		} else if (type.equals("date")) {
			element = new HTML("<input type='text' id='date" + id
					+ "' class='gwt-TextBox' />");
		} else {
			element = new HTML("<input type='text' id='time" + id
					+ "'  class='gwt-TextBox' />");
		}

		HorizontalPanel hp = cellule.getWidgetElement();

		hp.add(label);
		hp.add(element);
		cellule.choix = false;
		if (type.equals("checkbox") || type.equals("radio")
				|| type.equals("combobox")) {

			JSONValue parametresValue = parametres.get(0);
			JSONObject parametresObject = parametresValue.isObject();
			if (parametresObject.get("nom").isString().stringValue()
					.equals("titre"))
				paramAsJSONValue = parametres.get(1);
			else
				paramAsJSONValue = parametres.get(0);

			paramAsJSONObject = paramAsJSONValue.isObject();

			String ch[] = paramAsJSONObject.get("valeur").isString()
					.stringValue().split("\\*_\\*");
			if (type.equals("checkbox") || type.equals("radio")) {
				choix.setText(ch[0]);
				for (int i = 1; i < ch.length; i++) {
					DroppableElement.ajouterChoix(x + i, y, type, ch[i]);
				}
				CHOICE++;
			} else {
				for (int i = 0; i < ch.length; i++) {
					((ListBox) element).addItem(ch[i], ch[i]);
				}
			}

		}

		if (choix != null)
			hp.add(choix);
		hp.add(elem);
		hp.add(suppression);
		cellule.lock();
		if (type.equals("date") || type.equals("time"))
			FormulaireController.nativeMethod(type + id, type);

		// Action de la suppression
		suppression.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// Vider la cellule
				cellule.clear();
				// Si le champs est de type checkbox ou radio, il faix vider les
				// champs de choix également
				if (type.equals("checkbox") || type.equals("radio")) {
					final FormulaireController gc = FormulaireController
							.getInstance();
					FormulaireBoard c = gc.getCheckerBoard();
					boolean end = false;
					int ligne = x + 1;
					while (!end) {
						DroppableElement cellule_choix = (DroppableElement) c
								.getCell(ligne, y);
						if (cellule_choix.choix) {
							cellule_choix.clear();
							cellule_choix.type = null;
							cellule_choix.choix = false;
							cellule_choix.name = null;
							cellule_choix.enable();
							ligne++;
						} else
							end = true;

					}
				}
				// Rendre le champs capable de recevoir des elements puisqu'on
				// l'a vidé
				cellule.enable();
				cellule.type = null;

			}
		});
	}

	public static native void nativeMethod(String id)
	/*-{
		$wnd.$('.' + id).datepicker({
			dateFormat : 'dd/mm/yy'
		});
	}-*/;

	public void getParamsByElement(long elementId, final int x, final int y,
			final String type) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				PARAM_URL + "listerParametre/element/" + elementId);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						JSONValue paramsAsJSONValue = JSONParser.parse(response
								.getText());
						ajouterElement(paramsAsJSONValue.isArray(), x, y, type);
					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

	}

	// Ajouter les elements du formulaire à a base de données
	@SuppressWarnings("rawtypes")
	private void ajouterFormulaire(final ArrayList elements) {

		// Récuperer les champs nom et commentaire
		nom = form.getForm_nom().getValue();
		final String comment = form.getForm_comment().getValue();

		// Vérifier Si les champs ne sont pa vide
		if (comment.isEmpty()) {
			Window.alert("Le champs commentaire est obligatoire");
			return;
		}

		// **************************************************************************************************************************************ici

		if (ancienNom.equals(nom)) {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
					FORMULAIRE_URL + "derniereVersion/formulaire/" + nom);
			// Recuperer la derniere version du formulaire
			builder.setHeader("Content-Type", "application/json");

			try {
				builder.sendRequest(null, new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						Window.alert("Erreur de récuperation de l'entreprise!");
					}

					public void onResponseReceived(Request request,
							Response response) {
						if (response.getStatusCode() == Response.SC_OK) {
							lastVersion = Integer.parseInt(response.getText());

							formAsJSONObjectBisDupliquer = BuildJsonObject
									.buildJSONObjectFormBis("-1", nom, comment,
											(lastVersion + 1) + "",
											Utils.getToday(), "1",
											Cookies.getCookie("client"))
									.isObject();
							formAsJSONObjectBisEcraser = BuildJsonObject
									.buildJSONObjectFormBis(
											"-1",
											nom,
											comment,
											(formAsJSONObject.get("version")
													.isNumber().toString())
													+ "", Utils.getToday(),
											"2", Cookies.getCookie("client"))
									.isObject();
							controlerCollecte(formAsJSONObjectBisDupliquer,
									formAsJSONObjectBisEcraser);

						}

					}
				});
			} catch (RequestException e) {
				System.out.println("RequestException");
			}
		} else {

			// Vérifier si le chmaps nom n'existe pas déja dans la base de
			// données
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

							JSONObject formAsJSONObjectAjout = BuildJsonObject
									.buildJSONObjectFormBis("-1", nom, comment,
											1 + "", Utils.getToday(), "1",
											Cookies.getCookie("client"));
							formAsJSONObjectBis = formAsJSONObjectAjout;
							ajoutFormulaireDirect();

							// **************************************************************************************************************************************

						}
					}
				});
			} catch (RequestException e) {
				Window.alert("RequestException");
			}
		}

	}

	@SuppressWarnings("rawtypes")
	public void ajoutFormulaireDirect() {
		for (int i = 0; i < elements.size(); i++) {
			JSONObject elementAsJSONObject = BuildJsonObject
					.buildJSONObjectElementBis("" + i,
							((ArrayList) elements.get(i)).get(0).toString(),
							((ArrayList) elements.get(i)).get(1).toString(),
							((ArrayList) elements.get(i)).get(2).toString());
			formAsJSONObjectBis.put("element" + i, elementAsJSONObject);

			ajouterParametre(i,
					((ArrayList) elements.get(i)).get(3).toString(),
					((ArrayList) elements.get(i)).get(4).toString());
		}

		FormulaireController.allouerFormulaireBis(dualListBox,
				formAsJSONObjectBis);
		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
					FORMULAIRE_URL + "updateForm");
			builder.setHeader("Content-Type", "application/json");
			builder.sendRequest(formAsJSONObjectBis.toString(),
					new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							Window.alert("Erreur d'ajout");
						}

						public void onResponseReceived(Request request,
								Response response) {
							RootPanel.get("button_Enregistrer").remove(
									enregistrer);
							Window.alert("Formulaire: Ajout avec succès");
							Window.Location.reload();
						}

					});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}

	/***
	 * Enregistrer les paramètres liés à chaque Element (titre) Params: Les
	 * positions de l'elements (X,Y) et son emplacement dans la liste des
	 * elements
	 * 
	 * @param last
	 * ***/
	@SuppressWarnings("rawtypes")
	private void ajouterParametre(int emplacement, String name, String option) {

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

	public void controlerCollecte(
			final JSONObject formAsJSONObjectAjoutDupliquer,
			final JSONObject formAsJSONObjectAjoutEcraser) {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				VALUE_URL + "recupererValues/elements/" + elementsAsString);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération du formulaire");
				}

				public void onResponseReceived(Request request,
						Response response) {
					String comptes[] = response.getText().split("-");

					if (comptes.length == 0
							|| (comptes.length == 1 && comptes[0].isEmpty())) {
						createDialogBoxConfirmationFormulaire(
								"Voulez-vous ecraser cette version ?",
								formAsJSONObjectAjoutDupliquer,
								formAsJSONObjectAjoutEcraser).show();
					} else {
						formAsJSONObjectBis = formAsJSONObjectAjoutDupliquer;
						createDialogBoxConfirmationAllocationFormulaire(
								"Impossible d'écraser cette version (Des données sont collectées via ce formulaire)!",
								formAsJSONObjectAjoutDupliquer,
								formAsJSONObjectAjoutEcraser).show();
					}

				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}
	}

	// Popup pour les messages
	public DialogBox createDialogBoxConfirmationFormulaire(String msg,
			final JSONObject formAsJSONObjectAjoutDupliquer,
			final JSONObject formAsJSONObjectAjoutEcraser) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Confirmation");
		dialogBox.setAnimationEnabled(true);

		Button ouiButton = new Button("Oui");
		ouiButton.getElement().setId("ouiButton");

		Button nonButton = new Button("Non");
		nonButton.getElement().setId("nonButton");

		VerticalPanel dialogVPanel = new VerticalPanel();
		HorizontalPanel dialogHPanel = new HorizontalPanel();
		dialogHPanel.setWidth("80px");
		dialogVPanel.setWidth("300px");
		dialogVPanel.add(new HTML("<b>" + msg + "</b>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogHPanel.add(ouiButton);
		dialogHPanel.add(nonButton);
		dialogVPanel.add(dialogHPanel);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setPopupPosition(450, 450);

		/*** Action de clique sur le button de valisation ***/
		ouiButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				formAsJSONObjectBis = formAsJSONObjectAjoutEcraser;
				ajoutFormulaireDirect();

				// Supprimer le formulaire
				RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
						FORMULAIRE_URL + "supprimer/" + formulaire_id);
				builder.setHeader("Content-Type", "application/json");
				try {
					builder.sendRequest(null, new RequestCallback() {
						public void onError(Request request, Throwable exception) {

						}

						public void onResponseReceived(Request request,
								Response response) {

						}
					});
				} catch (RequestException e) {
					System.out.println("RequestException");
				}

				dialogBox.hide();
			}
		});

		/*** Action de clique sur le button de valisation ***/
		nonButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				formAsJSONObjectBis = formAsJSONObjectAjoutDupliquer;
				ajoutFormulaireDirect();

				dialogBox.hide();
			}
		});

		return dialogBox;
	}

	public DialogBox createDialogBoxConfirmationAllocationFormulaire(
			String msg, final JSONObject formAsJSONObjectAjoutDupliquer,
			final JSONObject formAsJSONObjectAjoutEcraser) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Confirmation");
		dialogBox.setAnimationEnabled(true);

		Button ouiButton = new Button("Enregister sous une nouvelle version");
		ouiButton.getElement().setId("ouiButton");

		Button nonButton = new Button("Changer l'allocation de cette version");
		nonButton.getElement().setId("nonButton");

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setWidth("350px");
		HTML h = new HTML("<b>" + msg + "</b>");
		dialogVPanel.add(h);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dialogVPanel.setCellHeight(h, "44px");
		dialogVPanel.add(ouiButton);
		dialogVPanel.setCellHeight(ouiButton, "35px");
		nonButton.setWidth("225px");
		dialogVPanel.add(nonButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setPopupPosition(450, 550);

		nonButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MouseListBox mlb = dualListBox.getRight();
				ArrayList<Widget> widgets = mlb.widgetList();
				String comptes = "";
				for (int i = 0; i < widgets.size(); i++) {
					if (i == 0)
						comptes = widgets.get(i).getTitle();
					else
						comptes = comptes + "-" + widgets.get(i).getTitle();
				}
				RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
						FORMULAIRE_URL + "changeAllocation/formulaire/"
								+ formulaire_id);
				builder.setHeader("Content-Type", "application/json");
				try {
					builder.sendRequest(comptes, new RequestCallback() {
						public void onError(Request request, Throwable exception) {

						}

						public void onResponseReceived(Request request,
								Response response) {
							Window.alert("Formulaire: Enregistrement avec succès");
							Window.Location.reload();
						}
					});
				} catch (RequestException e) {
					System.out.println("RequestException");
				}

				dialogBox.hide();
			}
		});

		ouiButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				formAsJSONObjectBis = formAsJSONObjectAjoutDupliquer;
				ajoutFormulaireDirect();

				dialogBox.hide();
			}
		});

		return dialogBox;
	}

}
