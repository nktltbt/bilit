package com.applifit.bilifit.server.formulaires;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.applifit.bilifit.server.comptes.DAO.CompteDaoImpl;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class CreerUpload extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	private final CompteDaoImpl edao = new CompteDaoImpl();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		log("xxxxx");
		String str = blobstoreService.createUploadUrl("/upload");
		log("yyyyy"+str);
		String str1 = str.replace("/", "3x4x5x6x");
		log("yyyyy"+str1);
		res.sendRedirect("/formulaire/uploadimg/" + str1);
//		res.sendRedirect(str1);
		return;

	}

}
