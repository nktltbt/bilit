package com.applifit.bilifit.client.outils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gwt.user.client.Window;

public class Email {

	
	/*** Fonction d'envoi de l'email ***/
	public static void sendEmail(String email, String pass)
			throws UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		// Le corps de l'email de confirmation
		String msgBody = "Bonjour, <br/><br/> Vous pouvez accéder à votre espace Bi-lifit Web en utilisant les données ci-dessous :  <br/> <br/>          Identifiant : "
				+ email
				+ " <br /> Mot de passe : <b>"
				+ pass
				+ " </b><br/><br/> Nous espérons que vous apprécierez l'utilisation de la plateforme. <br/><br/> L'équipe Bi-lifit,";

		try {
			
			Message msg = new MimeMessage(session);
			msg.setHeader("Content-Type", "text/html");
			/*** L'adresse mail de l'administrateur pour l'envoi de l'email ***/
			msg.setFrom(new InternetAddress(Constantes.EMAIL, "Admin"));
			// Le destinataire
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email, "Mr. User"));

			// Le sujet
			msg.setSubject("Activation du compte Bi-lifit");
			// Envoyer le msg en tant que code HTML
			msg.setContent(msgBody , "text/html; charset=utf-8");
			Transport.send(msg);

		} catch (AddressException e) {
			Window.alert("Adresse Email invalide!");

		} catch (MessagingException e) {
			Window.alert("Error dans l'envoi de message");
		}
	}

}
