package com.applifit.bilifit.server.Export;

import com.applifit.bilifit.client.export.GEIESampleService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import dk.lindhardt.gwt.geie.server.TableLayout2CSV;
import dk.lindhardt.gwt.geie.server.TableLayout2Excel;
import dk.lindhardt.gwt.geie.shared.TableLayout;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.List;

public class GEIESampleServiceImpl extends RemoteServiceServlet implements GEIESampleService {


   public TableLayout getTableLayout(String blobkey) {
      FileService service = FileServiceFactory.getFileService();
      try {
         AppEngineFile file = service.getBlobFile(new BlobKey(blobkey));
         FileReadChannel readChannel = service.openReadChannel(file, true);
         InputStream inputStream = Channels.newInputStream(readChannel);
         ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(inputStream, 512));
         Object object = in.readObject();
         in.close();
         readChannel.close();
         if (object instanceof TableLayout) {
            return (TableLayout) object;
         } else {
            throw new RuntimeException("Did not find file with key " + blobkey);
         }
      } catch (Exception e) {
         throw new RuntimeException("Reading file " + blobkey + " did not work", e);
      }
   }
   
   public String prepareExcelDownload(List<TableLayout> layouts, List<String> titles) {
      FileService fileService = FileServiceFactory.getFileService();
      AppEngineFile file = null;
      try {
    	  
         file = fileService.createNewBlobFile("application/vnd.ms-excel");
         FileWriteChannel writeChannel = fileService.openWriteChannel(file, true);

         writeChannel.write(ByteBuffer.wrap(generateExcelFile(layouts, titles)));
         
         writeChannel.closeFinally();
         

         BlobKey blobKey = fileService.getBlobKey(file);
         return blobKey.getKeyString();
      } catch (Exception e) {
         throw new RuntimeException("Error creating excel file", e);
      }
   }

   private byte[] generateExcelFile(List<TableLayout> layouts, List<String> titles) throws Exception {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      WritableWorkbook workBook = Workbook.createWorkbook(outputStream);
      for(int i = 0; i<layouts.size(); i++){
    	  TableLayout2Excel excelBuilder = new TableLayout2Excel(layouts.get(i));
          excelBuilder.build(workBook, titles.get(i), 0);
      }
      
      workBook.write();
      workBook.close();
      
      return outputStream.toByteArray();
   }

   public String prepareCSVDownload(TableLayout layout) {
      FileService fileService = FileServiceFactory.getFileService();
      AppEngineFile file = null;
      try {
         file = fileService.createNewBlobFile("text/csv");
         FileWriteChannel writeChannel = fileService.openWriteChannel(file, true);

         writeChannel.write(ByteBuffer.wrap(generateCSVFile(layout)));
         writeChannel.closeFinally();

         BlobKey blobKey = fileService.getBlobKey(file);
         return blobKey.getKeyString();
      } catch (Exception e) {
         throw new RuntimeException("Error creating csv file", e);
      }
   }

   private byte[] generateCSVFile(TableLayout layout) throws Exception {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      TableLayout2CSV parser = new TableLayout2CSV(layout);
      parser.build(outputStream);
      outputStream.flush();
      outputStream.close();
      return outputStream.toByteArray();
   }


}