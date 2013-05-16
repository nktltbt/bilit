package com.applifit.bilifit.client.formulaires;

import java.util.ArrayList;

import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.formulaires.BodyConsultation;
import com.applifit.bilifit.client.templatesUibinder.formulaires.FormRecherche;
import com.applifit.bilifit.client.templatesUibinder.formulaires.MenuForm;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Forumlaire_consulter_applifit implements EntryPoint {

	// Les liens pour les services web
	private static final String FORMULAIRE_URL = GWT.getHostPageBaseURL()
			+ "formulaire/";
	private static final String ELEMENT_URL = GWT.getHostPageBaseURL()
			+ "element/";
	private static final String PARAM_URL = GWT.getHostPageBaseURL()
			+ "parametre/";
	private static final String VALUE_URL = GWT.getHostPageBaseURL()
			+ "element_valeur/";

	// Les parties recherche, le body du formulaire et le formulaire contenant
	// le nom et le commentaire
	FormRecherche recherche = new FormRecherche();
	BodyConsultation body = new BodyConsultation();
	long formulaire_id;
	static boolean bodyExist = false;
	FlexTable flexApercu;
	String elementsAsString = "";
	JSONArray elementsAsJSONArray;

	final ArrayList<FlexTable> tables = new ArrayList<FlexTable>();
	int indice = 0;

	@Override
	public void onModuleLoad() {

		/*** Seul le profil Super Adminisatreur peut se connecter à cette page ***/
		if (Cookies.getCookie("profil") == null)
			Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html"
					+ Constantes.SUFFIXE_URL);
		else if (Cookies.getCookie("client") == null)
			Window.Location.replace(GWT.getHostPageBaseURL()
					+ "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
		else {
			// Ajouter le menu à la page
			final MenuForm menu = new MenuForm(Constantes.USER);
			Document.get().getBody().appendChild(menu.getElement());

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

			/*** action de deconnexion ***/
			Anchor deconnexionWrapper = Anchor.wrap(menu.getDeconnexion());
			deconnexionWrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Utils.deconnexion();
				}

			});

			// Ajouter la zone de recherche à la page
			Document.get().getBody()
					.insertAfter(recherche.getElement(), menu.getElement());
			nativeMethod("datepicker");
			// Remplire la liste des formulaires par les formulaires actifs
			// liés à la société courante
			if (Cookies.getCookie("profil").equals("SA")
					|| Cookies.getCookie("profil").equals("CA"))
				FormulaireController.alimenterListeForm(
						FORMULAIRE_URL + "listerFormulaire/entreprise/"
								+ Cookies.getCookie("client"), recherche);
			else
				FormulaireController.alimenterListeForm(FORMULAIRE_URL
						+ "listerFormulaire/compte/" + Cookies.getCookie("id"),
						recherche);

			// Ajouter action pour le button de recherche
			Anchor search = Anchor.wrap(recherche.getSearch_link());
			search.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {

					final ListBox lb = ListBox.wrap(recherche.getFormulaire());
					// Vider la liste
					lb.clear();
					lb.addItem("Choisir un formulaire...", "");
					lb.addItem("Choisir un formulaire...", "");

					// Lancer la recherche du formulaire selon le nom et la date
					// de création
					FormulaireController.rechercheForm(recherche);
					// Ajouter action on change de la liste des formulaires ,
					// affichant le formulaire selectionné
					final ListBox lb2 = ListBox.wrap(recherche.getFormulaire());
					lb2.addChangeHandler(new ChangeHandler() {
						@Override
						public void onChange(ChangeEvent event) {
							// Si la ligne selectionnée n'est pas le prompt de
							// la liste
							if (lb.getSelectedIndex() != 0) {
								// Récuperer la valeur selectionnée
								formulaire_id = Long.parseLong(lb.getValue(lb
										.getSelectedIndex()));
								elementsAsString = "";
								// Récuperer les elements et parametres du
								// formulaire et les afficher dans la zone de
								// modification
								chargerBody();
							}
						}
					});
				}

			});

			// Ajouter action on change de la liste des formulaires , affichant
			// le formulaire selectionné
			final ListBox lb = ListBox.wrap(recherche.getFormulaire());

			lb.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					// Si la ligne selectionnée n'est pas le prompt de la liste
					if (lb.getSelectedIndex() != 0) {
						// Récuperer la valeur selectionnée
						formulaire_id = Long.parseLong(lb.getValue(lb
								.getSelectedIndex()));
						elementsAsString = "";
						tables.clear();
						// Récuperer les elements et parametres du formulaire
						// et les afficher dans la zone de modification
						chargerBody();
					}
				}
			});
		}
	}

	public void chargerBody() {
		// Vider la zone de modification dans le cas où elle est occupée par
		// un autre formulaire
		if (bodyExist) {
			Document.get().getBody().removeChild(body.getElement());
		}

		// Construire la zone de modification
		body = new BodyConsultation();
		chargerFormulaire();
		bodyExist = true;

	}

	public void remplirTableau() {

		String url = "";
		if (Cookies.getCookie("profil").equals("SA")
				|| Cookies.getCookie("profil").equals("CA"))
			url = VALUE_URL + "recupererValues";
		else
			url = VALUE_URL + "recupererValues/compte/"
					+ Cookies.getCookie("id");

		// Récuperer la liste des comptes pour lesquels, nous avons allouer
		// cette version du formulaire
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(elementsAsString, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération");
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() != 500) {
						String comptes[] = response.getText().split(",");
						String class_cell = "";
						int j = 0;
						if (comptes.length > 1)
							for (int i = 0; i < comptes.length; i++) {
								if (comptes[i] != null) {
									Image edit = new Image();
									edit.setTitle(comptes[i] + "-"
											+ comptes[i + 1]);
									i = i + 2;
									if (j % 2 != 1)
										class_cell = "table_ligne_clair couleur_tableau_clair";
									else
										class_cell = "table_ligne_fonce couleur_tableau_fonce";
									TableRowElement row = body.getTbody()
											.insertRow(-1);
									TableCellElement cell = row.insertCell(-1);
									cell.addClassName(class_cell);
									cell.setInnerHTML(comptes[i] + " "
											+ comptes[i + 1]);
									i = i + 2;
									cell = row.insertCell(-1);
									cell.addClassName(class_cell);
									cell.setInnerHTML(comptes[i]);
									cell = row.insertCell(-1);
									cell.addClassName(class_cell);
									cell.addClassName("img_edit");
									edit.setUrl("images/Edit.png");
									cell.appendChild(edit.getElement());
									j++;
								}
							}
						NodeList<TableRowElement> rows = body.getTbody()
								.getRows();
						for (int i = 1; i < rows.getLength(); i++) {
							final NodeList<TableCellElement> cells = rows
									.getItem(i).getCells();
							final TableCellElement c = cells.getItem(2);

							/******************************************************************/
							final ImageElement editI = (ImageElement) c
									.getChild(0);
							Image imageWrapper = Image.wrap(editI);

							imageWrapper.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									donneesCollectees(editI.getTitle());
								}
							});
						}
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}

	}

	// Remplir le formulaire par les données collectées par l'utilisateur
	// choisir pour une date (jour , heur , minute) précise
	private void donneesCollectees(String title) {

		String variables[] = title.split("-");
		indice = Integer.parseInt(variables[1]);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				VALUE_URL + "getValuesParIndice/indice/" + variables[1]);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération du formulaire");
				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					JSONValue valuesAsJSONValue = JSONParser.parse(response
							.getText());
					JSONArray valuesAsJSONArray = valuesAsJSONValue.isArray();
					for (int i = 0; i < elementsAsJSONArray.size(); i++) {
						JSONObject elementAsJSONObject = elementsAsJSONArray
								.get(i).isObject();
						JSONObject valueAsJSONObject = rendreValue(
								elementAsJSONObject.get("id").isNumber()
										.toString(), valuesAsJSONArray);
						String type = elementAsJSONObject.get("type")
								.isString().stringValue();
						Element ie = tables
								.get(Integer.parseInt(elementAsJSONObject
										.get("position_x").isNumber()
										.toString()))
								.getWidget(
										2 * Integer
												.parseInt(elementAsJSONObject
														.get("position_y")
														.isNumber().toString()) + 1,
										0).getElement();
						if (type.equals("text") || type.equals("chiffre")
								|| type.equals("time")) {
							ie.setAttribute("value",
									valueAsJSONObject.get("valeur").isString()
											.stringValue());
						} else if (type.equals("date")) {
							ie.setAttribute(
									"value",
									Utils.dateFormat(valueAsJSONObject
											.get("valeur").isString()
											.stringValue()));

						} else if (type.equals("checkbox")
								|| type.equals("radio")) {
							String choix[] = valueAsJSONObject.get("valeur")
									.isString().stringValue().split("\\*_\\*");
							Widget widget = tables.get(
									Integer.parseInt(elementAsJSONObject
											.get("position_x").isNumber()
											.toString())).getWidget(
									2 * Integer.parseInt(elementAsJSONObject
											.get("position_y").isNumber()
											.toString()) + 1, 0);

							for (int o = 0; o < widget.getElement()
									.getChildCount(); o++) {
								if (o == 0) {
									InputElement element = widget.getElement()
											.getChild(o).cast();
									element.setChecked(false);
									o++;
								} else {
									Element element = widget.getElement()
											.getChild(o).cast();
									InputElement checkbox = element.getChild(0)
											.cast();
									checkbox.setChecked(false);
								}
							}

							for (int j = 0; j < choix.length; j++) {
								boolean found = false;

								int k = 0;
								while (k < widget.getElement().getChildCount()
										&& !found) {
									if (k == 0) {
										InputElement element = widget
												.getElement().getChild(k)
												.cast();
										LabelElement lb = widget.getElement()
												.getChild(k + 1).cast();

										if (element.getName().equals(
												elementAsJSONObject.get("id")
														.isNumber().toString())) {
											if (choix.length == 1
													&& lb.getInnerText()
															.equals("")) {
												if (choix[0].equals("X")) {
													element.setChecked(true);
												}
											} else if (lb.getInnerText()
													.equals(choix[j])) {
												element.setChecked(true);
												found = true;
											}
										}
										k++;
									} else {
										Element element = widget.getElement()
												.getChild(k).cast();
										InputElement checkbox = element
												.getChild(0).cast();
										LabelElement lb = element.getChild(1)
												.cast();

										if (checkbox.getName().equals(
												elementAsJSONObject.get("id")
														.isNumber().toString())) {
											if (lb.getInnerText().equals(
													choix[j])) {
												checkbox.setChecked(true);
												found = true;
											}
										}

									}
									k++;

								}

							}
						} else if (type.equals("combobox")) {
							SelectElement se = ie.cast();
							boolean found = false;
							int d = 0;
							OptionElement oe;
							while (d < se.getLength() && !found) {
								oe = se.getChild(d).cast();
								if (oe.getValue().equals(
										valueAsJSONObject.get("valeur")
												.isString().stringValue())) {
									found = true;
									se.setSelectedIndex(d);
								}
								d++;
							}
						}

					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}
	}

	/*** récuperer la valeur correspondante à l'element ***/
	public JSONObject rendreValue(String element, JSONArray valuesAsJSONArray) {

		int i = 0;
		boolean found = false;
		JSONObject valuesAsJSONObject = null;
		while (i < valuesAsJSONArray.size() && !found) {
			valuesAsJSONObject = valuesAsJSONArray.get(i).isObject();
			if (valuesAsJSONObject.get("element").isString().stringValue()
					.equals(element)) {
				found = true;
			} else
				i++;
		}
		return valuesAsJSONObject;
	}

	/*** Afficher l'aperçs du formulaire ***/
	/*
	 * public void chargerFormulaire(){
	 * 
	 * Document.get().getBody().appendChild(body.getElement());
	 * 
	 * //Table qui va héberger les elements du formulaire flexApercu = new
	 * FlexTable(); flexApercu.setCellPadding(1); flexApercu.setCellSpacing(0);
	 * flexApercu.setStyleName("style_grid_apercu");
	 * RootPanel.get("formConsultation").add(flexApercu);
	 * 
	 * 
	 * Anchor lien_enregistrer = Anchor.wrap(body.getLien_enregistrer());
	 * lien_enregistrer.addClickHandler(new ClickHandler() {
	 * 
	 * @Override public void onClick(ClickEvent event) { if(indice != 0)
	 * enregistrerForm(); }
	 * 
	 * });
	 * 
	 * //Remplir la zone de création par les elements et les parametres
	 * RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
	 * ELEMENT_URL+"listerElement/formulaire/"+formulaire_id);
	 * builder.setHeader("Content-Type", "application/json");
	 * 
	 * try { builder.sendRequest(null, new RequestCallback() { public void
	 * onError(Request request, Throwable exception) {
	 * 
	 * } public void onResponseReceived(Request request, Response response) { if
	 * (response.getStatusCode() == Response.SC_OK) { String
	 * elementsAsJSONString = response.getText(); JSONValue elementsAsJSONValue
	 * = JSONParser.parse(elementsAsJSONString); elementsAsJSONArray =
	 * elementsAsJSONValue.isArray(); for (int i = 0; i <
	 * elementsAsJSONArray.size(); i++) { JSONValue elementAsJSONValue =
	 * elementsAsJSONArray.get(i); final JSONObject elementAsJSONObject =
	 * elementAsJSONValue.isObject();
	 * 
	 * elementsAsString
	 * +=elementAsJSONObject.get("id").isNumber().toString()+"-"; RequestBuilder
	 * builder = new RequestBuilder(RequestBuilder.GET,
	 * PARAM_URL+"listerParametre/element/"
	 * +elementAsJSONObject.get("id").isNumber().toString());
	 * builder.setHeader("Content-Type", "application/json");
	 * 
	 * try { builder.sendRequest(null, new RequestCallback() { public void
	 * onError(Request request, Throwable exception) {
	 * 
	 * } public void onResponseReceived(Request request, Response response) {
	 * 
	 * if (response.getStatusCode() == Response.SC_OK) { JSONValue
	 * paramsAsJSONValue = JSONParser.parse(response.getText()); JSONArray
	 * paramsAsJSONArray = paramsAsJSONValue.isArray();
	 * 
	 * Label l = new Label(); JSONValue paramAsJSONValue;
	 * if(elementAsJSONObject.
	 * get("type").isString().stringValue().equals("text") ||
	 * elementAsJSONObject.get("type").isString().stringValue().equals("date")
	 * ||
	 * elementAsJSONObject.get("type").isString().stringValue().equals("time")
	 * ||
	 * elementAsJSONObject.get("type").isString().stringValue().equals("chiffre"
	 * )) paramAsJSONValue = paramsAsJSONArray.get(0); else { JSONValue
	 * parametresValue = paramsAsJSONArray.get(0); JSONObject parametresObject =
	 * parametresValue.isObject();
	 * if(parametresObject.get("nom").isString().stringValue().equals("titre"))
	 * paramAsJSONValue = paramsAsJSONArray.get(0); else paramAsJSONValue =
	 * paramsAsJSONArray.get(1); }
	 * 
	 * JSONObject paramAsJSONObject = paramAsJSONValue.isObject();
	 * l.setText(paramAsJSONObject.get("valeur").isString().stringValue());
	 * flexApercu
	 * .setWidget(2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString()),
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), l);
	 * 
	 * Widget widget = null;
	 * if(elementAsJSONObject.get("type").isString().stringValue
	 * ().equals("text")){ widget = new TextBox();
	 * widget.addStyleName("style_text_apercu");
	 * widget.getElement().setAttribute("name",
	 * elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu.setWidget
	 * (2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), widget); }else
	 * if(elementAsJSONObject.get("type"
	 * ).isString().stringValue().equals("chiffre")){ widget = new TextBox();
	 * widget.getElement().setAttribute("onkeypress",
	 * "if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} "
	 * ); widget.addStyleName("style_text_apercu");
	 * widget.getElement().setAttribute("name",
	 * elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu.setWidget
	 * (2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), widget); }else if
	 * (elementAsJSONObject.get("type").isString().stringValue().equals("date"))
	 * { widget = new TextBox(); widget.getElement().setAttribute("id",
	 * elementAsJSONObject.get("id").isNumber().toString());
	 * widget.getElement().setAttribute("name",
	 * elementAsJSONObject.get("id").isNumber().toString());
	 * widget.addStyleName("style_text_apercu");
	 * flexApercu.setWidget(2*Integer.parseInt
	 * (elementAsJSONObject.get("position_y").isNumber().toString())+1,
	 * Integer.parseInt
	 * (elementAsJSONObject.get("position_x").isNumber().toString()), widget); }
	 * else if
	 * (elementAsJSONObject.get("type").isString().stringValue().equals("time"))
	 * { widget = new TextBox(); widget.getElement().setAttribute("id",
	 * elementAsJSONObject.get("id").isNumber().toString());
	 * widget.getElement().setAttribute("name",
	 * elementAsJSONObject.get("id").isNumber().toString());
	 * widget.addStyleName("style_text_apercu");
	 * flexApercu.setWidget(2*Integer.parseInt
	 * (elementAsJSONObject.get("position_y").isNumber().toString())+1,
	 * Integer.parseInt
	 * (elementAsJSONObject.get("position_x").isNumber().toString()), widget);
	 * }else if
	 * (elementAsJSONObject.get("type").isString().stringValue().equals(
	 * "checkbox") ||
	 * elementAsJSONObject.get("type").isString().stringValue().equals("radio"))
	 * {
	 * 
	 * JSONValue parametresValue = paramsAsJSONArray.get(0); JSONObject
	 * parametresObject = parametresValue.isObject();
	 * if(parametresObject.get("nom").isString().stringValue().equals("titre"))
	 * paramAsJSONValue = paramsAsJSONArray.get(1); else paramAsJSONValue =
	 * paramsAsJSONArray.get(0);
	 * 
	 * 
	 * paramAsJSONObject = paramAsJSONValue.isObject();
	 * 
	 * String choix = paramAsJSONObject.get("valeur").isString().stringValue();
	 * String ch[] = choix.split("\\*_\\*");
	 * 
	 * if(elementAsJSONObject.get("type").isString().stringValue().equals("checkbox"
	 * )){ CheckBox c = new CheckBox(); c.setText(ch[0]); widget = c;
	 * c.setName(elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu
	 * .setWidget(2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), widget); c.addStyleName("style_choix_apercu");
	 * for(int j = 1 ; j <ch.length ; j++){ CheckBox check = new CheckBox();
	 * check.setText(ch[j]); check.addStyleName("style_choix_apercu");
	 * check.setName(elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu
	 * .getWidget(2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString())).getElement().appendChild(check.getElement()); }
	 * }else{
	 * 
	 * RadioButton r = new
	 * RadioButton(((JSONObject)paramsAsJSONArray.get(0)).get
	 * ("valeur").isString().stringValue()); r.setText(ch[0]); widget = r;
	 * r.setName(elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu
	 * .setWidget(2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), widget); r.addStyleName("style_choix_apercu");
	 * for(int j = 1 ; j <ch.length ; j++){ RadioButton radio = new
	 * RadioButton((
	 * (JSONObject)paramsAsJSONArray.get(0)).get("valeur").isString(
	 * ).stringValue()); radio.setText(ch[j]);
	 * radio.addStyleName("style_choix_apercu");
	 * radio.setName(elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu
	 * .setWidget(2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), widget); } } }else
	 * if(elementAsJSONObject.get("type"
	 * ).isString().stringValue().equals("combobox")){ JSONValue parametresValue
	 * = paramsAsJSONArray.get(0); JSONObject parametresObject =
	 * parametresValue.isObject();
	 * if(parametresObject.get("nom").isString().stringValue().equals("titre"))
	 * paramAsJSONValue = paramsAsJSONArray.get(1); else paramAsJSONValue =
	 * paramsAsJSONArray.get(0);
	 * 
	 * 
	 * parametresObject = paramAsJSONValue.isObject();
	 * 
	 * ListBox lb = new ListBox();
	 * lb.addItem("Veuillez selectionnez un choix...", ""); String opt =
	 * parametresObject.get("valeur").isString().stringValue(); String o[] =
	 * opt.split("\\*_\\*"); for(int j=0; j<o.length; j++ ){ lb.addItem(o[j],
	 * o[j]); } lb.addStyleName("style_select_apercu"); widget = lb;
	 * lb.setName(elementAsJSONObject.get("id").isNumber().toString());
	 * flexApercu
	 * .setWidget(2*Integer.parseInt(elementAsJSONObject.get("position_y"
	 * ).isNumber().toString())+1,
	 * Integer.parseInt(elementAsJSONObject.get("position_x"
	 * ).isNumber().toString()), widget); }
	 * 
	 * if(elementAsJSONObject.get("type").isString().stringValue().equals("date")
	 * ||
	 * elementAsJSONObject.get("type").isString().stringValue().equals("time")){
	 * 
	 * FormulaireController.nativeMethod(elementAsJSONObject.get("id").isNumber()
	 * .toString(), elementAsJSONObject.get("type").isString().stringValue()); }
	 * 
	 * } } }); } catch (RequestException e) {
	 * System.out.println("RequestException"); }
	 * 
	 * }
	 * 
	 * remplirTableau();
	 * 
	 * 
	 * } } }); } catch (RequestException e) {
	 * System.out.println("RequestException"); } }
	 */

	public void chargerFormulaire() {

		Document.get().getBody().appendChild(body.getElement());

		// Table qui va héberger les elements du formulaire
		FlexTable flexApercu1 = new FlexTable();
		flexApercu1.setCellPadding(1);
		flexApercu1.setCellSpacing(0);
		flexApercu1.setStyleName("style_grid_apercu");

		FlexTable flexApercu2 = new FlexTable();
		flexApercu1.setCellPadding(1);
		flexApercu1.setCellSpacing(0);
		flexApercu1.setStyleName("style_grid_apercu");

		FlexTable flexApercu3 = new FlexTable();
		flexApercu1.setCellPadding(1);
		flexApercu1.setCellSpacing(0);
		flexApercu1.setStyleName("style_grid_apercu");

		tables.add(flexApercu1);
		tables.add(flexApercu2);
		tables.add(flexApercu3);

		Anchor lien_enregistrer = Anchor.wrap(body.getLien_enregistrer());
		lien_enregistrer.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (indice != 0)
					enregistrerForm();
			}

		});

		// Remplir la zone de création par les elements et les parametres
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				ELEMENT_URL + "listerElement/formulaire/" + formulaire_id);
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
						elementsAsJSONArray = elementsAsJSONValue.isArray();
						for (int i = 0; i < elementsAsJSONArray.size(); i++) {
							JSONValue elementAsJSONValue = elementsAsJSONArray
									.get(i);
							final JSONObject elementAsJSONObject = elementAsJSONValue
									.isObject();

							elementsAsString += elementAsJSONObject.get("id")
									.isNumber().toString()
									+ "-";
							RequestBuilder builder = new RequestBuilder(
									RequestBuilder.GET, PARAM_URL
											+ "listerParametre/element/"
											+ elementAsJSONObject.get("id")
													.isNumber().toString());
							builder.setHeader("Content-Type",
									"application/json");
							try {
								builder.sendRequest(null,
										new RequestCallback() {
											public void onError(
													Request request,
													Throwable exception) {

											}

											public void onResponseReceived(
													Request request,
													Response response) {

												if (response.getStatusCode() == Response.SC_OK) {
													JSONValue paramsAsJSONValue = JSONParser
															.parse(response
																	.getText());
													JSONArray paramsAsJSONArray = paramsAsJSONValue
															.isArray();

													Label l = new Label();
													JSONValue paramAsJSONValue;
													if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("image")
															|| elementAsJSONObject
																	.get("type")
																	.isString()
																	.stringValue()
																	.equals("text")
															|| elementAsJSONObject
																	.get("type")
																	.isString()
																	.stringValue()
																	.equals("date")
															|| elementAsJSONObject
																	.get("type")
																	.isString()
																	.stringValue()
																	.equals("time")
															|| elementAsJSONObject
																	.get("type")
																	.isString()
																	.stringValue()
																	.equals("chiffre"))
														paramAsJSONValue = paramsAsJSONArray
																.get(0);
													else {
														JSONValue parametresValue = paramsAsJSONArray
																.get(0);
														JSONObject parametresObject = parametresValue
																.isObject();
														if (parametresObject
																.get("nom")
																.isString()
																.stringValue()
																.equals("titre"))
															paramAsJSONValue = paramsAsJSONArray
																	.get(0);
														else
															paramAsJSONValue = paramsAsJSONArray
																	.get(1);
													}

													JSONObject paramAsJSONObject = paramAsJSONValue
															.isObject();
													l.setText(paramAsJSONObject
															.get("valeur")
															.isString()
															.stringValue());
													tables.get(
															Integer.parseInt(elementAsJSONObject
																	.get("position_x")
																	.isNumber()
																	.toString()))
															.setWidget(
																	2 * Integer
																			.parseInt(elementAsJSONObject
																					.get("position_y")
																					.isNumber()
																					.toString()),
																	0, l);

													Widget widget = null;
													if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("image")) {
														widget = new Image(
																"../images/formulaire/Photo.png");
														widget.getElement()
																.setAttribute(
																		"name",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														tables.get(
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()))
																.setWidget(
																		2 * Integer
																				.parseInt(elementAsJSONObject
																						.get("position_y")
																						.isNumber()
																						.toString()) + 1,
																		0,
																		widget);
													} else if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("text")) {
														widget = new TextBox();
														widget.addStyleName("style_text_apercu");
														widget.getElement()
																.setAttribute(
																		"name",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														tables.get(
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()))
																.setWidget(
																		2 * Integer
																				.parseInt(elementAsJSONObject
																						.get("position_y")
																						.isNumber()
																						.toString()) + 1,
																		0,
																		widget);
													} else if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("chiffre")) {
														widget = new TextBox();
														widget.getElement()
																.setAttribute(
																		"onkeypress",
																		"if((event.keyCode < 48 || event.keyCode > 57) && event.keyCode != 46){ event.returnValue=false;}else if(event.keyCode == 46 && (this.value == '' || this.value.indexOf('.') != -1)){ event.returnValue =false;} ");
														widget.addStyleName("style_text_apercu");
														widget.getElement()
																.setAttribute(
																		"name",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														tables.get(
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()))
																.setWidget(
																		2 * Integer
																				.parseInt(elementAsJSONObject
																						.get("position_y")
																						.isNumber()
																						.toString()) + 1,
																		0,
																		widget);
													} else if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("date")) {
														widget = new TextBox();
														widget.getElement()
																.setAttribute(
																		"id",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														widget.getElement()
																.setAttribute(
																		"name",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														widget.addStyleName("style_text_apercu");
														tables.get(
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()))
																.setWidget(
																		2 * Integer
																				.parseInt(elementAsJSONObject
																						.get("position_y")
																						.isNumber()
																						.toString()) + 1,
																		0,
																		widget);
													} else if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("time")) {
														widget = new TextBox();
														widget.getElement()
																.setAttribute(
																		"id",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														widget.getElement()
																.setAttribute(
																		"name",
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString());
														widget.addStyleName("style_text_apercu");
														tables.get(
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()))
																.setWidget(
																		2 * Integer
																				.parseInt(elementAsJSONObject
																						.get("position_y")
																						.isNumber()
																						.toString()) + 1,
																		0,
																		widget);
													} else if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("checkbox")
															|| elementAsJSONObject
																	.get("type")
																	.isString()
																	.stringValue()
																	.equals("radio")) {

														JSONValue parametresValue = paramsAsJSONArray
																.get(0);
														JSONObject parametresObject = parametresValue
																.isObject();
														if (parametresObject
																.get("nom")
																.isString()
																.stringValue()
																.equals("titre"))
															paramAsJSONValue = paramsAsJSONArray
																	.get(1);
														else
															paramAsJSONValue = paramsAsJSONArray
																	.get(0);

														paramAsJSONObject = paramAsJSONValue
																.isObject();

														String choix = paramAsJSONObject
																.get("valeur")
																.isString()
																.stringValue();
														String ch[] = choix
																.split("\\*_\\*");

														if (elementAsJSONObject
																.get("type")
																.isString()
																.stringValue()
																.equals("checkbox")) {
															CheckBox c = new CheckBox();
															c.setText(ch[0]);
															widget = c;
															c.setName(elementAsJSONObject
																	.get("id")
																	.isNumber()
																	.toString());
															tables.get(
																	Integer.parseInt(elementAsJSONObject
																			.get("position_x")
																			.isNumber()
																			.toString()))
																	.setWidget(
																			2 * Integer
																					.parseInt(elementAsJSONObject
																							.get("position_y")
																							.isNumber()
																							.toString()) + 1,
																			0,
																			widget);
															c.addStyleName("style_choix_apercu");
															for (int j = 1; j < ch.length; j++) {
																CheckBox check = new CheckBox();
																check.setText(ch[j]);
																check.addStyleName("style_choix_apercu");
																check.setName(elementAsJSONObject
																		.get("id")
																		.isNumber()
																		.toString());
																if (j == ch.length - 1)
																	check.addStyleName("lastChoix");
																tables.get(
																		Integer.parseInt(elementAsJSONObject
																				.get("position_x")
																				.isNumber()
																				.toString()))
																		.getWidget(
																				2 * Integer
																						.parseInt(elementAsJSONObject
																								.get("position_y")
																								.isNumber()
																								.toString()) + 1,
																				0)
																		.getElement()
																		.appendChild(
																				check.getElement());
															}
															for (int j = 0; j < 2 * (ch.length - 1); j++) {
																tables.get(
																		Integer.parseInt(elementAsJSONObject
																				.get("position_x")
																				.isNumber()
																				.toString()))
																		.setWidget(
																				2
																						* Integer
																								.parseInt(elementAsJSONObject
																										.get("position_y")
																										.isNumber()
																										.toString())
																						+ 2
																						+ j,
																				0,
																				new HTML(
																						""));
																tables.get(
																		Integer.parseInt(elementAsJSONObject
																				.get("position_x")
																				.isNumber()
																				.toString()))
																		.getElement()
																		.getElementsByTagName(
																				"tr")
																		.getItem(
																				2
																						* Integer
																								.parseInt(elementAsJSONObject
																										.get("position_y")
																										.isNumber()
																										.toString())
																						+ 2
																						+ j)
																		.setClassName(
																				"trHeightNull");
															}
														} else {

															RadioButton r = new RadioButton(
																	((JSONObject) paramsAsJSONArray
																			.get(0))
																			.get("valeur")
																			.isString()
																			.stringValue());
															r.setText(ch[0]);
															widget = r;
															r.setName(elementAsJSONObject
																	.get("id")
																	.isNumber()
																	.toString());
															tables.get(
																	Integer.parseInt(elementAsJSONObject
																			.get("position_x")
																			.isNumber()
																			.toString()))
																	.setWidget(
																			2 * Integer
																					.parseInt(elementAsJSONObject
																							.get("position_y")
																							.isNumber()
																							.toString()) + 1,
																			0,
																			widget);
															r.addStyleName("style_choix_apercu");
															for (int j = 1; j < ch.length; j++) {
																RadioButton radio = new RadioButton(
																		((JSONObject) paramsAsJSONArray
																				.get(0))
																				.get("valeur")
																				.isString()
																				.stringValue());
																radio.setText(ch[j]);
																radio.addStyleName("style_choix_apercu");
																radio.setName(elementAsJSONObject
																		.get("id")
																		.isNumber()
																		.toString());
																if (j == ch.length - 1)
																	radio.addStyleName("lastChoix");
																tables.get(
																		Integer.parseInt(elementAsJSONObject
																				.get("position_x")
																				.isNumber()
																				.toString()))
																		.getWidget(
																				2 * Integer
																						.parseInt(elementAsJSONObject
																								.get("position_y")
																								.isNumber()
																								.toString()) + 1,
																				0)
																		.getElement()
																		.appendChild(
																				radio.getElement());
															}
															for (int j = 0; j < 2 * (ch.length - 1); j++) {
																tables.get(
																		Integer.parseInt(elementAsJSONObject
																				.get("position_x")
																				.isNumber()
																				.toString()))
																		.setWidget(
																				2
																						* Integer
																								.parseInt(elementAsJSONObject
																										.get("position_y")
																										.isNumber()
																										.toString())
																						+ 2
																						+ j,
																				0,
																				new HTML(
																						""));
																tables.get(
																		Integer.parseInt(elementAsJSONObject
																				.get("position_x")
																				.isNumber()
																				.toString()))
																		.getElement()
																		.getElementsByTagName(
																				"tr")
																		.getItem(
																				2
																						* Integer
																								.parseInt(elementAsJSONObject
																										.get("position_y")
																										.isNumber()
																										.toString())
																						+ 2
																						+ j)
																		.setClassName(
																				"trHeightNull");
															}
														}
													} else if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("combobox")) {
														JSONValue parametresValue = paramsAsJSONArray
																.get(0);
														JSONObject parametresObject = parametresValue
																.isObject();
														if (parametresObject
																.get("nom")
																.isString()
																.stringValue()
																.equals("titre"))
															paramAsJSONValue = paramsAsJSONArray
																	.get(1);
														else
															paramAsJSONValue = paramsAsJSONArray
																	.get(0);

														parametresObject = paramAsJSONValue
																.isObject();

														ListBox lb = new ListBox();
														lb.addItem(
																"Veuillez selectionnez un choix...",
																"");
														String opt = parametresObject
																.get("valeur")
																.isString()
																.stringValue();
														String o[] = opt
																.split("\\*_\\*");
														for (int j = 0; j < o.length; j++) {
															lb.addItem(o[j],
																	o[j]);
														}
														lb.addStyleName("style_select_apercu");
														widget = lb;
														lb.setName(elementAsJSONObject
																.get("id")
																.isNumber()
																.toString());
														tables.get(
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()))
																.setWidget(
																		2 * Integer
																				.parseInt(elementAsJSONObject
																						.get("position_y")
																						.isNumber()
																						.toString()) + 1,
																		0,
																		widget);
														flexApercu.setWidget(
																2 * Integer
																		.parseInt(elementAsJSONObject
																				.get("position_y")
																				.isNumber()
																				.toString()) + 1,
																Integer.parseInt(elementAsJSONObject
																		.get("position_x")
																		.isNumber()
																		.toString()),
																widget);
													}

													if (elementAsJSONObject
															.get("type")
															.isString()
															.stringValue()
															.equals("date")
															|| elementAsJSONObject
																	.get("type")
																	.isString()
																	.stringValue()
																	.equals("time")) {

														FormulaireController
																.nativeMethod(
																		elementAsJSONObject
																				.get("id")
																				.isNumber()
																				.toString(),
																		elementAsJSONObject
																				.get("type")
																				.isString()
																				.stringValue());
													}

													body.getPart1()
															.appendChild(
																	tables.get(
																			0)
																			.getElement());
													body.getPart2()
															.appendChild(
																	tables.get(
																			1)
																			.getElement());
													body.getPart3()
															.appendChild(
																	tables.get(
																			2)
																			.getElement());

												}
											}
										});
							} catch (RequestException e) {
								System.out.println("RequestException");
							}

						}

						remplirTableau();

					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}

	/*** Enregister le formulaire aprés modification ***/
	public void enregistrerForm() {

		for (int i = 0; i < elementsAsJSONArray.size(); i++) {

			String value = "%20";

			JSONObject elementAsJSONObject = elementsAsJSONArray.get(i)
					.isObject();
			String type = elementAsJSONObject.get("type").isString()
					.stringValue();
			if (type.equals("text") || type.equals("chiffre")
					|| type.equals("date") || type.equals("time")
					|| type.equals("image")) {
				InputElement ie = tables
						.get(Integer.parseInt(elementAsJSONObject
								.get("position_x").isNumber().toString()))
						.getWidget(
								2 * Integer.parseInt(elementAsJSONObject
										.get("position_y").isNumber()
										.toString()) + 1, 0).getElement()
						.cast();
				if (type.equals("date"))
					value = Utils.dateFormatInverse(ie.getValue());
				else
					value = ie.getValue();
			} else if (type.equals("checkbox") || type.equals("radio")) {
				Widget widget = tables.get(
						Integer.parseInt(elementAsJSONObject.get("position_x")
								.isNumber().toString())).getWidget(
						2 * Integer.parseInt(elementAsJSONObject
								.get("position_y").isNumber().toString()) + 1,
						0);
				InputElement input = (InputElement) widget.getElement()
						.getChild(0);

				if (widget.getElement().getChildCount() == 2) {
					if (input.isChecked()) {
						value = "X";
					}
				} else {
					if (input.isChecked()) {
						LabelElement label = (LabelElement) widget.getElement()
								.getChild(1);
						value = label.getInnerHTML();
					}
					if (widget.getElement().getChildCount() > 2) {
						for (int k = 2; k < widget.getElement().getChildCount(); k++) {
							SpanElement span = (SpanElement) widget
									.getElement().getChild(k);
							input = (InputElement) span.getChild(0);

							if (input.isChecked()) {
								LabelElement label = (LabelElement) span
										.getChild(1);
								if (value.equalsIgnoreCase(""))
									value = label.getInnerHTML();
								else
									value = value + "*_*"
											+ label.getInnerHTML();
							}
						}
					}
				}
			} else if (type.equals("combobox")) {
				SelectElement ie = tables
						.get(Integer.parseInt(elementAsJSONObject
								.get("position_x").isNumber().toString()))
						.getWidget(
								2 * Integer.parseInt(elementAsJSONObject
										.get("position_y").isNumber()
										.toString()) + 1, 0).getElement()
						.cast();
				value = ie.getOptions().getItem(ie.getSelectedIndex())
						.getInnerHTML();
			}

			// Traiter les caractères spéciaux
			value = value.replace("%", "%25").replace("/", "%2F")
					.replace("#", "%23").replace(" ", "%20")
					.replace("\"", "%22").replace("&", "%26")
					.replace("(", "%28").replace(")", "%29")
					.replace("+", "%2B").replace(",", "%2C")
					.replace(".", "%2E").replace(":", "%3A")
					.replace(";", "%3B").replace("<", "%3C")
					.replace("=", "%2D").replace(">", "%3E")
					.replace("?", "%3F").replace("@", "%40")
					.replace("[", "%5B").replace("]", "%5D")
					.replace("^", "%5E").replace("'", "%60")
					.replace("{", "%7B").replace("}", "%7D")
					.replace("~", "%7E");

			// value = value.replace(" ", "*_#*");
			// try {
			// value = URL.encode(value);
			// } catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// value = value.replace("*_%23*", "%20");

			final int size = i + 1;

			RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
					VALUE_URL
							+ "modifierValeur/valeur/"
							+ value
							+ "/element/"
							+ elementAsJSONObject.get("id").isNumber()
									.toString() + "/indice/" + indice);
			Window.alert(VALUE_URL + "modifierValeur/valeur/" + value
					+ "/element/"
					+ elementAsJSONObject.get("id").isNumber().toString()
					+ "/indice/" + indice);
			builder.setHeader("Content-Type", "application/json");
			try {
				builder.sendRequest(null, new RequestCallback() {
					public void onError(Request request, Throwable exception) {

					}

					public void onResponseReceived(Request request,
							Response response) {
						if (response.getStatusCode() == Response.SC_OK) {
							if (size == elementsAsJSONArray.size()) {
								Window.alert("Modification avec succès!! ");
								Window.Location.reload();
							}
						}
					}
				});

			} catch (RequestException e) {
				System.out.println("RequestException");
			}
		}
	}

	/*** Fonction native pour initialiser un champs date à datepicker ***/
	public static native void nativeMethod(String id)
	/*-{
		$wnd.$('.' + id).datepicker({
			dateFormat : 'dd/mm/yy'
		});
	}-*/;

}
