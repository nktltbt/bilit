package com.applifit.bilifit.client.export;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import dk.lindhardt.gwt.geie.shared.TableLayout;

public interface GEIESampleServiceAsync {

   /**
    *
    * @param blobkey
    * @return
    */
   void getTableLayout(String blobkey, AsyncCallback<TableLayout> async);

   void prepareExcelDownload(List<TableLayout> layouts, List<String> titles,
		AsyncCallback<String> async);

   /**
    * Prepares a csv download and return the blobkey for the create file
    * @param layout  the table layout to parse to csv
    * @return blobkey created blob key
    */
   void prepareCSVDownload(TableLayout layout, AsyncCallback<String> async);
}
