package com.applifit.bilifit.server.Export;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: AnAmuser
 * Date: 03-06-11
 * <p/>
 * Handling export get requests
 */
public class Export extends HttpServlet {

   protected void doGet(
         HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {
      try {
         String type = request.getParameter("type");
         String key = request.getParameter("key");
         String name = request.getParameter("name");
         BlobKey blobKey = new BlobKey(key);
         BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();

         if (type.equals("xls")) {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + name+".xls");
         } else if (type.equals("csv")) {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "example.csv");
         }

         blobStoreService.serve(blobKey, response);
      } catch (Exception e) {
         throw new ServletException("File was not generated", e);
      }
   }
}
