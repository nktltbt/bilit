package com.applifit.bilifit.client.formulaires;

import com.applifit.bilifit.client.outils.Constantes;
import com.applifit.bilifit.client.outils.Utils;
import com.applifit.bilifit.client.templatesUibinder.formulaires.Body_menu_formulaire;
import com.applifit.bilifit.client.templatesUibinder.formulaires.MenuForm;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;

public class Formulaire_menu_applifit implements EntryPoint {

	@Override
	public void onModuleLoad() {

		if (Cookies.getCookie("profil") == null)
			Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html"
					+ Constantes.SUFFIXE_URL);
		else {
			MenuForm menu = new MenuForm(Constantes.USER);
			Body_menu_formulaire body = new Body_menu_formulaire();

			Document.get().getBody().appendChild(menu.getElement());
			Document.get().getBody()
					.insertAfter(body.getElement(), menu.getElement());

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
					if (Cookies.getCookie("profil").equals("SA"))
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Accueil_superadmin.html"
								+ Constantes.SUFFIXE_URL);
					else
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Accueil_applifit.html"
								+ Constantes.SUFFIXE_URL);
				}

			});

			/*** action de rubrique formulaire ***/
			Anchor formulaireWrapper = Anchor.wrap(menu.getFormulaire());
			formulaireWrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (Cookies.getCookie("profil").equals("SA"))
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Formulaire_menu_applifit.html"
								+ Constantes.SUFFIXE_URL);
					else
						Window.Location.replace(GWT.getHostPageBaseURL()
								+ "Formulaire_consulter_applifit.html"
								+ Constantes.SUFFIXE_URL);
				}

			});
			Anchor creationWrapper = Anchor.wrap(body.getCreation());
			creationWrapper.setHref(GWT.getHostPageBaseURL()
					+ "Formulaire_applifit.html" + Constantes.SUFFIXE_URL);
			Anchor modifierWrapper = Anchor.wrap(body.getModification());
			modifierWrapper.setHref(GWT.getHostPageBaseURL()
					+ "Formulaire_modifier_applifit.html"
					+ Constantes.SUFFIXE_URL);
			if (Cookies.getCookie("profil").equals("CU")) {
				body.getCreation().removeFromParent();
				body.getModification().removeFromParent();
			}

		}
	}

}
