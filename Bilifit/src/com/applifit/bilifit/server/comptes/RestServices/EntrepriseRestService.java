package com.applifit.bilifit.server.comptes.RestServices;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.applifit.bilifit.client.outils.Email;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.server.comptes.Compte;
import com.applifit.bilifit.server.comptes.Entreprise;
import com.applifit.bilifit.server.comptes.DAO.CompteDaoImpl;
import com.applifit.bilifit.server.comptes.DAO.EntrepriseDaoImpl;
import com.googlecode.objectify.Key;
import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/entreprise")
public class EntrepriseRestService {
	
	 private final EntrepriseDaoImpl edao = new EntrepriseDaoImpl(); 
	 private final CompteDaoImpl cdao = new CompteDaoImpl();
	 
	 
	 @Context
     UriInfo uriInfo;
	 
	 
	 @GET
	 @Path("{id}")
	 @Produces("application/json") 
     public Entreprise getEntreprise(@PathParam("id") Long id) {
		 return edao.getEntrepriseById(id);
     }
	 
	 
	 
	 @SuppressWarnings("rawtypes")
	@POST
	 @Path("/ajouterEntreprise")
	 @Consumes("application/json")
	 public Response createOrUpdateCompte(Entreprise entreprise){
		 if(entreprise.getId()== -1)
			 	entreprise.setId(null);
        entreprise = edao.ajouterEntreprise(entreprise);
        @SuppressWarnings("unchecked")
		Compte compteSA = new Compte(entreprise.getContact_nom(), entreprise.getContact_prenom(), entreprise.getContact_fonction(), entreprise.getContact_mail(), null, "CA",  new Key(Entreprise.class , entreprise.getId()) , 1); 
        compteSA.setId(null);
		String password = Utils.genererPass();
		compteSA.setPassword(Utils.cripterPass(password));
        cdao.ajouterCompte(compteSA);
        try {
			Email.sendEmail(compteSA.getMail(), password);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.created(URI.create("/"+entreprise.getId())).build();
	 }
	 
	 @GET
	 @Path("/listerEntreprise")
	 @Produces("application/json") 
     public List<Entreprise> getEntreprise() {
		 return edao.getEntreprises();
     }
	 
	 
	 @GET
	 @Path("/recupererEntreprise/raisonsocial/{rs}")
	 @Produces("application/json") 
     public Entreprise getEntrepriseByRs(@PathParam("rs") String rs) {
		 return edao.getEntrepriseByRs(rs);
     }
	 
	 @GET
	 @Path("/rechercherEntreprise/rs/{rs}/ville/{ville_cp}/contact_nom/{contact_nom}/contact_mail/{contact_mail}/offre/{offre}/date/{date}")
	 @Produces("application/json") 
     public List<Entreprise> rechercherEntreprise(@PathParam("rs") String rs, @PathParam("ville_cp") String ville_cp, @PathParam("contact_nom") String contact_nom, @PathParam("contact_mail") String contact_mail, @PathParam("offre") String offre, @PathParam("date") String date) {
		 return edao.getEntrepriseByParams(rs, ville_cp, contact_nom, contact_mail, offre, date);
     }
	 
	 @DELETE
	 @Path("/deleteCompte/{id}")
	 @Consumes("application/json")
	 public void deleteEntreprise(@PathParam("id") Long id){
		 edao.supprimerClient(id);
        
	 }
	 
	 @PUT
	 @Path("/modifierEntreprise")
	 @Consumes("application/json")
	 public Response updateEntreprise(Entreprise entreprise){
        entreprise = edao.modifierEntreprise(entreprise);
        return Response.created(URI.create("/"+entreprise.getId())).build();
	 }
	 
	 @PUT
	 @Path("/supprimerEntreprise/{entreprise_id}")
	 @Consumes("application/json")
	 public Response deleteEntreprise(@PathParam("entreprise_id") long entreprise_id){
        edao.supprimerEntreprise(entreprise_id);
        return Response.ok().build();
	 }
	 
	 
	 
}
