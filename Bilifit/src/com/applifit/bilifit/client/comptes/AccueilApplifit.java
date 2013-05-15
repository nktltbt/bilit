package com.applifit.bilifit.client.comptes;

import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.comptes.Accueil_Applifit;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

public class AccueilApplifit implements EntryPoint {

	@Override
	public void onModuleLoad() {

		// Seul les utilisateurs connectés peuvent accéder à cette page
		if (Cookies.getCookie("profil") != null) {
			if (!Cookies.getCookie("profil").equals("SA")
					&& !Cookies.getCookie("profil").equals("CA")
					&& !Cookies.getCookie("profil").equals("CU"))
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "Connexion.html" + Constantes.SUFFIXE_URL);
		} else
			Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html"
					+ Constantes.SUFFIXE_URL);

		Accueil_Applifit accueil_applifit = new Accueil_Applifit(
				Constantes.USER);
		Document.get().getBody().appendChild(accueil_applifit.getElement());

		Anchor anchorwrap = Anchor.wrap(accueil_applifit
				.getAdministration_applifit());

		anchorwrap.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "GestionClient.html" + Constantes.SUFFIXE_URL);

			}

		});

		Document.get().getBody().appendChild(accueil_applifit.getElement());
		Anchor formulaire_link = Anchor.wrap(accueil_applifit
				.getFormulaire_menu_applifit());

		formulaire_link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Cookies.getCookie("profil").equals("SA")
						|| Cookies.getCookie("profil").equals("CA"))
					Window.Location.replace(GWT.getHostPageBaseURL()
							+ "Formulaire_menu_applifit.html"
							+ Constantes.SUFFIXE_URL);
				else
					Window.Location.replace(GWT.getHostPageBaseURL()
							+ "Formulaire_consulter_applifit.html"
							+ Constantes.SUFFIXE_URL);
			}

		});

		if (!Cookies.getCookie("profil").equals("SA")) {
			Anchor deconnexionWrapper = Anchor.wrap(accueil_applifit
					.getDeconnexion());
			deconnexionWrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Utils.deconnexion();
				}

			});
		}
		Document.get().getBody().appendChild(accueil_applifit.getElement());
		Anchor bdd_applifit = Anchor.wrap(accueil_applifit.getBdd_applifit());

		bdd_applifit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "Bdd_applifit.html" + Constantes.SUFFIXE_URL);
			}

		});

		Anchor acceuilWrapper = Anchor.wrap(accueil_applifit.getAccueil());
		acceuilWrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(GWT.getHostPageBaseURL()
						+ "Accueil_superadmin.html" + Constantes.SUFFIXE_URL);
			}

		});
		if (!Cookies.getCookie("profil").equals("SA"))
			accueil_applifit.getAccueil().removeFromParent();

		if (Cookies.getCookie("profil").equals("CU")) {
			accueil_applifit.getDashbords().removeFromParent();
			accueil_applifit.getBdd_applifit().removeFromParent();
		}

	}

}
