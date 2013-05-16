package com.applifit.bilifit.server.formulaires.RestServices;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import com.applifit.bilifit.server.formulaires.Element_valeur;
import com.applifit.bilifit.server.formulaires.DAO.Element_valeurDaoImpl;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/element_valeur")
public class Element_valeurRestService {
	
	 private final Element_valeurDaoImpl evdao = new Element_valeurDaoImpl();
	
	 @Context
     UriInfo uriInfo;
	 
	 @GET
	 @Path("/ajouterValeur/valeur/{valeur}/element/{element}/compte/{compte}/date/{date}/indice/{indice}")
	 @Consumes("application/json")
	 public Response createElement(@PathParam("valeur") String valeur ,  @PathParam("element")  long element , @PathParam("compte") long compte , @PathParam("date") String date, @PathParam("indice") int indice ){
		/*String d[] = date.split(" ");
		String j[] = d[0].split("-");
		String h[] = d[1].split(":");
		date = j[0]+"-";
		if(Integer.parseInt(j[1]) < 10){
			date = date+"0"+j[1]+"-";
		}else date = date+j[1]+"-";
		
		if(Integer.parseInt(j[2]) < 10){
			date = date+"0"+j[2]+" ";
		}else date = date+j[2]+" ";
		
		if(Integer.parseInt(h[0]) < 10){
			date = date+"0"+h[0]+":";
		}else date = date+h[0]+":";
		
		if(Integer.parseInt(h[1]) < 10){
			date = date+"0"+h[1];
		}else date = date+h[1];
		*/
		 Element_valeur el = evdao.ajouterValeur(valeur ,element, compte , date, indice);
        return Response.created(URI.create("/"+el.getId())).build();
	 }
	 
	 
	 @SuppressWarnings("rawtypes")
	@GET
	 @Path("/recupererValues/elements/{element}")
	 @Produces("application/json")
	 public String getValues(@PathParam("element") String element ){
		 String el[] = element.split("-");
		 ArrayList<Long> e = new ArrayList<Long>();
		 for(int i = 0; i<el.length; i++){
			 e.add(Long.parseLong(el[i]));
		 }
		 ArrayList ci = evdao.getDistinctCompteIndice(e);
		 String retour ="";
		 for(int i=0 ; i<ci.size(); i++){
			 for(int j =0; j<((ArrayList)ci.get(i)).size(); j++){
				 retour+=((ArrayList)ci.get(i)).get(j)+",";
			 }
		 }
        return retour;
	 }
	 
	 @SuppressWarnings("rawtypes")
	@POST
	 @Path("/recupererValues")
	 @Produces("application/json")
	 public String getValuesPost(String element ){
		 String el[] = element.split("-");
		 ArrayList<Long> e = new ArrayList<Long>();
		 for(int i = 0; i<el.length; i++){
			 e.add(Long.parseLong(el[i]));
		 }
		 ArrayList ci = evdao.getDistinctCompteIndice(e);
		 String retour ="";
		 for(int i=0 ; i<ci.size(); i++){
			 for(int j =0; j<((ArrayList)ci.get(i)).size(); j++){
				 retour+=((ArrayList)ci.get(i)).get(j)+",";
			 }
		 }
        return retour;
	 }
	 
	 @SuppressWarnings("rawtypes")
	@GET
	 @Path("/recupererValues/elements/{element}/compte/{compte}")
	 @Produces("application/json")
	 public String getValues(@PathParam("element") String element , @PathParam("compte") long compte){
		 String el[] = element.split("-");
		 ArrayList<Long> e = new ArrayList<Long>();
		 for(int i = 0; i<el.length; i++){
			 e.add(Long.parseLong(el[i]));
		 }
		 ArrayList ci = evdao.getDistinctCompteIndice(e , compte);
		 String retour ="";
		 for(int i=0 ; i<ci.size(); i++){
			 for(int j =0; j<((ArrayList)ci.get(i)).size(); j++){
				 retour+=((ArrayList)ci.get(i)).get(j)+"-";
			 }
		 }
        return retour;
	 }
	 
	 @SuppressWarnings("rawtypes")
	@POST
	 @Path("/recupererValues/compte/{compte}")
	 @Produces("application/json")
	 public String getValuesPOST(String element , @PathParam("compte") long compte){
		 String el[] = element.split("-");
		 ArrayList<Long> e = new ArrayList<Long>();
		 for(int i = 0; i<el.length; i++){
			 e.add(Long.parseLong(el[i]));
		 }
		 ArrayList ci = evdao.getDistinctCompteIndice(e , compte);
		 String retour ="";
		 for(int i=0 ; i<ci.size(); i++){
			 for(int j =0; j<((ArrayList)ci.get(i)).size(); j++){
				 retour+=((ArrayList)ci.get(i)).get(j)+"-";
			 }
		 }
        return retour;
	 }
	 
	 @GET
	 @Path("/getValue/element/{element}/indice/{indice}")
	 @Produces("application/json")
	 public Element_valeur getValue(@PathParam("element") Long element , @PathParam("indice") int indice){
        return evdao.getValue(element , indice);
	 }
	 
	 @GET
	 @Path("/getValuesParIndice/indice/{indice}")
	 @Produces("application/json")
	 public List<Element_valeur> getValuesByIndice(@PathParam("indice") int indice){
	
        return evdao.getValuesByIndice(indice);
	 }
	 
	 @GET
	 @Path("/listerValues")
	 @Produces("application/json") 
     public List<Element_valeur> getAllValues() {
		 return evdao.getAllValues();
     }
     
     @PUT
	@Path("/modifierValeur/valeur/{valeur}/element/{element}/indice/{indice}")
     public Response updateValue(@PathParam("valeur") String valeur, @PathParam("element") long element, @PathParam("indice") int indice) {
    	 evdao.modifierValeur( valeur, element, indice);
    	 return Response.ok().build();
     }
	 
	 
	 
	 
	 

}
