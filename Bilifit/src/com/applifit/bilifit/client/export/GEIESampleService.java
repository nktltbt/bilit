package com.applifit.bilifit.client.export;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import dk.lindhardt.gwt.geie.shared.TableLayout;

@RemoteServiceRelativePath("GEIESampleService")
public interface GEIESampleService extends RemoteService {

   /**
    * Gets table layout with the given key
    * @param blobkey  the blob key for the table layout
    * @return the table layout
    */
   TableLayout getTableLayout(String blobkey);

   /**
    * Prepares a excel download and return the blobkey for the create file
    * @param layout  the table layout to parse to excel
    * @return blobkey created blob key
    */
   String prepareExcelDownload(List<TableLayout> layout , List<String> titles);

   /**
    * Prepares a csv download and return the blobkey for the create file
    * @param layout  the table layout to parse to csv
    * @return blobkey created blob key
    */
   String prepareCSVDownload(TableLayout layout);

   /**
    * Utility/Convenience class.
    * Use SampleApplicationService.App.getInstance() to access static instance of GEIESampleServiceAsync
    */
   public static class App {
      private static GEIESampleServiceAsync ourInstance = GWT.create(GEIESampleService.class);

      public static synchronized GEIESampleServiceAsync getInstance() {
         return ourInstance;
      }
   }
}
