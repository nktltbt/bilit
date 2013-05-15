package com.applifit.bilifit.server.formulaires;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

public class Upload extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		log("iiiiiii");
		BlobKey blobKey = null;
		log("iiiiiii1");
		// Policy.setPolicy(new Policy() {
		// public void refresh() {
		// }
		//
		// public PermissionCollection getPermissions(CodeSource arg0) {
		// Permissions perms = new Permissions();
		// perms.add(new AllPermission());
		// return (perms);
		// }
		// });
//		try {
//			Enumeration e = req.getParameterNames();
//
//			while (e.hasMoreElements()) {
//				String str = (String) e.nextElement();
//				log(str);
//			}
//			String nom = req.getParameter("nom");
//			log(nom);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		try {
//			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
//			log("iii" + blobs.size());
//			blobKey = blobs.get("uploaded_file").get(0);
//			log("iiiiiii2");
//		} catch (Exception e) {
//			// Window.alert("EURREUR");
//			log("xxx" + e.getMessage());
//			String str = e.getMessage().replace("/", "3x4x5x6x");
//			res.sendRedirect("/formulaire/uploadimg/" + str + "aaaaa1");
//			return;
//
//		}
		 ServletFileUpload upload = new ServletFileUpload();
		    FileItemIterator iter=null;
			try {
				iter = upload.getItemIterator(req);
				log("ok1");
			} catch (FileUploadException e1) {
				log(e1.getMessage());
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    FileItemStream imageItem=null;
			try {
				imageItem = iter.next();
				log("ok2");
			} catch (FileUploadException e1) {
				// TODO Auto-generated catch block
				log(e1.getMessage());
				e1.printStackTrace();
			}
		    InputStream imgStream = imageItem.openStream();
//		log("iiiiiii");
//		String dirName = req.getSession().getServletContext()
//				.getRealPath("/WEB-INF/");
//		log("iiiiiii1");
//		String fileName = null;

//		MultipartRequest multi = null;
//
//		try {
//			multi = new MultipartRequest(req, dirName, 1024 * 1024,
//					"ISO-8859-1");
//			log("iiiiiii2");
//		} catch (IOException ex) {
//			ex.printStackTrace();
//
//		}
//		try {
//			Enumeration files = multi.getFileNames();
//			log("iiiiiii4");
//			log(multi.getParameter("nom"));
//			log("iiiiiii5");
//			while (files.hasMoreElements()) {
//				String name = (String) files.nextElement();
//				log("iii6" + name);
//				fileName = multi.getFilesystemName(name);
//			}
//			// String filePath = dirName + System.getProperty("file.separator")
//			// + fileName;
//			// File clientImage = new File(filePath);
//
//		} catch (Exception ioe) {
//			ioe.printStackTrace();
//			log("\nFile does not exist or cant delete\n");
//		}
		try {
			log("iiiiiii3");
			ImagesService imagesService = ImagesServiceFactory
					.getImagesService();

			Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
			Transform resize = ImagesServiceFactory.makeResize(300, 300, true);

			Image newImage = imagesService.applyTransform(resize, oldImage);

			// Get a file service

			FileService fileService = FileServiceFactory.getFileService();

			// Create a new Blob file with mime-type "text/plain"
			AppEngineFile file = fileService.createNewBlobFile("image/jpeg");

			// Open a channel to write to it
			boolean lock = false;
			// FileWriteChannel writeChannel =
			// fileService.openWriteChannel(file, lock);
			//
			// // Different standard Java ways of writing to the channel
			// // are possible. Here we use a PrintWriter:
			// PrintWriter out = new
			// PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
			// out.println("The woods are lovely dark and deep.");
			// out.println("But I have promises to keep.");
			//
			// // Close without finalizing and save the file path for writing
			// later
			// out.close();
			// String path = file.getFullPath();
			//
			// // Write more to the file in a separate request:
			// file = new AppEngineFile(path);

			// This time lock because we intend to finalize
			log("gggg");
			lock = true;
			FileWriteChannel writeChannel = fileService.openWriteChannel(file,
					lock);

			// This time we write to the channel directly
			writeChannel.write(ByteBuffer.wrap(newImage.getImageData()));

			// Now finalize
			writeChannel.closeFinally();
			BlobKey blobKey1 = fileService.getBlobKey(file);
			log(blobKey1.getKeyString());
			res.sendRedirect("/formulaire/uploadimg/" + blobKey1.getKeyString());
			return;
		} catch (Exception e) {

			System.out.println("Uploadxxxxx");
			// Window.alert("retry");
			String str = e.getMessage().replace("/", "3x4x5x6x");
			res.sendRedirect("/formulaire/uploadimg/" + str + "aaaaa2");
			return;
		}

	}

}
