package com.applifit.bilifit.server.formulaires.RestServices;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;
import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.Formulaire;
import com.applifit.bilifit.server.formulaires.Parametre;
import com.applifit.bilifit.server.formulaires.DAO.ElementDaoImpl;
import com.applifit.bilifit.server.formulaires.DAO.FormulaireDaoImpl;
import com.applifit.bilifit.server.formulaires.DAO.ParametreDaoImpl;
import com.googlecode.objectify.Key;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.resource.Singleton;




@Singleton
@Path("/formulaire")
public class FormulaireRestService {
	
	 private final FormulaireDaoImpl fdao = new FormulaireDaoImpl(); 

	 
	 @Context
     UriInfo uriInfo;
	 
	 @Context
     Request request;
	 

	@GET
	 @Path("{id}")
	 @Produces("application/json") 
     public Formulaire getFormulaireById(@PathParam("id") Long id) {
		 return fdao.getFormulaireById(id);
     }
	 
	 @GET
	 @Path("/cookie")
	 @Produces("application/json") 
     public String getCookie(@HeaderParam("host") String host , @HeaderParam("User-Agent") String useragent , @HeaderParam("cookie") String cookie ) {
		 ServletContainer test = new ServletContainer();
		 return host+" "+useragent+" "+cookie;
     }
	 
	 @POST
	 @Path("/ajouterFormulaire")
	 @Consumes("application/json")
	 public Response createFormulaire(Formulaire formulaire){
		formulaire.setId(null);
        fdao.ajouterFormulaire(formulaire);
        return Response.created(URI.create("/"+formulaire.getId())).build();
	 }
	 
	 @POST
	 @Path("/ajouterFormulaireComplet")
	 @Consumes("application/json")
	 public Response createFormulaireComplet(Formulaire formulaire, List<Element> elements){
		formulaire.setId(null);
        fdao.ajouterFormulaire(formulaire , elements);
        return Response.created(URI.create("/"+formulaire.getId())).build();
	 }
	 
	 @GET
	 @Path("/recupererFormulaire/nom/{nom}")
	 @Produces("application/json") 
     public Formulaire getFormParNom(@PathParam("nom") String nom) {
		 return fdao.getFormParNom(nom);
     }
	 
	 @GET
	 @Path("/recupererFormulaire/nom/{nom}/version/{version}")
	 @Produces("application/json") 
     public Formulaire getFormParNomAndVersion(@PathParam("nom") String nom , @PathParam("version") int version) {
		 return fdao.getFormParNomAndVersion(nom , version);
     }
	 
	 @GET
	 @Path("/listerFormulaire")
	 @Produces("application/json") 
     public List<Formulaire> getComptes() {
		 return fdao.getFormulaires();
     }
	 
	 
	 /**
	  * Lister les formulaires d'une entreprise
	  * @param entreprise
	  * @return la liste des formulaires de l'entreprise
	  */
	 
	 @GET
	 @Path("/listerFormulaire/entreprise/{entreprise}")
	 @Produces("application/json") 
     public List<Formulaire> getFormsByEntreprise(@PathParam("entreprise") long entreprise) {
		 return fdao.getFormulairesByEntreprise(entreprise);
     }
	 
	 /**
	  * Récuperer les formulaires correspondants à une recherche par nom et date
	  * @param entreprise
	  * @param nom
	  * @param date
	  * @return la liste des formulaires trouvés
	  */
	 @GET
	 @Path("/listerFormulaire/entreprise/{entreprise}/nom/{nom}/date/{date}")
	 @Produces("application/json") 
     public List<Formulaire> getFormsByParam(@PathParam("entreprise") long entreprise, @PathParam("nom") String nom, @PathParam("date") String date) {
		 return fdao.getFormulairesByParam(entreprise, nom, date);
     }
	 
	 /**
	  * Vérifie si le nombre de formulaire depasse le quota
	  * @param idEntreprise
	  * @return boolean
	  */
	 @GET
	 @Path("/etatFormulaire/idEntreprise/{idEntreprise}")
	 @Produces("application/json") 
     public Boolean getCountFormulaireByEntreprise(@PathParam("idEntreprise") long idEntreprise) {
		 return fdao.getEtatFormulaire(idEntreprise);
     }
	 
	 
	 /**
	  * Lister les fomulaire alloués à un compte
	  * @param compte
	  * @return liste des formulaire alloués
	  */
	 @GET
	 @Path("/listerFormulaire/compte/{compte}")
	 @Produces("application/json") 
     public List<Formulaire> getFormsByCompte(@PathParam("compte") long compte) {
		 return fdao.getFormulairesByCompte(compte);
     }
	 /**
	  * recuperer information
	  * @param info
	  * @return info
	  */
		@GET
		@Path("/uploadimg/{str}")
		@Produces("application/json")
		public String getString(@PathParam("str") String str) {
			return str;
		}
	 
	 /**
	  * Lister les fomulaire alloués à un compte (Derniére version) 
	  * @param compte
	  * @return liste des formulaire alloués
	  */
	 @GET
	 @Path("/listerFormulaireLastVersion/compte/{compte}")
	 @Produces("application/json") 
     public List<Formulaire> getFormsVersionByCompte(@PathParam("compte") long compte) {
		 return fdao.getFormulairesVersionByCompte(compte);
     }
	 
	 
	 /**
	  * Retourne la derniere version active pour un formulaire
	  * @param formulaire
	  * @return entier indiquant la dernière version active d'un formulaire
	  */
	 @GET
	 @Path("/derniereVersion/formulaire/{formulaire}")
	 @Produces("application/json") 
     public int getLastVersion(@PathParam("formulaire") String formulaire) {
		 return fdao.getLastVersion(formulaire);
     }
	 
	 /**
	  * Désactive un formulaire
	  * @param id
	  * @return 
	  */
	 @PUT
	 @Path("/supprimer/{id}")
	 @Consumes("application/json")
	 public Response supprimer(@PathParam("id") Long id){
		 fdao.supprimer(id);
        return Response.ok().build();
	 }
	 
	 
	 
	 @PUT
	 @Path("allouerFormulaire/compte/{compte}/formulaire/{formulaire}")
	 @Consumes("application/json")
	 public Response allouer(@PathParam("compte") Long compte, @PathParam("formulaire") Long formulaire ){
		 fdao.allouerForm(compte, formulaire);
		 return Response.ok().build();
	 }
	 
	 
	 @GET
	 @Path("/listerCompteAllocation/formulaire/{formulaire}")
	 @Produces("application/json") 
     public List<Compte> getAllocationUsers(@PathParam("formulaire") Long formulaire) {
		 return fdao.getAllocationUsers(formulaire);
     }
	
	 
	 @GET
	 @Path("/listerFormulaire/compte/{compte}/nom/{nom}/date/{date}")
	 @Produces("application/json") 
     public List<Formulaire> getFormsCompteByParam(@PathParam("compte") long compte, @PathParam("nom") String nom, @PathParam("date") String date) {
		 return fdao.getFormulairesComptesByParam(compte, nom, date);
     }
	 
	 @POST
	 @Path("/addForm")
	 @Consumes("application/json")
	 public Response addForm(String form) throws JSONException{
		 JSONObject formulaireObject = new JSONObject(form);
		 Formulaire formulaire = new Formulaire(formulaireObject.getString("nom"),formulaireObject.getString("commentaire") ,  1 , formulaireObject.getString("date_creation"), new Key<Entreprise>(Entreprise.class, formulaireObject.getLong("entreprise")), null);
		 formulaire = fdao.ajouterFormulaire(formulaire);
		 int i =0;
		 JSONObject elementObject;
		 JSONObject paramObject;
		 ElementDaoImpl edao = new ElementDaoImpl(); 
		 ParametreDaoImpl pdao = new ParametreDaoImpl(); 
		 while(formulaireObject.has("element"+i)){
			 elementObject = formulaireObject.getJSONObject("element"+i);
			 Element e = new Element(elementObject.getString("type"), elementObject.getInt("position_x") , elementObject.getInt("position_y") , new Key<Formulaire>(Formulaire.class, formulaire.getId()));
			 e = edao.ajouterElement(e);
			 int k =0;
			 while(formulaireObject.has("parametre"+i+k)){
				 paramObject = formulaireObject.getJSONObject("parametre"+i+k);
				 Parametre p = new Parametre(paramObject.getString("nom"), paramObject.getString("valeur"), new Key<Element>(Element.class, e.getId()));
				 p= pdao.ajouterParametre(p);
				 k++;
			 }
			 
			 i++;
		 }
		 
		 i=0;
		 while(formulaireObject.has("user"+i)){
			 fdao.allouerForm(formulaireObject.getLong("user"+i), formulaire.getId());
			 i++;
		 }

		 return Response.ok().build();
	 }
	 
	 @PUT
	 @Path("/updateForm")
	 @Consumes("application/json")
	 public Response apdateForm(String form) throws JSONException{
		 JSONObject formulaireObject = new JSONObject(form);
		 Formulaire formulaire = new Formulaire(formulaireObject.getString("nom"),formulaireObject.getString("commentaire") , formulaireObject.getInt("version"), formulaireObject.getString("date_creation"), new Key<Entreprise>(Entreprise.class, formulaireObject.getLong("entreprise")), null);
		
		 formulaire = fdao.ajouterFormulaire(formulaire);
		 int i =0;
		 JSONObject elementObject;
		 JSONObject paramObject;
		 ElementDaoImpl edao = new ElementDaoImpl(); 
		 ParametreDaoImpl pdao = new ParametreDaoImpl(); 
		 while(formulaireObject.has("element"+i)){
			 elementObject = formulaireObject.getJSONObject("element"+i);
			 Element e = new Element(elementObject.getString("type"), elementObject.getInt("position_x") , elementObject.getInt("position_y") , new Key<Formulaire>(Formulaire.class, formulaire.getId()));
			 e = edao.ajouterElement(e);
			 int k =0;
			 while(formulaireObject.has("parametre"+i+k)){
				 paramObject = formulaireObject.getJSONObject("parametre"+i+k);
				 Parametre p = new Parametre(paramObject.getString("nom"), paramObject.getString("valeur"), new Key<Element>(Element.class, e.getId()));
				 p= pdao.ajouterParametre(p);
				 k++;
			 }
			 
			 i++;
		 }
		 
		 i=0;
		 while(formulaireObject.has("user"+i)){
			 fdao.allouerForm(formulaireObject.getLong("user"+i), formulaire.getId());
			 i++;
		 }

		 return Response.ok().build();
	 }

 @PUT
 @Path("/changeAllocation/formulaire/{id}")
 @Consumes("application/json")
 public Response changeAllocation(@PathParam("id") Long id, String comptes) throws JSONException{
	 
	 String[] c = comptes.split("-");
	 fdao.initialiserAllocation(id);
	 for(int i =0; i<c.length; i++){
		 System.out.println("rrr"+c[i]);
		 fdao.allouerForm(Long.parseLong(c[i]), id);
	 }
	 
	 return Response.ok().build();
 }

}
