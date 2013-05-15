package com.applifit.bilifit.client.bdd;

import java.util.ArrayList;
import java.util.List;


import com.applifit.bilifit.client.export.GEIESampleService;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.bdd.Bdd;
import com.applifit.bilifit.client.templatesUibinder.bdd.HeaderBdd;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;


import dk.lindhardt.gwt.geie.client.HTMLTable2TableLayout;
import dk.lindhardt.gwt.geie.shared.TableLayout;



public class BddApplifit implements EntryPoint {

	private static final String FORMULAIRE_URL = GWT.getHostPageBaseURL() + "formulaire/";
	private static final String ELEMENT_URL = GWT.getHostPageBaseURL() + "element/";
	private static final String PARAM_URL = GWT.getHostPageBaseURL() + "parametre/";
	private static final String VALUE_URL = GWT.getHostPageBaseURL() + "element_valeur/";
	
	Bdd bddCorps;
	String elementsAsString = "";
	
	List<Integer> indices = new ArrayList<Integer>();
	List<Long> formulaires = new ArrayList<Long>();
	List<FlexTable> tables = new ArrayList<FlexTable>();
	
	@Override
	public void onModuleLoad() {

		// Seul les utilisateurs connectés peuvent accéder à cette page
		
		if(Cookies.getCookie("profil") == null){
			Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html" + Constantes.SUFFIXE_URL);
		}
		else if(Cookies.getCookie("client") == null)
			Window.Location.replace(GWT.getHostPageBaseURL()+ "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
		else if(!Cookies.getCookie("profil").equals("SA") && !Cookies.getCookie("profil").equals("CA"))
			Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html" + Constantes.SUFFIXE_URL);
		else{
				
		/*** La partie menu ***/
		HeaderBdd menu = new HeaderBdd(Constantes.USER);
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
		
		/*** Le corps de la page***/
		bddCorps = new Bdd();
		Document.get().getBody().appendChild(bddCorps.getElement());
		
		formulaires = new ArrayList<Long>();
	    //alimenter la liste des formulaires par le resultat de la recherche
		alimenterListeBdd(FORMULAIRE_URL+"listerFormulaire/entreprise/"+Cookies.getCookie("client") , bddCorps);
		
		//Ajouter action onchange de la liste des formulaires , affichant le formulaire selectionné
		final ListBox lb =ListBox.wrap(bddCorps.getSelectForm());
		
		lb.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				tables = new ArrayList<FlexTable>();
				
				for(int i=bddCorps.getDivtest().getChildCount() ; i>0; i--){
					bddCorps.getDivtest().getChild(0).removeFromParent();
				}
				//Si la ligne selectionnée n'est pas le prompt de la liste
				if (lb.getSelectedIndex() != 0){
					if(lb.getSelectedIndex() == 1){
						for(int i=0; i<formulaires.size(); i++){
							FlexTable table = new FlexTable();
							table.setStyleName("tableMargin");
							alimenterTable(table , formulaires.get(i), lb.getItemText(i+2) , true );
							tables.add(table);
						}
					}else{
					//Récuperer la valeur selectionnée
					
					FlexTable table = new FlexTable();
					//Récuperer les elements et parametres du formulaire et les afficher dans la table
					alimenterTable(table , Long.parseLong(lb.getValue(lb.getSelectedIndex())), lb.getItemText(lb.getSelectedIndex()) , false);
					tables.add(table);
					}
				}
			}
		});
		
		/*** action d'export ***/
		Anchor export = Anchor.wrap(bddCorps.getExport());
		export.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<TableLayout> layouts = new ArrayList<TableLayout>();
				List<String> titles = new ArrayList<String>();
				String nom="";
				for(int i =0; i<tables.size(); i++){
					if(tables.size() > 1){
						nom ="Bdd_Generale";
						SpanElement span = bddCorps.getDivtest().getChild(2*i).cast();
						InputElement check =(InputElement) span.getChild(0);
						LabelElement label = (LabelElement) span.getChild(1);
						if(check.isChecked()){
							layouts.add(getTableLayout(tables.get(i))) ;
							titles.add(label.getInnerHTML());
						}
							
					}else{
						layouts.add(getTableLayout(tables.get(i))) ;
						titles.add(lb.getItemText(lb.getSelectedIndex()));
						nom = lb.getItemText(lb.getSelectedIndex()); 
					}
					
				}
				if(layouts.size()>0){
					final String name = nom;
					GEIESampleService.App.getInstance().prepareExcelDownload(
	                		layouts, titles ,new AsyncCallback<String>() {
	                         public void onFailure(Throwable throwable) {
	                            Window.alert("Error while preparing excel sheet\n" + throwable.getMessage());
	                         }
	
	                         public void onSuccess(String key) {
	                            Window.open("/bdd/Export?type=xls&key=" + key +"&name="+ name, "_self", "");
	                         }
	                      });
				}
			}
		});
		}
	}
	
	//Alimenter la liste aprés selection du formulaire
	public void alimenterTable(final FlexTable table , Long formulaire_id , final String formulaire_nom , final boolean totale){
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, ELEMENT_URL+"listerElement/formulaire/"+formulaire_id);
		builder.setHeader("Content-Type", "application/json");
		
		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {

				}
				@SuppressWarnings("deprecation")
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
						String elementsAsJSONString = response.getText();
						JSONValue elementsAsJSONValue = JSONParser.parse(elementsAsJSONString);
						JSONArray elementsAsJSONArray = elementsAsJSONValue.isArray();
						
						//Dans le cas où on a choisit d'afficher la liste totale
						if(totale){
							CheckBox check = new CheckBox();
							check.setStyleName("checkBdd");
							check.setText(formulaire_nom);
							bddCorps.getDivtest().appendChild(check.getElement());
						}
						
						table.setWidget(0, 0, new Label("Utilisateur"));
						DOM.setStyleAttribute(table.getCellFormatter().getElement(0, 0), "backgroundColor", "#6a6b6f");
						DOM.setStyleAttribute(table.getCellFormatter().getElement(0, 0), "color", "#FFF");
						table.getColumnFormatter().setWidth(0, "150px");
						table.getWidget(0, 0).setStyleName("table_ligne_haute");
						table.setWidget(0, 1, new Label("Date"));
						DOM.setStyleAttribute(table.getCellFormatter().getElement(0, 1), "backgroundColor", "#6a6b6f");
						DOM.setStyleAttribute(table.getCellFormatter().getElement(0, 1), "color", "#FFF");
						table.getColumnFormatter().setWidth(1, "150px");
						table.getWidget(0, 1).setStyleName("table_ligne_haute");
						bddCorps.getDivtest().appendChild(table.getElement());
						elementsAsString = "";
						for (int i = 0; i < elementsAsJSONArray.size(); i++) {
							JSONValue elementAsJSONValue = elementsAsJSONArray.get(i);
							JSONObject elementAsJSONObject = elementAsJSONValue.isObject();
							elementsAsString +=elementAsJSONObject.get("id").isNumber().toString()+"-";
							RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, PARAM_URL+"listerParametre/element/"+elementAsJSONObject.get("id").isNumber().toString());
							builder.setHeader("Content-Type", "application/json");
							final int j =i+2;
							try {
								builder.sendRequest(null, new RequestCallback() {
									public void onError(Request request, Throwable exception) {

									}
									public void onResponseReceived(Request request, Response response) {
										if (response.getStatusCode() == Response.SC_OK) {
											JSONValue paramsAsJSONValue = JSONParser.parse(response.getText());
											JSONArray paramsAsJSONArray = paramsAsJSONValue.isArray();
											JSONObject paramAsJSONObject;
											if(paramsAsJSONArray.size() > 1){
												if(paramsAsJSONArray.get(0).isObject().get("nom").isString().stringValue().equals("titre"))
													paramAsJSONObject = paramsAsJSONArray.get(0).isObject();
												else paramAsJSONObject = paramsAsJSONArray.get(1).isObject();
											}else paramAsJSONObject = paramsAsJSONArray.get(0).isObject();
											table.setWidget(0, j, new Label(paramAsJSONObject.get("valeur").isString().stringValue()));
											table.getWidget(0, j).setStyleName("table_ligne_haute");
											DOM.setStyleAttribute(table.getCellFormatter().getElement(0, j), "backgroundColor", "#6a6b6f");
											DOM.setStyleAttribute(table.getCellFormatter().getElement(0, j), "color", "#FFF");
											table.getColumnFormatter().setWidth(j, "150px");
						          		    
										}
										
									}
								});
							} catch (RequestException e) {
								System.out.println("RequestException");
							}
		          		    
		                   }
						/*** Dans l'étape précedente on remplis la première partie du tableau (Utilisteur + date) ***/
						remplirTableau(table , elementsAsString);
						
					}
					
				}
			});
		} catch (RequestException e) {
			System.out.println("RequestException");
		}
	}
	
	/*** Remplire le rest de la table  (Les tritres de champs) ***/
	public void remplirTableau(final FlexTable table , final String elementsAsString){
		//Récuperer la liste des comptes pour lesquels, nous avons allouer cette version du formulaire
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, VALUE_URL + "recupererValues/elements/" + elementsAsString);
		builder.setHeader("Content-Type", "application/json");
		try {

			builder.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert("erreur de récupération du formulaire");
				}

				@SuppressWarnings({ "rawtypes", "unchecked" })
				public void onResponseReceived(Request request,Response response) {
					if (response.getStatusCode() == Response.SC_OK) {
					String comptes[] =  response.getText().split(",");
					String class_cell="";
					String color="";
					int j =0;
						indices = new ArrayList();
                    for (int i = 0; i < comptes.length-1 ; i++) {
                    	indices.add(Integer.parseInt(comptes[i+1]));
                    	if(j%2 == 0){
    						class_cell = "table_ligne_clair";
    						color="#f7f8de";
                    	}
    					else{ class_cell ="table_ligne_fonce";
    						color="#eceeaf";
    					}
                    	//color="#eceeaf";
                    	i=i+2;
          		      	table.setWidget(j+1, 0, new Label(comptes[i]+" "+comptes[i+1]));
          		        table.getWidget(j+1, 0).setStyleName(class_cell);
          		        DOM.setStyleAttribute(table.getCellFormatter().getElement(j+1, 0), "backgroundColor", color);
						DOM.setStyleAttribute(table.getCellFormatter().getElement(j+1, 0), "color", "#000");
						table.getColumnFormatter().setWidth(1, "150px");
          		      	i= i+2;
          		        table.setWidget(j+1, 1, new Label(comptes[i]));
        		      	table.getWidget(j+1, 1).setStyleName(class_cell);
        		      	DOM.setStyleAttribute(table.getCellFormatter().getElement(j+1, 1), "backgroundColor", color);
						DOM.setStyleAttribute(table.getCellFormatter().getElement(j+1, 1), "color", "#000");
        				j++;
                    }
                    remplirChampsForm(table , elementsAsString);
                    
				}
				}
			});
		} catch (RequestException e) {
			Window.alert("RequestException");
		}
		
	}
	
	/*** Remplire le rest de la table  (Les valeurs des champs) ***/
	public void remplirChampsForm(final FlexTable table, String elementsAsString){
		String elements[] = elementsAsString.split("-");
		for(int i =0; i<indices.size(); i++){
			final int ligne = i+1;
			for(int j = 0; j <elements.length; j++){
				final int colonne = j+2;
				table.setWidget(ligne , colonne, new Label(""));
				if(ligne%2 != 0){
					table.getWidget(ligne, colonne).setStyleName("table_ligne_clair");
					DOM.setStyleAttribute(table.getCellFormatter().getElement(ligne, colonne), "backgroundColor", "#f7f8de");
					
				}
				else {table.getWidget(ligne, colonne).setStyleName("table_ligne_fonce");
					DOM.setStyleAttribute(table.getCellFormatter().getElement(ligne, colonne), "backgroundColor", "#eceeaf");
				
				}
				DOM.setStyleAttribute(table.getCellFormatter().getElement(ligne, colonne), "color", "#000");
				
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, VALUE_URL + "getValue/element/"+elements[j]+"/indice/"+indices.get(i));
				builder.setHeader("Content-Type", "application/json");
				try {

					builder.sendRequest(null, new RequestCallback() {

						public void onError(Request request, Throwable exception) {
							Window.alert("erreur de récupération du formulaire");
						}

						@SuppressWarnings("deprecation")
						public void onResponseReceived(Request request,Response response) {
							if (response.getStatusCode() == Response.SC_OK) {
								JSONValue valuesAsJSONValue = JSONParser.parse(response.getText());
								JSONObject valuesAsJSONObject = valuesAsJSONValue.isObject();
								table.setWidget(ligne , colonne, new Label(valuesAsJSONObject.get("valeur").isString().stringValue().replaceAll("\\*_\\*", " , ")));
								if(ligne%2 != 0){
									table.getWidget(ligne, colonne).setStyleName("table_ligne_clair");
									DOM.setStyleAttribute(table.getCellFormatter().getElement(ligne, colonne), "backgroundColor", "#f7f8de");
									
								}
								else {table.getWidget(ligne, colonne).setStyleName("table_ligne_fonce");
									DOM.setStyleAttribute(table.getCellFormatter().getElement(ligne, colonne), "backgroundColor", "#eceeaf");
								
								}
							}
						}
					});
					
					
				} catch (RequestException e) {
					Window.alert("RequestException");
				}
			}
			
		}
	}
	
	/*** Fonction qui transforme une table FlexTable à une table se type TableLayout pour l'exporter en formta excel ***/
	private TableLayout getTableLayout(FlexTable table) {
	      HTMLTable2TableLayout parser = new HTMLTable2TableLayout(table);
	      return parser.build();
	   }
	
	
	// Alimenter la lise des formulaires par le resultat de la recherche
		public void alimenterListeBdd(String url, Bdd recherche) {

			final ListBox lb = ListBox.wrap(recherche.getSelectForm());
			// Vider la liste
			lb.clear();
			lb.addItem("Choisir un formulaire...", "");
			lb.addItem("Base de données gobale", "");
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
							String formulairesAsJSONString = response.getText();

							JSONValue formulairesAsJSONValue = JSONParser
									.parse(formulairesAsJSONString);
							JSONArray formulairesAsJSONArray = formulairesAsJSONValue
									.isArray();
							
							// Alimenter la liste
							for (int i = 0; i < formulairesAsJSONArray.size(); i++) {
								JSONValue formulaireAsJSONValue = formulairesAsJSONArray
										.get(i);
								JSONObject formulaireAsJSONObject = formulaireAsJSONValue
										.isObject();
								formulaires.add(Long.parseLong(formulaireAsJSONObject.get("id").isNumber()
												.toString()));
								lb.addItem(
										formulaireAsJSONObject.get("nom")
												.isString().stringValue()
												+ " V"
												+ formulaireAsJSONObject
														.get("version").isNumber()
														.toString(),
										formulaireAsJSONObject.get("id").isNumber()
												.toString());
							}
						}
					}
				});
			} catch (RequestException e) {
				System.out.println("RequestException");
			}
		}
	
	

}
