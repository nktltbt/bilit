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
import com.applifit.bilifit.server.comptes.DAO.CompteDaoImpl;
import com.applifit.bilifit.server.comptes.DAO.EntrepriseDaoImpl;

import com.sun.jersey.spi.resource.Singleton;

@Singleton
@Path("/compte")
public class CompteRestService {

	private final CompteDaoImpl cdao = new CompteDaoImpl();
	private final EntrepriseDaoImpl edao = new EntrepriseDaoImpl();

	@Context
	UriInfo uriInfo;

	@GET
	@Path("{id}")
	@Produces("application/json")
	public Compte getComptet(@PathParam("id") Long id) {
		return cdao.getCompteById(id);
	}

	@GET
	@Path("/listerCompteSa")
	@Produces("application/json")
	public List<Compte> getComptesSa() {
		return cdao.getCompteByProfil("SA");
	}

	@GET
	@Path("/listerCompte")
	@Produces("application/json")
	public List<Compte> getComptes() {
		return cdao.getComptes();
	}

	@GET
	@Path("/listerCompteEntrepriseAU/client/{entreprise}")
	@Produces("application/json")
	public List<Compte> getComptes(@PathParam("entreprise") Long entreprise) {

		return cdao.getComptesByEntreprise(edao.getEntrepriseById(entreprise));
	}

	@GET
	@Path("/recupererCompte/email/{mail}")
	@Produces("application/json")
	public Compte getCompteParMail(@PathParam("mail") String mail) {
		return cdao.getCompteByMail(mail);
	}

	@GET
	@Path("/rechercherCompte/email/{mail}/nom/{nom}/prenom/{prenom}/fonction/{fonction}")
	@Produces("application/json")
	public List<Compte> rechercherCompteSa(@PathParam("mail") String mail,
			@PathParam("prenom") String prenom, @PathParam("nom") String nom,
			@PathParam("fonction") String fonction) {
		return cdao.getCompteSaByParam(mail, nom, prenom, fonction, "SA");
	}

	@POST
	@Path("/ajouterCompte")
	@Consumes("application/json")
	public Response createCompte(Compte compte) {
		compte.setId(null);
		String password = Utils.genererPass();
		compte.setPassword(Utils.cripterPass(password));
		cdao.ajouterCompte(compte);
		try {
			Email.sendEmail(compte.getMail(), password);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return Response.created(URI.create("/" + compte.getId())).build();
	}

	@PUT
	@Path("/oublierCompte/{mail}")
	@Produces("application/json")
	public void oublierCompte(@PathParam("mail") String mail) {
		Compte compte = cdao.getCompteByMail(mail);
		String password = Utils.genererPass();
		compte.setPassword(Utils.cripterPass(password));
		cdao.modifierCompte(compte);
		try {
			Email.sendEmail(compte.getMail(), password);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@PUT
	@Path("/supprimerCompte/{email}")
	@Produces("application/json")
	public void deleteCompte(@PathParam("email") String email) {
		Compte c = cdao.getCompteByMail(email);
		c.setEtat(0);
		cdao.modifierCompte(c);
	}

	@PUT
	@Path("/supprimerCompteId/{id}")
	@Produces("application/json")
	public void deleteCompteId(@PathParam("id") Long id) {
		Compte c = cdao.getCompteById(id);
		c.setEtat(0);
		cdao.modifierCompte(c);
	}

	@PUT
	@Path("/modifierCompteClient")
	@Consumes("application/json")
	public void updateCompteClient(Compte compte) {
		Compte c = cdao.getCompteById(compte.getId());
		c.setFonction(compte.getFonction());
		c.setNom(compte.getNom());
		c.setPrenom(compte.getPrenom());
		if (compte.getPassword() != null)
			c.setPassword(Utils.cripterPass(compte.getPassword()));
		if (compte.getProfil() != null)
			c.setProfil(compte.getProfil());
		cdao.modifierCompte(c);

	}

	@GET
	@Path("/modifierCompte/id/{id}/nom/{nom}/prenom/{prenom}/fonction/{fonction}/pass/{pass}")
	@Consumes("application/json")
	public String updateCompteClientGet(@PathParam("id") Long id,
			@PathParam("nom") String nom, @PathParam("prenom") String prenom,
			@PathParam("fonction") String fonction,
			@PathParam("pass") String password) {
		Compte c = cdao.getCompteById(id);
		c.setFonction(fonction);
		c.setNom(nom);
		c.setPrenom(prenom);
		c.setPassword(password);
		cdao.modifierCompte(c);
		return null;
	}

	@PUT
	@Path("/modifierCompte")
	@Consumes("application/json")
	public void updateCompte(Compte compte) {
		Compte c = cdao.getCompteByMail(compte.getMail());
		c.setFonction(compte.getFonction());
		c.setNom(compte.getNom());
		c.setPrenom(compte.getPrenom());
		if (compte.getPassword() != null)
			c.setPassword(Utils.cripterPass(compte.getPassword()));
		cdao.modifierCompte(c);

	}

	@DELETE
	@Path("/deleteCompte/{id}")
	@Consumes("application/json")
	public void deleteCompte(@PathParam("id") Long id) {
		cdao.supprimerCompte(id);
	}

	@GET
	@Path("/modifierDevice/device/{device}/id/{id}")
	@Consumes("application/json")
	public String updateDevice(@PathParam("device") String device,
			@PathParam("id") Long id) {
		Compte c = cdao.getCompteById(id);
		if (!device.equals("null"))
			c.setDevice(device);
		else
			c.setDevice(null);
		cdao.modifierCompte(c);
		return null;
	}

}
