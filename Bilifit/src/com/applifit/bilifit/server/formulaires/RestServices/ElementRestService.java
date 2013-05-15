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

import com.applifit.bilifit.server.formulaires.Element;
import com.applifit.bilifit.server.formulaires.DAO.ElementDaoImpl;
import com.sun.jersey.spi.resource.Singleton;


@Singleton
@Path("/element")
public class ElementRestService {
	
	 private final ElementDaoImpl edao = new ElementDaoImpl();
	 
	 @Context
     UriInfo uriInfo;
	 
	 
	 @POST
	 @Path("/ajouterElement")
	 @Consumes("application/json")
	 public Response createElement(Element element){
		element.setId(null);
        edao.ajouterElement(element);
        return Response.created(URI.create("/"+element.getId())).build();
	 }
	 
	 
	 @GET
	 @Path("/listerElement")
	 @Produces("application/json") 
     public List<Element> getElements() {
		 return edao.getElements();
     }
	 
	 
	 @GET
	 @Path("/recupererElement/pos_x/{position_x}/pos_y/{position_y}/formulaire/{idform}")
	 @Produces("application/json") 
     public Element getElementByPosition(@PathParam("position_x") int position_x, @PathParam("position_y") int position_y, @PathParam("idform") Long idform) {
		 return edao.getElementByParams(position_x, position_y, idform);
     }
	 
	 @GET
	 @Path("/listerElement/formulaire/{formulaire}")
	 @Produces("application/json") 
     public List<Element> getElementsbyForm(@PathParam("formulaire") long formulaire) {
		 return edao.getElementsByForm(formulaire);
     }

	 

}
