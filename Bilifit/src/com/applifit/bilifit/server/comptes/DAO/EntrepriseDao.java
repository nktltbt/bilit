package com.applifit.bilifit.server.comptes.DAO;

import java.util.List;

import com.applifit.bilifit.server.comptes.Entreprise;

public interface EntrepriseDao {

	Entreprise ajouterEntreprise(Entreprise entreprise);

	Entreprise getEntrepriseById(Long id);

	List<Entreprise> getEntreprises();

	Entreprise getEntrepriseByRs(String rs);

	List<Entreprise> getEntrepriseByParams(String rs, String ville_cp,
			String contact_nom, String contact_mail, String offre, String date);

	void supprimerClient(Long id);

	boolean comprarerEntreprises(Entreprise e1, Entreprise e2);

	Entreprise modifierEntreprise(Entreprise entreprise);

	void supprimerEntreprise(long entreprise);

}
