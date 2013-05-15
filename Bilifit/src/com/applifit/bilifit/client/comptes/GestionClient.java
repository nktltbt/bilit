package com.applifit.bilifit.client.comptes;

import com.applifit.bilifit.client.outils.BuildJsonObject;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.comptes.AjoutEntreprise;
import com.applifit.bilifit.client.templatesUibinder.comptes.FacturationBloc;
import com.applifit.bilifit.client.templatesUibinder.comptes.HeaderAdministration;
import com.applifit.bilifit.client.templatesUibinder.comptes.QuotaAjout;
import com.applifit.bilifit.client.templatesUibinder.comptes.SousGestionCU;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
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
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;

public class GestionClient implements EntryPoint {
	private HeaderAdministration menu;
	private AjoutEntreprise formEntreprise;
	private SousGestionCU sousgestioncu;
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()
			+ "compte/";
	private static final String Entreprise_URL = GWT.getHostPageBaseURL()
			+ "entreprise/";

	// cette variable est crée pour indiquer l'etats des champs (enable = modifiable , disable = non modifiable)
	private boolean enable = true;

	private String clientId;
	private String raisonsocial = "";
	private String adresse = "";
	private String siret = "";
	private String ville = "";
	private String cp = "";
	private String pays = "";
	private String contact_nom = "";
	private String contact_prenom = "";
	private String offre = "";
	private String date_creation = "";
	private String fonction = "";
	private String telephone = "";
	private String mail = "";
	private String quota_form = "";
	private String quota_user = "";

	@Override
	public void onModuleLoad() {

		//contrôle d'accés
		if (Cookies.getCookie("profil") == null) {
			Window.Location.replace(GWT.getHostPageBaseURL());
		}else if(Cookies.getCookie("client") == null)
			Window.Location.replace(GWT.getHostPageBaseURL()+ "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
		else{

		// AFFICHAGE DU HEADER
		menu = new HeaderAdministration(Constantes.USER);
		Document.get().getBody().appendChild(menu.getElement());

		Anchor wrapper = Anchor.wrap(menu.getDeconnexion());
		wrapper.addClickHandler(new ClickHandler() {
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
				if(Cookies.getCookie("profil").equals("SA"))
					Window.Location.replace(GWT.getHostPageBaseURL() + "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
				else Window.Location.replace(GWT.getHostPageBaseURL() + "Accueil_applifit.html" + Constantes.SUFFIXE_URL);
			}

		});
		/*** action de rubrique formulaire ***/
		Anchor formulaireWrapper = Anchor.wrap(menu.getFormulaire());
		formulaireWrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(Cookies.getCookie("profil").equals("SA") || Cookies.getCookie("profil").equals("CA"))
					Window.Location.replace(GWT.getHostPageBaseURL()+ "Formulaire_menu_applifit.html" + Constantes.SUFFIXE_URL);
				else Window.Location.replace(GWT.getHostPageBaseURL()+ "Formulaire_consulter_applifit.html" + Constantes.SUFFIXE_URL);
			}

		});

		// CHARGER L'ENTREPRISE
		clientId = Cookies.getCookie("client");

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				Entreprise_URL + clientId);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération d'entreprise");
				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					String compteAsJSONString = response.getText();
					if (compteAsJSONString.isEmpty()) {
						Window.alert(raisonsocial
								+ ": Cette raison sociale ne correspond pas à  une entreprise cliente");
						return;
					} else {

						JSONValue compteAsJSONValue = JSONParser.parse(compteAsJSONString);
						final JSONObject compteAsJSONObject = compteAsJSONValue
								.isObject();

						raisonsocial = compteAsJSONObject.get("raison_sociale")
								.isString().stringValue();
						adresse = compteAsJSONObject.get("adresse").isString()
								.stringValue();
						siret = compteAsJSONObject.get("siret").isString()
								.stringValue();
						ville = compteAsJSONObject.get("ville").isString()
								.stringValue();
						cp = compteAsJSONObject.get("code_postal").isString()
								.stringValue();
						pays = compteAsJSONObject.get("pays").isString()
								.stringValue();
						contact_nom = compteAsJSONObject.get("contact_nom")
								.isString().stringValue();
						contact_prenom = compteAsJSONObject
								.get("contact_prenom").isString().stringValue();
						offre = compteAsJSONObject.get("offre_souscrite")
								.isString().stringValue();
						date_creation = compteAsJSONObject.get("date").isString().stringValue();
						
						fonction = compteAsJSONObject.get("contact_fonction")
								.isString().stringValue();
						telephone = compteAsJSONObject.get("contact_tel")
								.isString().stringValue();
						mail = compteAsJSONObject.get("contact_mail")
								.isString().stringValue();
						quota_form = compteAsJSONObject.get("quota_formulaire").isNumber().toString();
						quota_user =compteAsJSONObject.get("quota_utilisateur").isNumber().toString();					

						formEntreprise = new AjoutEntreprise(raisonsocial,
								adresse, siret, ville, cp, pays, contact_nom,
								contact_prenom, offre, date_creation, fonction,
								telephone, mail ,quota_form,quota_user);
						if (Utils.VerificationProfil("CA")
								|| Utils.VerificationProfil("CU")) {
							formEntreprise.getEnregistrer().removeFromParent();

						}

						Document.get()
								.getBody()
								.insertAfter(formEntreprise.getElement(),
										menu.getElement());
						
						nativeMethod("datepicker");

						/******************************************************************/
						sousgestioncu = new SousGestionCU();
						if (Utils.VerificationProfil("CA")){
							sousgestioncu.getProfil_admin().removeFromParent();
							sousgestioncu.getProfil_label().removeFromParent();
						}

						if (Utils.VerificationProfil("CU"))
							sousgestioncu.getInfonew().removeFromParent();

						loadUtilisateurs(COMPTE_URL
								+ "listerCompteEntrepriseAU/client/" + clientId);

						/******************************************************************/
						QuotaAjout quotaajout = new QuotaAjout();
						Document.get().getBody()
								.appendChild(quotaajout.getElement());
						/******************************************************************/
						FacturationBloc facturation = new FacturationBloc();
						Document.get().getBody()
								.appendChild(facturation.getElement());
						if (Utils.VerificationProfil("SA")){
							Button buttonWrapper = Button.wrap(formEntreprise
									.getEnregistrer());
	
							buttonWrapper.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									if (enable) {
										// CHANGER L'ETAT DES BUTTONS : enable
										enable = false;
										formEntreprise.getTitreButton()
												.setInnerText("Enregistrer");
										formEntreprise.getForm_RS().setReadOnly(
												false);
										formEntreprise.getAdresse().setReadOnly(
												false);
										formEntreprise.getSiret()
												.setReadOnly(false);
										formEntreprise.getVille()
												.setReadOnly(false);
										formEntreprise.getCp().setReadOnly(false);
										formEntreprise.getPays().setReadOnly(false);
										formEntreprise.getContact_nom()
												.setReadOnly(false);
										formEntreprise.getContact_prenom()
												.setReadOnly(false);
										formEntreprise.getOffre()
												.setReadOnly(false);
										formEntreprise.getDate_creation()
												.setReadOnly(false);
										formEntreprise.getFonction().setReadOnly(
												false);
										formEntreprise.getTelephone().setReadOnly(
												false);
										formEntreprise.getMail().setReadOnly(true);
										formEntreprise.getQuota_form().setReadOnly(false);
										formEntreprise.getQuota_user().setReadOnly(false);
	
									} else {
										// ENREGISTRER ET CHANGER L'ETAT DES BUTTONS
										// : disable
										// ENREGISTER L'ENTREPRISE
										raisonsocial = (formEntreprise.getForm_RS())
												.getValue().trim();
										adresse = (formEntreprise.getAdresse())
												.getValue().trim();
										siret = (formEntreprise.getSiret())
												.getValue().trim();
										ville = (formEntreprise.getVille())
												.getValue().trim();
										cp = (formEntreprise.getCp()).getValue()
												.trim();
										pays = (formEntreprise.getPays())
												.getValue().trim();
										contact_nom = (formEntreprise
												.getContact_nom()).getValue()
												.trim();
										contact_prenom = (formEntreprise
												.getContact_prenom()).getValue()
												.trim();
										offre = (formEntreprise.getOffre())
												.getValue().trim();
										date_creation = (formEntreprise
												.getDate_creation()).getValue()
												.trim();
										fonction = (formEntreprise.getFonction())
												.getValue().trim();
										telephone = (formEntreprise.getTelephone())
												.getValue().trim();
										mail = (formEntreprise.getMail())
												.getValue().trim();
	
										quota_form =(formEntreprise.getQuota_form())
												.getValue().trim();
										quota_user = (formEntreprise.getQuota_user())
												.getValue().trim();
										
										if (ValiderEntreprise()) {
											enable = true;
											formEntreprise.getTitreButton()
													.setInnerText("Modifier");
											formEntreprise.getForm_RS()
													.setReadOnly(true);
											formEntreprise.getAdresse()
													.setReadOnly(true);
											formEntreprise.getSiret().setReadOnly(
													true);
											formEntreprise.getVille().setReadOnly(
													true);
											formEntreprise.getCp()
													.setReadOnly(true);
											formEntreprise.getPays().setReadOnly(
													true);
											formEntreprise.getContact_nom()
													.setReadOnly(true);
											formEntreprise.getContact_prenom()
													.setReadOnly(true);
											formEntreprise.getOffre().setReadOnly(
													true);
											formEntreprise.getDate_creation()
													.setReadOnly(true);
											formEntreprise.getFonction()
													.setReadOnly(true);
											formEntreprise.getTelephone()
													.setReadOnly(true);
											formEntreprise.getMail().setReadOnly(
													true);
											formEntreprise.getQuota_form().setReadOnly(
													true);
											formEntreprise.getQuota_user().setReadOnly(
													true);
	
											modifierEntreprise();
	
										}
									}
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
	}

	
	
	public static native void nativeMethod(String id)
	/*-{
		  $wnd.$('.'+id).datepicker({ dateFormat: 'dd/mm/yy' });
	}-*/;
	
	
	private boolean ValiderEntreprise() {
		boolean repense = true;

		//controle sur champs
		if (raisonsocial.isEmpty()) {
			Window.alert("Le champs Raison sociale est obligatoire");
			repense = false;
		}

		if (siret.isEmpty()) {
			Window.alert("Le champs SIRET est obligatoire");
			repense = false;
		}

		if (adresse.isEmpty()) {
			Window.alert("Le champs Adresse est obligatoire");
			repense = false;
		}

		if (ville.isEmpty()) {
			Window.alert("Le champs Ville est obligatoire");
			repense = false;
		}

		if (cp.isEmpty()) {
			Window.alert("Le champs Code Postal est obligatoire");
			repense = false;
		}

		if (pays.isEmpty()) {
			Window.alert("Le champs Pays est obligatoire");
			repense = false;
		}

		if (offre.isEmpty()) {
			Window.alert("Le champs Offre souscrite est obligatoire");
			repense = false;
		}

		if (date_creation.isEmpty()) {
			Window.alert("Le champs Date est obligatoire");
			repense = false;
		}

		if (contact_nom.isEmpty()) {
			Window.alert("Le champs nom est obligatoire");
			repense = false;
		}

		if (contact_prenom.isEmpty()) {
			Window.alert("Le champs prenom est obligatoire");
			repense = false;
		}

		if (fonction.isEmpty()) {
			Window.alert("Le champs Fonction est obligatoire");
			repense = false;
		}

		if (telephone.isEmpty()) {
			Window.alert("Le champs Télephone est obligatoire");
			repense = false;
		}

		if (mail.isEmpty()) {
			Window.alert("Le champs Mail est obligatoire");
			repense = false;
		}

		if (!mail
				.matches("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")) {
			Window.alert("'" + mail + "': Adresse Email invalide");
			repense = false;
		}
		
		try {
			Integer.parseInt(quota_form);
		} catch (Exception e) {
			Window.alert("Le champs 'Nombre de formulaires de collecte' doit être un nombre entier");
			repense = false;
		}

		try {
			Integer.parseInt(quota_user);
		} catch (Exception e) {
			Window.alert("Le champs 'Nombre d'utilisateurs de Bilifit' doit être un nombre entier");
			repense = false;
		}

		return repense;
	}

	private void modifierEntreprise() {
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, Entreprise_URL + "ajouterEntreprise");
		builder.setHeader("Content-Type", "application/json");

		final JSONObject entrepriseAsJSONObject = buildJSONObject(clientId,
				raisonsocial, adresse, siret, ville, cp, pays, contact_nom,
				contact_prenom, offre, date_creation, fonction, telephone, mail, quota_form, quota_user);
		try {
			builder.sendRequest(entrepriseAsJSONObject.toString(),
					new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							Window.alert("Erreur d'ajout");
						}

						public void onResponseReceived(Request request,
								Response response) {
							System.out.print(response.getText());
							/*
							 * initialiserPage(ajoutSa); container = new
							 * ContainerSA(); pageListeSa();
							 */
						}
					});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

	}

	private JSONObject buildJSONObject(String clientId, String form_RS,
			String adresse, String siret, String ville, String cp, String pays,
			String contact_nom, String contact_prenom, String offre,
			String date_creation, String fonction, String telephone, String mail, String quota_form, String quota_user) {

		JSONObject entrepriseAsJSONObject = new JSONObject();

		entrepriseAsJSONObject.put("id", new JSONString(clientId));
		entrepriseAsJSONObject.put("raison_sociale", new JSONString(form_RS));
		entrepriseAsJSONObject.put("siret", new JSONString(siret));
		entrepriseAsJSONObject.put("adresse", new JSONString(adresse));
		entrepriseAsJSONObject.put("ville", new JSONString(ville));
		entrepriseAsJSONObject.put("code_postal", new JSONString(cp));
		entrepriseAsJSONObject.put("pays", new JSONString(pays));
		entrepriseAsJSONObject.put("contact_nom", new JSONString(contact_nom));
		entrepriseAsJSONObject.put("contact_prenom", new JSONString(
				contact_prenom));
		entrepriseAsJSONObject.put("offre_souscrite", new JSONString(offre));
		entrepriseAsJSONObject.put("date", new JSONString(date_creation));
		entrepriseAsJSONObject
				.put("contact_fonction", new JSONString(fonction));
		entrepriseAsJSONObject.put("contact_tel", new JSONString(telephone));
		entrepriseAsJSONObject.put("contact_mail", new JSONString(mail));
		entrepriseAsJSONObject.put("quota_formulaire", new JSONString(quota_form));
		entrepriseAsJSONObject.put("quota_utilisateur", new JSONString(quota_user));

		return entrepriseAsJSONObject;
	}

	private void ajouterClient() {
		final String nom = (sousgestioncu.getNom()).getValue().trim();
		final String prenom = (sousgestioncu.getPrenom()).getValue().trim();
		final String fonction = (sousgestioncu.getFonction()).getValue().trim();
		final String mail = (sousgestioncu.getMail()).getValue().trim();
		final String profil;
		boolean ca = sousgestioncu.getProfil_admin().isChecked();
		
		//controle sur les champs
		if (nom.isEmpty()) {
			Window.alert("Le champs Nom est obligatoire");
			return;
		}
		if (prenom.isEmpty()) {
			Window.alert("Le champs Prénom est obligatoire");
			return;
		}
		if (mail.isEmpty()) {
			Window.alert("Le champs Mail est obligatoire");
			return;
		}
		if (!mail
				.matches("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")) {
			Window.alert("'" + mail + "': Adresse Email invalide");
			return;
		}
		if (fonction.isEmpty()) {
			Window.alert("Le champs Fonction est obligatoire");
			return;
		}
		if (ca) {
			profil = "CA";
		} else
			profil = "CU";

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				COMPTE_URL + "recupererCompte/email/" + mail);
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
						Window.alert(mail
								+ ": Cette adresse email correspond à un utilisateur actif");
						return;
					} else {

						RequestBuilder builder = new RequestBuilder(
								RequestBuilder.GET, Entreprise_URL
										+ Cookies.getCookie("client"));
						builder.setHeader("Content-Type", "application/json");

						try {
							builder.sendRequest(null, new RequestCallback() {
								public void onError(Request request,
										Throwable exception) {

								}

								@SuppressWarnings("deprecation")
								public void onResponseReceived(Request request,
										Response response) {
									if (response.getStatusCode() == Response.SC_OK) {
										String entrepriseAsJSONString = response
												.getText();
										JSONValue entrepriseAsJSONValue = JSONParser
												.parse(entrepriseAsJSONString);
										final JSONObject compteAsJSONObject = BuildJsonObject
												.buildJSONObjectCompte("-1",
														nom, prenom, null,
														fonction, mail, profil,
														entrepriseAsJSONValue
																.isObject());
										try {
											RequestBuilder builder = new RequestBuilder(
													RequestBuilder.POST,
													COMPTE_URL
															+ "ajouterCompte");
											builder.setHeader("Content-Type",
													"application/json");
											builder.sendRequest(
													compteAsJSONObject
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
															Document.get()
																	.getBody()
																	.removeChild(
																			sousgestioncu
																					.getElement());
															sousgestioncu = new SousGestionCU();
															if (Utils
																	.VerificationProfil("CA"))
																sousgestioncu
																		.getProfil_admin()
																		.setDisabled(
																				true);
															if (Utils
																	.VerificationProfil("CU"))
																sousgestioncu
																		.getInfonew()
																		.removeFromParent();
															loadUtilisateurs(COMPTE_URL
																	+ "listerCompteEntrepriseAU/client/"
																	+ Cookies
																			.getCookie("client"));
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
							System.out.println("RequestException");
						}
					}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}

	}

	private void modifierClient(boolean owner) {
		final String id = (sousgestioncu.getId()).getValue().trim();
		final String nom = (sousgestioncu.getNom()).getValue().trim();
		final String prenom = (sousgestioncu.getPrenom()).getValue().trim();
		final String fonction = (sousgestioncu.getFonction()).getValue().trim();
		final String mail = (sousgestioncu.getMail()).getValue().trim();
		final String mdp1 = (sousgestioncu.getMdp1()).getValue().trim();
		final String mdp2 = (sousgestioncu.getMdp2()).getValue().trim();
		final String profil;
		boolean ca = sousgestioncu.getProfil_admin().isChecked();
		if (nom.isEmpty()) {
			Window.alert("Le champs Nom est obligatoire");
			return;
		}
		if (prenom.isEmpty()) {
			Window.alert("Le champs Prénom est obligatoire");
			return;
		}
		if (fonction.isEmpty()) {
			Window.alert("Le champs Fonction est obligatoire");
			return;
		}
		String pass = null;
		if (owner && !mdp1.equals(mdp2)) {
			Window.alert("Les deux mots de passe sont différents");
			return;
		} else if (!mdp1.equals(""))
			pass = mdp1;
		if (ca) {
			profil = "CA";
		} else
			profil = "CU";

		final JSONObject compteAsJSONObject = BuildJsonObject.buildJSONObjectCompte(id, nom, prenom, pass, fonction, mail,profil, null);
		try {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
					COMPTE_URL + "modifierCompteClient");
			builder.setHeader("Content-Type", "application/json");
			builder.sendRequest(compteAsJSONObject.toString(),
					new RequestCallback() {
						public void onError(Request request, Throwable exception) {
							Window.alert("Erreur d'ajout");
						}

						public void onResponseReceived(Request request,
								Response response) {
							Window.alert("Modification avec succès"
									+ response.getText());
							Document.get().getBody()
									.removeChild(sousgestioncu.getElement());
							sousgestioncu = new SousGestionCU();
							if (Utils.VerificationProfil("CA"))
								sousgestioncu.getProfil_admin().setDisabled(
										true);

							if (Utils.VerificationProfil("CU"))
								sousgestioncu.getInfonew().removeFromParent();

							sousgestioncu.getInfo().removeClassName(
									"entete_ferme");
							sousgestioncu.getInfo().addClassName("entete_open");

							sousgestioncu.getInfos_collabs().addClassName(
									"block");
							loadUtilisateurs(COMPTE_URL
									+ "listerCompteEntrepriseAU/client/"
									+ Cookies.getCookie("client"));
						}
					});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

	}

	private void loadUtilisateurs(String url) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						String comptesAsJSONString = response.getText();

						JSONValue comptesAsJSONValue = JSONParser
								.parse(comptesAsJSONString);
						JSONArray comptesAsJSONArray = comptesAsJSONValue
								.isArray();
						String class_cell = "";
						for (int i = 0; i < comptesAsJSONArray.size(); i++) {

							JSONValue contactAsJSONValue = comptesAsJSONArray
									.get(i);
							final JSONObject contactAsJSONObject = contactAsJSONValue
									.isObject();

							if (i % 2 != 1)
								class_cell = "table_ligne_clair";
							else
								class_cell = "table_ligne_fonce";
							TableRowElement row = sousgestioncu.getTbody()
									.insertRow(-1);
							TableCellElement cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("nom")
									.isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("prenom")
									.isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);

							cell.setInnerHTML(contactAsJSONObject
									.get("fonction").isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("profil")
									.isString().stringValue());

							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.addClassName("img_edit");
							Image edit = new Image();
							Hidden hiddden_id = new Hidden();
							Hidden hiddden_mail = new Hidden();
							hiddden_id.setValue(contactAsJSONObject.get("id")
									.isNumber().toString());
							edit.setUrl("images/Edit.png");
							cell.appendChild(hiddden_id.getElement());
							cell.appendChild(edit.getElement());
							hiddden_mail.setValue(contactAsJSONObject
									.get("mail").isString().stringValue());
							cell.appendChild(hiddden_mail.getElement());
							Image delete = new Image();
							delete.setUrl("images/delete.png");
							cell.appendChild(delete.getElement());

							if (Utils.VerificationProfil("CA")
									&& contactAsJSONObject.get("profil")
											.isString().stringValue()
											.equals("CA")) {
								delete.setVisible(false);
								if (!contactAsJSONObject.get("id").isNumber()
										.toString()
										.equals(Cookies.getCookie("id")))
									edit.setVisible(false);
							}

							if (Utils.VerificationProfil("CU")) {
								delete.removeFromParent();
								if (!contactAsJSONObject.get("id").isNumber()
										.toString()
										.equals(Cookies.getCookie("id")))
									edit.removeFromParent();
							}
						}
						ajouterSousGestionCu();
					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

	}

	private void ajouterSousGestionCu() {
		Document.get()
				.getBody()
				.insertAfter(sousgestioncu.getElement(),
						formEntreprise.getElement());
		Button cuajoutWrapper = Button.wrap(sousgestioncu.getEnregistrer());

		cuajoutWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ajouterClient();
			}
		});
		NodeList<TableRowElement> rows = sousgestioncu.getTbody().getRows();

		for (int i = 1; i < rows.getLength(); i++) {
			final NodeList<TableCellElement> cells = rows.getItem(i).getCells();
			final TableCellElement c = cells.getItem(cells.getLength() - 1);

			/******************************************************************/
			ImageElement edit = (ImageElement) c.getChild(1);
			Image imageWrapper = Image.wrap(edit);

			final InputElement id = (InputElement) c.getChild(0);

			imageWrapper.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					boolean owner = false;
					if (id.getValue().equals(Cookies.getCookie("id")))
						owner = true;

					Document.get().getBody()
							.removeChild(sousgestioncu.getElement());

					sousgestioncu = new SousGestionCU();
					if (Utils.VerificationProfil("CA")
							|| Utils.VerificationProfil("CU"))
						sousgestioncu.getProfil_admin().setDisabled(true);
					sousgestioncu.getEnregistrer().setInnerText("Modifier");
					sousgestioncu.getInfo().removeClassName("entete_ferme");
					sousgestioncu.getInfo().addClassName("entete_open");
					sousgestioncu.getInfonew().addClassName("block");
					sousgestioncu.getInfos_collabs().addClassName("block");
					sousgestioncu.setNomValue(cells.getItem(0).getInnerText());
					sousgestioncu.setPrenomValue(cells.getItem(1)
							.getInnerText());
					sousgestioncu.setFonctionValue(cells.getItem(2)
							.getInnerText());
					InputElement mail = (InputElement) cells.getItem(
							cells.getLength() - 1).getChild(2);
					sousgestioncu.setMailValue(mail.getValue());
					sousgestioncu.getMail().setDisabled(true);
					sousgestioncu.getId().setValue(id.getValue());

					if (owner) {
						sousgestioncu.getMdpDiv().addClassName("passeVisible");
						sousgestioncu.getMdpDiv().removeClassName("passeHidden");
					}
					if (cells.getItem(3).getInnerText().equals("CA")) {
						sousgestioncu.getProfil_admin().setChecked(true);
					}

					modifierSousGestionCu(owner);

				}
			});
			/******************************************************************/
			ImageElement delete = (ImageElement) c.getChild(3);
			imageWrapper = Image.wrap(delete);

			imageWrapper.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {

					Boolean response = Window
							.confirm("Voulez-vous vraiment supprimer cet utilisateur? Cette action est irreversible");
					if (response) {
						supprimerCompte(id.getValue());
					}
				}
			});
		}
	}

	private void modifierSousGestionCu(final boolean owner) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				COMPTE_URL + "listerCompteEntrepriseAU/client/"
						+ Cookies.getCookie("client"));
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						String comptesAsJSONString = response.getText();

						JSONValue comptesAsJSONValue = JSONParser
								.parse(comptesAsJSONString);
						JSONArray comptesAsJSONArray = comptesAsJSONValue
								.isArray();
						String class_cell = "";
						for (int i = 0; i < comptesAsJSONArray.size(); i++) {

							JSONValue contactAsJSONValue = comptesAsJSONArray
									.get(i);
							final JSONObject contactAsJSONObject = contactAsJSONValue
									.isObject();

							if (i % 2 != 1)
								class_cell = "table_ligne_clair";
							else
								class_cell = "table_ligne_fonce";
							TableRowElement row = sousgestioncu.getTbody()
									.insertRow(-1);
							TableCellElement cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("nom")
									.isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("prenom")
									.isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);

							cell.setInnerHTML(contactAsJSONObject
									.get("fonction").isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("profil")
									.isString().stringValue());

							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.addClassName("img_edit");
							Image edit = new Image();
							Hidden hiddden_id = new Hidden();
							Hidden hiddden_mail = new Hidden();
							hiddden_id.setValue(contactAsJSONObject.get("id")
									.isNumber().toString());
							edit.setUrl("images/Edit.png");
							cell.appendChild(hiddden_id.getElement());
							cell.appendChild(edit.getElement());
							hiddden_mail.setValue(contactAsJSONObject
									.get("mail").isString().stringValue());
							cell.appendChild(hiddden_mail.getElement());
							Image delete = new Image();
							delete.setUrl("images/delete.png");
							cell.appendChild(delete.getElement());

							if (Utils.VerificationProfil("CA")
									&& contactAsJSONObject.get("profil")
											.isString().stringValue()
											.equals("CA")) {
								delete.setVisible(false);
								if (!contactAsJSONObject.get("id").isNumber()
										.toString()
										.equals(Cookies.getCookie("id")))
									edit.setVisible(false);
							}

							if (Utils.VerificationProfil("CU")) {
								delete.removeFromParent();
								if (!contactAsJSONObject.get("id").isNumber()
										.toString()
										.equals(Cookies.getCookie("id")))
									edit.removeFromParent();
							}
						}

					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
		Document.get()
				.getBody()
				.insertAfter(sousgestioncu.getElement(),
						formEntreprise.getElement());
		Button cumodifWrapper = Button.wrap(sousgestioncu.getEnregistrer());

		cumodifWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				modifierClient(owner);
			}
		});

		Button annuler = Button.wrap(sousgestioncu.getAnnuler());
		annuler.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Document.get().getBody()
						.removeChild(sousgestioncu.getElement());
				sousgestioncu = new SousGestionCU();
				loadUtilisateurs(COMPTE_URL
						+ "listerCompteEntrepriseAU/client/"
						+ Cookies.getCookie("client"));
				sousgestioncu.getInfo().removeClassName("entete_ferme");
				sousgestioncu.getInfo().addClassName("entete_open");
				sousgestioncu.getInfos_collabs().addClassName("block");
			}
		});
	}

	private void supprimerCompte(String id) {
		String deleteUrl = COMPTE_URL + "supprimerCompteId/" + id;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,
				deleteUrl);
		builder.setHeader("Content-Type", "application/json");
		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
				}

				public void onResponseReceived(Request request,
						Response response) {
						
					Document.get().getBody().removeChild(sousgestioncu.getElement());
					sousgestioncu = new SousGestionCU();
					if (Utils.VerificationProfil("CA")
							|| Utils.VerificationProfil("CU"))
						sousgestioncu.getProfil_admin().setDisabled(true);
					loadUtilisateurs(COMPTE_URL
							+ "listerCompteEntrepriseAU/client/"
							+ Cookies.getCookie("client"));
					sousgestioncu.getInfo().removeClassName("entete_ferme");
					sousgestioncu.getInfo().addClassName("entete_open");
					sousgestioncu.getInfos_collabs().addClassName("block");
					}
				
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}

}
