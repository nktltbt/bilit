package com.applifit.bilifit.client.comptes;

import com.applifit.bilifit.client.outils.BuildJsonObject;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.comptes.AjoutClient;
import com.applifit.bilifit.client.templatesUibinder.comptes.HeaderAccueilAdmin;
import com.applifit.bilifit.client.templatesUibinder.comptes.ListeClient;
import com.applifit.bilifit.client.templatesUibinder.comptes.ModifierClient;
import com.applifit.bilifit.client.templatesUibinder.comptes.RechercheClient;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
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
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;


public class Accueil_superadmin implements EntryPoint {

	private AjoutClient formClient;
	private ModifierClient modifierClient;
	private RechercheClient recherche;
	private static final String Entreprise_URL = GWT.getHostPageBaseURL()
			+ "entreprise/";
	private ListeClient liste;
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()+ "compte/";

	
	@Override
	public void onModuleLoad() {

		// Seul le profil SA peut se connecter à cette page
		
	if(Cookies.getCookie("profil") == null || !Cookies.getCookie("profil").equals("SA"))
		Window.Location.replace(GWT.getHostPageBaseURL());
	else {
		
		/*** La partie menu ***/
		HeaderAccueilAdmin menu = new HeaderAccueilAdmin(Constantes.USER);
		Document.get().getBody().appendChild(menu.getElement());
		Anchor gsaWrapper = Anchor.wrap(menu.getGsa());
		/*** action sur la rubrique Gestion Super Admin ***/
		gsaWrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "GestionSA.html" + Constantes.SUFFIXE_URL);
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

		/******************************************************************/

		/*** Ajouter la partie recherche à la page ***/
		recherche = new RechercheClient();
		Document.get().getBody().appendChild(recherche.getElement());
		
		nativeMethod("datepicker");
		
		/*** L'action pour le lien Ajouter un client***/
		Anchor anchorWrapper = Anchor.wrap(recherche.getLinkajout());
		anchorWrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				/*** Puisqu'on travaille sur une seule page, on supprime la zone de recherche et la zone de liste et on ajoute le formulaire d'ajout ***/
				Document.get().getBody().removeChild(recherche.getElement());
				Document.get().getBody().removeChild(liste.getElement());
				pageAjout();
			}

		});
		
		/*** L'action pour le button de recherche***/

		Button buttonWrapper = Button.wrap(recherche.getRechercher());
		buttonWrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rechercherClient();
			}

		});
		/******************************************************************/

		/*** Charger la liste des clients***/
		liste = new ListeClient();
		loadClients(Entreprise_URL + "listerEntreprise");
		
		
		/******************************************************************/
		}
	}

	
	
	public static native void nativeMethod(String id)
	/*-{
		  $wnd.$('.'+id).datepicker({ dateFormat: 'dd/mm/yy' });
	}-*/;
	
	
	/*** Fonction de recherche d'un client ***/
	
	private void rechercherClient() {

		String rs;
		String ville_cp;
		String contact_nom;
		String contact_mail;
		String offre;
		String date;

		if ((recherche.getRs()).getValue().trim().isEmpty())
			rs = "-";
		else
			rs = (recherche.getRs()).getValue().trim();

		if ((recherche.getVille_cp()).getValue().trim().isEmpty())
			ville_cp = "-";
		else
			ville_cp = (recherche.getVille_cp()).getValue().trim();

		if ((recherche.getContact_nom()).getValue().trim().isEmpty())
			contact_nom = "-";
		else
			contact_nom = (recherche.getContact_nom()).getValue().trim();

		if ((recherche.getContact_mail()).getValue().trim().isEmpty())
			contact_mail = "-";
		else
			contact_mail = (recherche.getContact_mail()).getValue().trim();

		if ((recherche.getOffre()).getValue().trim().isEmpty())
			offre = "-";
		else
			offre = (recherche.getOffre()).getValue().trim();

		if ((recherche.getDate()).getValue().trim().isEmpty())
			date = "-";
		else{
			date= (recherche.getDate()).getValue().trim();
			
			
		}
			
		Document.get().getBody().removeChild(liste.getElement());
		liste = new ListeClient();
		loadClients(Entreprise_URL + "rechercherEntreprise/rs/" + rs
				+ "/ville/" + ville_cp + "/contact_nom/" + contact_nom
				+ "/contact_mail/" + contact_mail + "/offre/" + offre
				+ "/date/" + date.replaceAll("/", "%2F"));
		
	}
	
	
	/*** Créer la page d'ajout avec l'action du button ajouter***/
	private void pageAjout() {

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

		
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,COMPTE_URL+"recupererCompte/email/"+contact_mail);
		builder.setHeader("Content-Type", "application/json");
		 try {
	
	            builder.sendRequest(null, new RequestCallback() {
	            	
	                public void onError(Request request, Throwable exception) {
	                	Window.alert("erreur de récupération de mail");
	                }

	                public void onResponseReceived(Request request, Response response) {
	                	
	                	String compteAsJSONString = response.getText();
                        if(!compteAsJSONString.isEmpty()){
                        	Window.alert(contact_mail + ": Cette adresse email correspond à un utilisateur actif");
                        	return;
                        }else{
						RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Entreprise_URL + "recupererEntreprise/raisonsocial/" + rs);
						builder.setHeader("Content-Type", "application/json");
						try {
				
							builder.sendRequest(null, new RequestCallback() {
				
								public void onError(Request request, Throwable exception) {
									Window.alert("erreur de récupération d'entreprise");
								}
				
								public void onResponseReceived(Request request,
										Response response) {
									String compteAsJSONString = response.getText();
									if (!compteAsJSONString.isEmpty()) {
										Window.alert(rs
												+ ": Cette raison sociale correspond à une entreprise cliente");
										return;
									} else {
										RequestBuilder builder = new RequestBuilder(
												RequestBuilder.POST, Entreprise_URL + "ajouterEntreprise");
										builder.setHeader("Content-Type", "application/json");
				
										final JSONObject entrepriseAsJSONObject = BuildJsonObject
												.buildJSONObjectEntreprise(null, rs, siret, adresse,
														ville, cp, pays, offre, dateString,
														contact_nom, contact_prenom,
														contact_mail, contact_tel,
														contact_fonction, quota_formulaire,
														quota_utilisateur);
										try {
											builder.sendRequest(
													entrepriseAsJSONObject.toString(),
													new RequestCallback() {
														public void onError(Request request,
																Throwable exception) {
															Window.alert("Erreur d'ajout");
														}
				
														public void onResponseReceived(
																Request request,
																Response response) {
															Window.alert("Ajout avec succès");
															Window.Location.reload();
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
	                }}
	            });
	        } catch (RequestException e) {
	        	Window.alert("RequestException");
	        }	
						
	}

	/*** Charger la liste des clients avec les radio button pour la selection ***/
	
	private void loadClients(String url) {
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
							
							if(contactAsJSONObject.get("etat").isNumber().toString().equals("0"))
								class_cell = "table_ligne_clair_grey";
							TableRowElement row = liste.getTbody()
									.insertRow(-1);
							/*RadioButton selection = new RadioButton("client");
							selection.setFormValue(contactAsJSONObject
									.get("id").isNumber().toString());*/
							TableCellElement cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							/*cell.addClassName("td_radio");
							cell.appendChild(selection.getElement());
							cell = row.insertCell(-1);*/
							cell.addClassName(class_cell);
							Label rs_label = new Label();
							rs_label.setText(contactAsJSONObject
									.get("raison_sociale").isString()
									.stringValue());
							cell.appendChild(rs_label.getElement());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("ville")
									.isString().stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject
									.get("code_postal").isString()
									.stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject
									.get("offre_souscrite").isString()
									.stringValue());
							cell = row.insertCell(-1);
							cell.addClassName(class_cell);
							cell.setInnerHTML(contactAsJSONObject.get("date")
									.isString().stringValue());
							
							//Ajouter l'icon de suppression
							
              		      	Image delete = new Image();
							delete.setUrl("images/delete.png");
							Hidden hidden_id = new Hidden();
							hidden_id.setValue(contactAsJSONObject.get("id").isNumber().toString());
            		      	cell = row.insertCell(-1);
	          		      	cell.addClassName(class_cell);
	          		      	cell.addClassName("img_edit");
	          		      	cell.appendChild(hidden_id.getElement());
	          		      	if(!contactAsJSONObject.get("etat").isNumber().toString().equals("0"))
	          		      	cell.appendChild(delete.getElement());
						}
						
						
						addListePage();
						
						NodeList<TableRowElement> rows = liste.getTbody().getRows();
						for (int i = 1; i < rows.getLength(); i++) {
							NodeList<TableCellElement> cells = rows.getItem(i).getCells();
							TableCellElement c = cells.getItem(cells.getLength() - 1);
							final InputElement id = (InputElement) c.getChild(0);
							
							if(c.getChildCount()==2){
							ImageElement delete = (ImageElement)c.getChild(1);
							Image deleteWrapper = Image.wrap(delete);
							
							deleteWrapper.addClickHandler(new ClickHandler() {
								public void onClick(ClickEvent event) {
									Boolean response = Window.confirm("Voulez-vous vraiment supprimer cette entreprise? ");
									if(response){
										supprimerEntreprise(id.getValue());
									}

								}

								
							});
							}
							
							
							
							//Click sur la raison sociale
							c = cells.getItem(0);
							LabelElement rs = (LabelElement) c.getChild(0);
							Label rsWrapper = Label.wrap(rs);
							rsWrapper.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
										Cookies.setCookie("client", id.getValue());
										Window.Location.replace(GWT.getHostPageBaseURL()
												+ "Accueil_applifit.html" + Constantes.SUFFIXE_URL);
									
								}
							});
						}

					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}
	
	public void supprimerEntreprise(String entreprise_id){
		String deleteUrl = Entreprise_URL + "supprimerEntreprise/" + entreprise_id;
        RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT, deleteUrl);
        builder.setHeader("Content-Type", "application/json");
        try {
            builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable exception) {
                }

                public void onResponseReceived(Request request,Response response) {
                	Window.Location.reload();
                }
            });
        } catch (RequestException e) {
            System.out.println("RequestException");
        }
	}

	public void modifierClient(String entreprise_id){
		final String rs = (modifierClient).getForm_RS().getValue().trim();
		final String siret = (modifierClient).getSiret().getValue().trim();
		final String adresse = (modifierClient.getAdresse()).getValue().trim();
		final String ville = (modifierClient.getVille()).getValue().trim();
		final String cp = (modifierClient.getCp()).getValue().trim();
		final String pays = (modifierClient.getPays()).getValue().trim();
		final String offre = (modifierClient.getOffre()).getValue().trim();
		final String dateString = (modifierClient.getDate()).getValue().trim();
		
		final String quota_formulaire = (modifierClient.getQuota_formulaire())
				.getValue().trim();
		final String quota_utilisateur = (modifierClient.getQuota_utilisateur())
				.getValue().trim();

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


		RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT, Entreprise_URL + "modifierEntreprise");
		builder.setHeader("Content-Type", "application/json");

		final JSONObject entrepriseAsJSONObject = BuildJsonObject.buildJSONObjectEntreprise(entreprise_id , rs, siret, adresse,ville, cp, pays, offre, dateString,"", "","", "","", quota_formulaire,quota_utilisateur);
		try {
			builder.sendRequest(
					entrepriseAsJSONObject.toString(),
					new RequestCallback() {
						public void onError(Request request,
								Throwable exception) {
							Window.alert("Erreur d'ajout");
						}

						public void onResponseReceived(
								Request request,
								Response response) {
							Window.alert("Modification avec succés");
							Window.Location.reload();
						}
					});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}

	}
	
	
	/*** Remplir la page de modification par les données de l'entreprise selectionnée */
	public void alimenterformModif(String entreprise_id){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Entreprise_URL+entreprise_id);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}

				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,
						Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						String entreprisesAsJSONString = response.getText();

						JSONValue entreprisesAsJSONValue = JSONParser.parse(entreprisesAsJSONString);
						JSONObject entreprisesAsJSONObject = entreprisesAsJSONValue.isObject();
						modifierClient.getForm_RS().setValue(entreprisesAsJSONObject.get("raison_sociale").isString().stringValue());
						modifierClient.getSiret().setValue(entreprisesAsJSONObject.get("siret").isString().stringValue());
						modifierClient.getAdresse().setValue(entreprisesAsJSONObject.get("adresse").isString().stringValue());
						modifierClient.getVille().setValue(entreprisesAsJSONObject.get("ville").isString().stringValue());
						modifierClient.getCp().setValue(entreprisesAsJSONObject.get("code_postal").isString().stringValue());
						modifierClient.getPays().setValue(entreprisesAsJSONObject.get("pays").isString().stringValue());
						modifierClient.getOffre().setValue(entreprisesAsJSONObject.get("offre_souscrite").isString().stringValue());
						modifierClient.getDate().setValue(entreprisesAsJSONObject.get("date").isString().stringValue());
						modifierClient.getQuota_formulaire().setValue(entreprisesAsJSONObject.get("quota_formulaire").isNumber().toString());
						modifierClient.getQuota_utilisateur().setValue(entreprisesAsJSONObject.get("quota_utilisateur").isNumber().toString());
					}
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}
	
	
	/*** Ajouter la liste des clients alimentée à la page*/
	private void addListePage() {
		Document.get().getBody().appendChild(liste.getElement());
		
		/*Anchor goWrapper = Anchor.wrap(liste.getGo());
		goWrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				NodeList<TableRowElement> rows = liste.getTbody().getRows();
				int i = 1;
				String client = "";
				while (i < rows.getLength() && client.isEmpty()) {
					final NodeList<TableCellElement> cells = rows.getItem(i)
							.getCells();
					TableCellElement c = cells.getItem(0);
					InputElement br = (InputElement) c.getChild(0)
							.getFirstChild();
					if (br.isChecked()) {
						client = br.getValue();
					}
					
					
					i++;
				}
				if (client.isEmpty()) {
					Window.alert("Veuillez choisir un client!");
				} else {
					Cookies.setCookie("client", client);
					Window.Location.replace(GWT.getHostPageBaseURL()
							+ "Accueil_applifit.html" + Constantes.SUFFIXE_URL);
				}
			}
		});*/

	}

}
