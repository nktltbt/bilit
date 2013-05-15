/**
 * 
 */
package com.applifit.bilifit.client.comptes;



import com.applifit.bilifit.client.outils.BuildJsonObject;
import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.comptes.AjoutSa;
import com.applifit.bilifit.client.templatesUibinder.comptes.ContainerSA;
import com.applifit.bilifit.client.templatesUibinder.comptes.Header;
import com.applifit.bilifit.client.templatesUibinder.comptes.ModifierSa;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;



/**
 * @author Meryem
 *
 */
public class GestionSuperAdmin implements EntryPoint {

	private ContainerSA container = new ContainerSA();
	final AjoutSa ajoutSa = new AjoutSa();
	private ModifierSa modifierSa = new ModifierSa();
	private static final String COMPTE_URL = GWT.getHostPageBaseURL()+ "compte/";
	

	
	
	@Override
	public void onModuleLoad() {
		// Seul le profil SA peut se connecter à cette page
		
		if(Cookies.getCookie("profil") == null || !Cookies.getCookie("profil").equals("SA"))
			Window.Location.replace(GWT.getHostPageBaseURL());
		else {
			
		//Afficher le menu 
		
		Header menu= new Header(Constantes.USER);
		Document.get().getBody().appendChild(menu.getElement());
		
		Anchor accueil = Anchor.wrap(menu.getAccueil());
		accueil.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()+"Accueil_superadmin.html"+Constantes.SUFFIXE_URL);
			}
		  }
		);
		
		Anchor deconnexion = Anchor.wrap(menu.getDeconnexion());
		deconnexion.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Utils.deconnexion();
			}
		  }
		);
		
		//Afficher le corps de la page
		loadCompte();
		}
	}
	
	
	//Vider le corps de la page
	public void initialiserPage(UIObject container){
	
		Document.get().getBody().removeChild(container.getElement());
	}
	
	
	//Afficher la page d'ajout
	public void pageAjout(){
		
		Document.get().getBody().appendChild(ajoutSa.getElement());
		Button buttonWrapper = Button.wrap(ajoutSa.getButton());
		
		buttonWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ajouterSuperAdmin();
			}
		});
		
		buttonWrapper = Button.wrap(ajoutSa.getRetour());
		
		buttonWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.reload();
			}
		});

	}
	
	//Afficher la page de modification
	public void pageModification(final String email){
		
		Document.get().getBody().appendChild(modifierSa.getElement());
		
		//Afficher les champs password pour le compte correspondant
		if(!Cookies.getCookie("mail").equals(email)){
			modifierSa.getDivPass().removeFromParent();
		}
		
		getComptebyEmail(email);
		
		
		Button buttonWrapper = Button.wrap(modifierSa.getButton());
		
		buttonWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				modifierSuperAdmin(email);
			}
		});
		
		Button retourWrapper = Button.wrap(modifierSa.getRetour());
		
		retourWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.reload();
			}
		});
		

	}
	
	
	//Afficher la liste des super admin
	public void pageListeSa(){
		
		Document.get().getBody().appendChild(container.getElement());
		
		NodeList<TableRowElement> rows = container.getTbodySa().getRows();
		
		for(int i=1 ; i<rows.getLength(); i++)
		{
			final NodeList<TableCellElement> cells =rows.getItem(i).getCells();
			TableCellElement c = cells.getItem(cells.getLength() - 1);
			
			ImageElement b = (ImageElement)c.getChild(0);
			Image buttonWrapper = Image.wrap(b);
			
			buttonWrapper.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					initialiserPage(container);
					pageModification(cells.getItem(2).getInnerHTML());
				}
			});
			
			ImageElement supprimer = (ImageElement)c.getChild(1);
			buttonWrapper = Image.wrap(supprimer);
			
			buttonWrapper.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Boolean response = Window.confirm("Voulez-vous vraiment supprimer ce contact? Cette action est irreversible");
					if(response){
						SupprimerSuperAdmin(cells.getItem(2).getInnerHTML());
					}

				}

				
			});
			
		}
		
		Anchor anchorWrapper = Anchor.wrap(container.getLinkajout());
		anchorWrapper.addClickHandler(new ClickHandler() {
		     @Override
		     public void onClick(ClickEvent event) {
		    	 initialiserPage(container);
		    	 pageAjout();
		     }

		});
		
		Button buttonWrapper = Button.wrap(container.getButtonRechercher());
		
		buttonWrapper.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rechercherSuperAdmin();
			}
		});
	}
	
	//Fonction de suppresion d'un compte SA
	private void SupprimerSuperAdmin(String email) {
		// TODO Auto-generated method stub
		delete(email);
	}
	
	
	//Modification d'un compte SA
	private void modifierSuperAdmin(String email) {
				
		final String nom = (modifierSa.getNom()).getValue().trim();
		final String prenom = (modifierSa.getPrenom()).getValue().trim();
		final String mail = (modifierSa.getMail()).getValue().trim();
		final String fonction = (modifierSa.getFonction()).getValue().trim();
		String password = "";
		String password_confirmation ="";
		if(Cookies.getCookie("mail").equals(email)){
			password = modifierSa.getPassword().getValue().trim();
			password_confirmation = modifierSa.getPassword_confirmation().getValue().trim();
		}
		
			
		//Tous les champs sont obligatoires
		
		if (nom.isEmpty()) {
			Window.alert("Le champs Nom est obligatoire");
			return;
		}
		if (prenom.isEmpty()) {
			Window.alert("Le champs Prenom est obligatoire");
			return;
		}
		if (fonction.isEmpty()) {
			Window.alert("Le champs Fonction est obligatoire");
			return;
		}
		if (! password.isEmpty() && !password.equals(password_confirmation)) {
			Window.alert("Les deux mots de passe saisies ne sont pas egaux !!");
			return;
		}
	

		RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT,COMPTE_URL+"modifierCompte");
        builder.setHeader("Content-Type", "application/json");

        final JSONObject compteAsJSONObject = BuildJsonObject.buildJSONObjectCompte("-1",nom, prenom,password, fonction, mail, "SA",null);
        try {
            builder.sendRequest(compteAsJSONObject.toString(),
                    new RequestCallback() {
                        public void onError(Request request, Throwable exception) {
                            Window.alert("Erreur d'ajout");
                        }

                        public void onResponseReceived(Request request,
                                Response response) {
                        			initialiserPage(modifierSa);
                        			container = new ContainerSA();
                        			loadCompte();
                        }
                    });
        } catch (RequestException e) {
            System.out.println("RequestException");
        }
		
	}
	
	
	//Recherche multicritere d'un compte Sa
	private void rechercherSuperAdmin(){
		String nom;
		String prenom;
		String mail;
		String fonction;
		
		if((container.getNom()).getValue().trim().isEmpty())
			nom = "-";
		else nom = (container.getNom()).getValue().trim();
		if((container.getPrenom()).getValue().trim().isEmpty())
			prenom = "-";
		else prenom = (container.getPrenom()).getValue().trim();
		if((container.getMail()).getValue().trim().isEmpty())
			mail = "-";
		else mail = (container.getMail()).getValue().trim();
		if((container.getFonction()).getValue().trim().isEmpty())
			fonction = "-";
		else fonction = (container.getFonction()).getValue().trim();
		
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,COMPTE_URL+"rechercherCompte/email/"+mail+"/nom/"+nom+"/prenom/"+prenom+"/fonction/"+fonction);
        builder.setHeader("Content-Type", "application/json");
        try {
            builder.sendRequest(null, new RequestCallback() {
                public void onError(Request request, Throwable exception) {

                }

                @SuppressWarnings("deprecation")
				public void onResponseReceived(Request request,Response response) {
                		initialiserPage(container);
                		container = new ContainerSA();
                    if (response.getStatusCode() == Response.SC_OK) {
                        String comptesAsJSONString = response.getText();
                        JSONValue comptesAsJSONValue = JSONParser
                                .parse(comptesAsJSONString);
                        JSONArray comptesAsJSONArray = comptesAsJSONValue
                                .isArray();
                        String class_cell="";
                        for (int i = 0; i < comptesAsJSONArray.size(); i++) {
                        	
                            JSONValue contactAsJSONValue = comptesAsJSONArray
                                    .get(i);
                            final JSONObject contactAsJSONObject = contactAsJSONValue
                                    .isObject();
                            
                            if(i%2 != 1)
                            	class_cell= "table_ligne_clair";
                            else class_cell= "table_ligne_fonce";
                            TableRowElement row = container.getTbodySa().insertRow(-1);
              		      	TableCellElement cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("nom").isString().stringValue());
              		      	cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("prenom").isString().stringValue());
              		      	cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("mail").isString().stringValue());
              		      	cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	
              		      	cell.setInnerHTML(contactAsJSONObject.get("fonction").isString().stringValue());
              		      	Image edit = new Image();
              		      	edit.setUrl("images/Edit.png");
              		      	Image delete = new Image();
							delete.setUrl("images/delete.png");
            		      	cell = row.insertCell(-1);
	          		      	cell.addClassName(class_cell);
	          		      	cell.addClassName("img_edit");
	          		      	cell.appendChild(edit.getElement());
	          		      	cell.appendChild(delete.getElement());
	          		      	
                        }
                        	pageListeSa();
                        }
                }
            });
        } catch (RequestException e) {
            System.out.println("RequestException");
        }
	
		 
		 
	}
	
	
	//Ajouter un compte super Admin
	private void ajouterSuperAdmin() {
		
		final String nom = (ajoutSa.getNom()).getValue().trim();
		final String prenom = (ajoutSa.getPrenom()).getValue().trim();
		final String mail = (ajoutSa.getMail()).getValue().trim();
		final String fonction = (ajoutSa.getFonction()).getValue().trim();
		
		//Tous les champs sont obligatoires
		
		if (nom.isEmpty()) {
			Window.alert("Le champs Nom est obligatoire");
			return;
		}
		if (prenom.isEmpty()) {
			Window.alert("Le champs Prenom est obligatoire");
			return;
		}
		if (mail.isEmpty()) {
			Window.alert("Le champs Mail est obligatoire");
			return;
		}
		
		if(!mail.matches("^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+$")){
			Window.alert("'" + mail + "': Adresse Email invalide");
			return;
		}

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,COMPTE_URL+"recupererCompte/email/"+mail);
		builder.setHeader("Content-Type", "application/json");
		 try {
	
	            builder.sendRequest(null, new RequestCallback() {
	            	
	                public void onError(Request request, Throwable exception) {
	                	Window.alert("erreur de récupération de mail");
	                }

	                public void onResponseReceived(Request request, Response response) {
	                        String compteAsJSONString = response.getText();
	                        if(!compteAsJSONString.isEmpty()){
	                        	Window.alert(mail + ": Cette adresse email correspond à un utilisateur actif");
	                        	return;
	                        }else{
	                        	if (fonction.isEmpty()) {
	                    			Window.alert("Le champs fonction est obligatoire");
	                    			return;
	                    		}
	                        	RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,COMPTE_URL+"ajouterCompte");
	                            builder.setHeader("Content-Type", "application/json");

	                            final JSONObject compteAsJSONObject = BuildJsonObject.buildJSONObjectCompte("-1",nom, prenom, null, fonction, mail, "SA", null);
	                            try {
	                                	builder.sendRequest(compteAsJSONObject.toString(),
	                                        new RequestCallback() {
	                                            public void onError(Request request, Throwable exception) {
	                                                Window.alert("Erreur d'ajout");
	                                            }

	                                            public void onResponseReceived(Request request,
	                                                    Response response) {
	                                            			Window.Location.reload();
	                                            	//Window.Location.reload();
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
	
	
	//Charger la liste des comptes super Administrateur
	private void loadCompte(){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,COMPTE_URL+"listerCompteSa");
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
                        String class_cell="";
                        for (int i = 0; i < comptesAsJSONArray.size(); i++) {
                        	
                            JSONValue contactAsJSONValue = comptesAsJSONArray
                                    .get(i);
                            final JSONObject contactAsJSONObject = contactAsJSONValue
                                    .isObject();
                            
                            if(i%2 != 1)
                            	class_cell= "table_ligne_clair";
                            else class_cell= "table_ligne_fonce";
                            TableRowElement row = container.getTbodySa().insertRow(-1);
              		      	TableCellElement cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("nom").isString().stringValue());
              		      	cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("prenom").isString().stringValue());
              		      	cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("mail").isString().stringValue());
              		      	cell = row.insertCell(-1);
              		      	cell.addClassName(class_cell);
              		      	cell.setInnerHTML(contactAsJSONObject.get("fonction").isString().stringValue());
              		      	Image edit = new Image();
            		      	edit.setUrl("images/Edit.png");
            		      	Image delete = new Image();
							delete.setUrl("images/delete.png");
          		      		cell = row.insertCell(-1);
	          		      	cell.addClassName(class_cell);
	          		      	cell.addClassName("img_edit");
	          		      	cell.appendChild(edit.getElement());
	          		      	cell.appendChild(delete.getElement());
	          		      	
	          		      
              		      	}
                        pageListeSa();
                        }
                }
            });
        } catch (RequestException e) {
            System.out.println("RequestException");
        }
	}
        
	
	   //Recuperer les chmaps d'un compte par son email pour les afficher dans le formulaire de modification
        private void getComptebyEmail(String mail){
    		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,COMPTE_URL+"recupererCompte/email/"+mail);
            builder.setHeader("Content-Type", "application/json");
            try {
                builder.sendRequest(null, new RequestCallback() {
                    public void onError(Request request, Throwable exception) {

                    }

                    @SuppressWarnings("deprecation")
					public void onResponseReceived(Request request, Response response) {
                        if (response.getStatusCode() == Response.SC_OK) {
                            String compteAsJSONString = response.getText();
                            JSONValue compteAsJSONValue = JSONParser.parse(compteAsJSONString);
                            final JSONObject compteAsJSONObject = compteAsJSONValue.isObject();
                            modifierSa.getNom().setValue(compteAsJSONObject.get("nom").isString().stringValue());
                            modifierSa.getPrenom().setValue(compteAsJSONObject.get("prenom").isString().stringValue());
                            modifierSa.getFonction().setValue(compteAsJSONObject.get("fonction").isString().stringValue());
                            modifierSa.getMail().setValue(compteAsJSONObject.get("mail").isString().stringValue());
              		      	
                            }
                    }
                });
            } catch (RequestException e) {
                System.out.println("RequestException");
            }
		
	}
	
	
	//Supprimer un compte dont l'email ets la variable email
    private void delete(String email) {
            String deleteUrl = COMPTE_URL + "supprimerCompte/" + email;
            RequestBuilder builder = new RequestBuilder(RequestBuilder.PUT, deleteUrl);
            builder.setHeader("Content-Type", "application/json");
            try {
                builder.sendRequest(null, new RequestCallback() {
                    public void onError(Request request, Throwable exception) {
                    }

                    public void onResponseReceived(Request request,Response response) {
                    	initialiserPage(container);
                    	container = new ContainerSA();
                    	loadCompte();
                    }
                });
            } catch (RequestException e) {
                System.out.println("RequestException");
            }
        }

}
