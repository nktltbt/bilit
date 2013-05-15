package com.applifit.bilifit.client.outils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/***
 * Objectif: Regrouper toutes des fonction fréquament utilisée (Réutilisable)
 * 
 * ***/

public class Utils {

	// Ccrypter une chaine de caractère en MD5
	public static String cripterPass(String password) {
		MessageDigest algorithm;
		try {
			algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(password.getBytes());
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');

				hexString.append(hex);
			}
			password = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return password;
	}

	// Générer une suite de carractère aléatoire (6 caractères)
	public static String genererPass() {
		char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}

		return sb.toString();

	}

	//Vérifier si le profil de l'utilisateur connecté correspond bien au profil "profil"
	public static boolean VerificationProfil(String profil) {

		if (Cookies.getCookie("profil").equals(profil))
			return true;
		return false;
	}

	// Vider les cookies avant de se deconnecter et redirection vers la page de connexion
	public static void deconnexion() {
		Cookies.removeCookie("id");
		Cookies.removeCookie("mail");
		Cookies.removeCookie("nom");
		Cookies.removeCookie("prenom");
		Cookies.removeCookie("profil");
		Cookies.removeCookie("client");
		Window.Location.replace(GWT.getHostPageBaseURL() + "Connexion.html"
				+ Constantes.SUFFIXE_URL);
	}
	
	
	//Popup pour les messages 
	public static DialogBox createDialogBox(String msg) {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Informations additionnelles");
		dialogBox.setAnimationEnabled(true);
		Button okButton = new Button("Ok");
		okButton.getElement().setId("okButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setWidth("300px");
		dialogVPanel.add(new HTML("<b>"+msg+"</b>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(okButton);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.setPopupPosition(450, 450);

		/*** Action de clique sur le button de valisation ***/
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		return dialogBox;
	}
	
	//Retourne la date d'aujourd'hui
	public static String getToday()
	{
	  Date today = new Date();
	  DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd");
	  return fmt.format(today);
	}
	
	
	
	
	//Popup pour les messages de choix oui/non
		public static DialogBox createDialogBoxChoix(String msg) {
			final DialogBox dialogBox = new DialogBox();
			dialogBox.setText("Informations additionnelles");
			dialogBox.setAnimationEnabled(true);
			Button okButton = new Button("Oui");
			okButton.getElement().setId("okButton");
			Button noButton = new Button("Non");
			noButton.getElement().setId("noButton");
			
			VerticalPanel dialogVPanel = new VerticalPanel();
			dialogVPanel.setWidth("300px");
			dialogVPanel.add(new HTML("<b>"+msg+"</b>"));
			dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			HorizontalPanel dialogHPanel = new HorizontalPanel();
			dialogHPanel.add(okButton);
			dialogHPanel.add(noButton);
			dialogVPanel.add(dialogHPanel);
			dialogBox.setWidget(dialogVPanel);
			dialogBox.setPopupPosition(450, 450);

			return dialogBox;
		}

		/*** Passer du format de date aaaa-mm-jj à jj/mm/aaaa ***/
		public static String dateFormat(String date){
			String d[] = date.split("-");
			String newDate="";
			if(Integer.parseInt(d[2]) < 10 && d[2].length() == 1){
				newDate="0"+d[2]+"/";
			}else newDate=d[2]+"/";
			if(Integer.parseInt(d[1]) < 10 && d[1].length() == 1){
				newDate=newDate+"0"+d[1]+"/";
			}else newDate=newDate+d[1]+"/";
			newDate = newDate+d[0];
			return newDate;
		}
		
		/*** Passer du format de date jj/mm/aaaa à aaaa-mm-jj ***/
		public static String dateFormatInverse(String date){
			String d[] = date.split("/");
			String newDate=d[2]+"-"+d[1]+"-"+d[0];
			return newDate;
		}
	
		//Génerer id 
		public static String  generateId(){
			char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < 5; i++) {
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}
			return sb.toString();
		}

		

		

}
