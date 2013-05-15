package com.applifit.bilifit.server.formulaires.RestServices;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.applifit.bilifit.server.formulaires.Parametre;
import com.applifit.bilifit.server.formulaires.DAO.ParametreDaoImpl;
import com.sun.jersey.spi.resource.Singleton;


@Singleton
@Path("/parametre")
public class ParametreRestService {

	private final ParametreDaoImpl pdao = new ParametreDaoImpl(); 
	 @Context
    UriInfo uriInfo;
	 
	 
	 @GET
	 @Path("{id}")
	 @Produces("application/json") 
	 public Parametre getParametre(@PathParam("id") Long id) {
		return pdao.getParametreById(id);
	  }
	 
	 
	 @POST
	 @Path("/ajouterParametre")
	 @Consumes("application/json")
	 public Response createOrUpdateCompte(Parametre param){
		 if(param.getId()== -1)
			 param.setId(null);
       pdao.ajouterParametre(param);
       return Response.created(URI.create("/"+param.getId())).build();
	 }
	 
	 @GET
	 @Path("/listerParametre")
	 @Produces("application/json") 
	 public List<Parametre> getParametre() {
		 return pdao.getParametres();
    }
	 
	 
	 @GET
	 @Path("/listerParametre/element/{element}")
	 @Produces("application/json") 
	 public List<Parametre> getParametreByElement(@PathParam("element") Long element) {
		 return pdao.getParametresByElement(element);
    }
	 
}
